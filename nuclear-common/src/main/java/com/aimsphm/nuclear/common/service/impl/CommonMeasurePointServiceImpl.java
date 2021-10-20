package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.FeatureExtractionParamDTO;
import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.entity.vo.PointFeatureVO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.common.enums.AlarmMessageEnum;
import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import com.aimsphm.nuclear.common.enums.PointFeatureEnum;
import com.aimsphm.nuclear.common.enums.PointVisibleEnum;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.CommonMeasurePointMapper;
import com.aimsphm.nuclear.common.service.*;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.*;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.*;

/**
 * <p>
 * 功能描述:测点信息扩展服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-11-16 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonMeasurePointServiceImpl extends ServiceImpl<CommonMeasurePointMapper, CommonMeasurePointDO> implements CommonMeasurePointService {
    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redis;

    @Resource
    private CommonDeviceService deviceServiceExt;

    @Resource
    private CommonSensorService sensorService;

    @Resource
    private CommonSubSystemService subSystemServiceExt;

    @Resource
    private AlgorithmModelPointService algorithmModelPointService;

    /**
     * 缓存队列的长度
     */
    @Value("${customer.config.cache_queue_data_size:60}")
    private Integer CACHE_QUEUE_DATA_SIZE;


    @Override
    public Page<CommonMeasurePointDO> listCommonMeasurePointByPageWithParams(QueryBO<CommonMeasurePointDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getStart()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return this.page(queryBO.getPage(), wrapper);
    }

    @Async
    @Override
    public void updateMeasurePointsInRedis(String itemId, Double value, Long timestamp) {
        if (StringUtils.isEmpty(itemId)) {
            return;
        }
        List<MeasurePointVO> vos = this.getMeasurePointsByPointId(itemId);
        if (CollectionUtils.isEmpty(vos)) {
            return;
        }
        vos.forEach(item -> store2Redis(item, value, timestamp));
        //缓存指定长度的队列
        cacheQueueData(vos, timestamp);
    }

    @Override
    public Double calculatePointValueFromRedis(String itemId, Double value) {
        if (StringUtils.isEmpty(itemId) || Objects.isNull(value)) {
            return 0D;
        }
        List<MeasurePointVO> vos = this.getMeasurePointsByPointId(itemId);
        if (CollectionUtils.isEmpty(vos)) {
            return 0D;
        }
        String storeKey = getStoreKey(vos.get(0));
        MeasurePointVO vo = (MeasurePointVO) redis.opsForValue().get(storeKey);
        //如果没有数据增量值是0
        if (Objects.isNull(vo) || Objects.isNull(vo.getValue())) {
            return 0D;
        }
        double sub = value - vo.getValue();
        return sub < 0 ? 0D : sub;
    }

    /**
     * 缓存队列数据
     *
     * @param vos       集合
     * @param timestamp 时间戳
     */
    private void cacheQueueData(List<MeasurePointVO> vos, Long timestamp) {
        for (MeasurePointVO point : vos) {
            Long id = point.getId();
            LambdaQueryWrapper<AlgorithmModelPointDO> query = Wrappers.lambdaQuery(AlgorithmModelPointDO.class);
            query.eq(AlgorithmModelPointDO::getPointId, point.getId());
            //没有被配置到模型中的数据不做缓存
            int count = algorithmModelPointService.count(query);
            if (count == 0) {
                continue;
            }
            String key = REDIS_QUEUE_REAL_TIME_PRE + id;
            Long size = redis.opsForList().size(key);
            HBaseTimeSeriesDataDTO data = new HBaseTimeSeriesDataDTO();
            data.setValue(point.getValue());
            data.setTimestamp(timestamp);
            if (Objects.nonNull(size) && size.intValue() >= CACHE_QUEUE_DATA_SIZE) {
                redis.opsForList().rightPush(key, data);
                redis.opsForList().leftPop(key);
                continue;
            }
            redis.opsForList().rightPush(key, data);
        }
    }

    @Override
    public CommonMeasurePointDO getPointByPointId(String pointId) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonMeasurePointDO::getPointId, pointId).last("limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public Boolean isNeedDownSample(String pointId) {
        CommonMeasurePointDO point = this.getPointByPointId(pointId);
        if (Objects.isNull(point)) {
            throw new CustomMessageException("the point not exist");
        }
        return isNeedDownSample(point);
    }

    @Override
    public Boolean isNeedDownSample(CommonMeasurePointDO point) {
        return Objects.nonNull(point.getVisible()) && point.getVisible() % PointVisibleEnum.DOWN_SAMPLE.getCategory() == 0;
    }

    @Override
    @Cacheable(value = REDIS_POINT_INFO_LIST, key = "#itemId")
    public List<MeasurePointVO> getMeasurePointsByPointId(String itemId) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonMeasurePointDO::getPointId, itemId);
        List<CommonMeasurePointDO> pointDOList = this.list(wrapper);
        if (CollectionUtils.isEmpty(pointDOList)) {
            return null;
        }
        return pointDOList.stream().map(item -> {
            MeasurePointVO vo = new MeasurePointVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = REDIS_KEY_FEATURES)
    public Set<String> listFeatures() {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(CommonMeasurePointDO::getPointType, 1);
        List<CommonMeasurePointDO> pointDOList = this.list(wrapper);
        if (CollectionUtils.isEmpty(pointDOList)) {
            return null;
        }
        return pointDOList.stream().map(item -> item.getFeatureType() + DASH + item.getFeature()).collect(Collectors.toSet());
    }

    public PointFeatureVO listFeatures(List<String> sensorCodeList) {
        PointFeatureVO vo = new PointFeatureVO();
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CommonMeasurePointDO::getFeatureType, CommonMeasurePointDO::getFeature, CommonMeasurePointDO::getFeatureName);
        wrapper.in(CommonMeasurePointDO::getSensorCode, sensorCodeList).isNotNull(CommonMeasurePointDO::getFeatureType).isNotNull(CommonMeasurePointDO::getFeature);
        //因为替别人解决一些问题，需要剔除以下这个几个特征.
        wrapper.notIn(CommonMeasurePointDO::getFeature, Lists.newArrayList("BGER3", "MPFF3", "MPE", "GMSBF"));
        List<CommonMeasurePointDO> pointDOList = this.list(wrapper);
        if (CollectionUtils.isEmpty(pointDOList)) {
            return vo;
        }
        Map<String, List<CommonMeasurePointDO>> collect = pointDOList.stream().collect(Collectors.groupingBy(CommonMeasurePointDO::getFeatureType));
        List<TreeVO> list = collect.entrySet().stream().map(item -> {
            List<CommonMeasurePointDO> features = item.getValue();
            TreeVO treeVO = new TreeVO(item.getKey(), PointFeatureEnum.getLabel(item.getKey()));
            Set<TreeVO> children = features.stream().map(o -> new TreeVO(o.getFeature(), o.getFeatureName())).collect(Collectors.toSet());
            treeVO.setChildren(Lists.newArrayList(children));
            return treeVO;
        }).collect(Collectors.toList());
        vo.setList(list);
        return vo;
    }

    @Override
    public void clearAllPointsData() {
        Set<String> keys = redis.keys(REDIS_POINT_REAL_TIME_PRE + STAR);
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        redis.delete(keys);
    }

    @Override
    public List<CommonMeasurePointDO> listPointsByConditions(CommonQueryBO query) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = wrapperByConditions(query);
        if (PointVisibleEnum.THRESHOLD.getCategory().equals(query.getVisible())) {
            wrapper.orderByAsc(CommonMeasurePointDO::getPointId);
        } else {
            wrapper.orderByAsc(CommonMeasurePointDO::getSort);
        }
        return this.list(wrapper);
    }

    private LambdaQueryWrapper<CommonMeasurePointDO> wrapperByConditions(CommonQueryBO query) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = initWrapper(query);
        if (Objects.nonNull(query.getCategory())) {
            wrapper.and(x -> x.eq(CommonMeasurePointDO::getCategory, query.getCategory()));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(query.getFeatureType())) {
            wrapper.and(x -> x.eq(CommonMeasurePointDO::getFeatureType, query.getFeatureType()));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(query.getFeature())) {
            wrapper.and(x -> x.eq(CommonMeasurePointDO::getFeature, query.getFeature()));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(query.getLocationCode())) {
            wrapper.and(x -> x.eq(CommonMeasurePointDO::getLocationCode, query.getLocationCode()));
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(query.getSensorCode())) {
            wrapper.and(x -> x.eq(CommonMeasurePointDO::getSensorCode, query.getSensorCode()));
        }
        if (Objects.nonNull(query.getVisible())) {
            wrapper.apply("visible%" + query.getVisible() + "=0");
        }
        return wrapper;
    }

    @Override
    public List<LabelVO> listLocationInfo(CommonQueryBO query) {
        Map<String, CommonSensorDO> collect = listSensorCodeLocation(query);
        if (MapUtils.isEmpty(collect)) {
            return null;
        }
        return collect.entrySet().stream().sorted((a, b) -> {
            CommonSensorDO left = a.getValue();
            CommonSensorDO right = b.getValue();
            if (Objects.isNull(left.getSort()) || Objects.isNull(right.getSort())) {
                return 0;
            }
            return left.getSort().compareTo(right.getSort());
        }).map(x -> new LabelVO(x.getValue().getLocation(), x.getKey())).collect(Collectors.toList());
    }

    public Map<String, CommonSensorDO> listSensorCodeLocation(CommonQueryBO query) {
        LambdaQueryWrapper<CommonSensorDO> wrapper = Wrappers.lambdaQuery(CommonSensorDO.class);
        wrapper.select(CommonSensorDO::getLocation, CommonSensorDO::getLocationCode, CommonSensorDO::getSort, CommonSensorDO::getSensorCode);
        if (Objects.nonNull(query.getSubSystemId())) {
            wrapper.eq(CommonSensorDO::getSubSystemId, query.getSubSystemId());
        }
        if (Objects.nonNull(query.getDeviceId())) {
            wrapper.eq(CommonSensorDO::getDeviceId, query.getDeviceId());
        }
        if (StringUtils.hasText(query.getLocationCode())) {
            wrapper.eq(CommonSensorDO::getLocationCode, query.getLocationCode());
        }
        if (Objects.nonNull(query.getCategory())) {
            wrapper.eq(CommonSensorDO::getCategory, query.getCategory());
        }
        List<CommonSensorDO> list = sensorService.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().collect(Collectors.toMap(CommonSensorDO::getLocationCode, x -> x, (a, b) -> Objects.isNull(a.getSort()) ? b : a));
    }

    @Override
    public List<String> listSensorCodeByPointList(List<String> pointIdList) {
        LambdaQueryWrapper<CommonMeasurePointDO> query = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        query.in(CommonMeasurePointDO::getPointId, pointIdList);
        List<CommonMeasurePointDO> list = this.list(query);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().map(CommonMeasurePointDO::getSensorCode).distinct().collect(Collectors.toList());
    }

    @Override
    public List<CommonMeasurePointDO> listOilPoint(Long deviceId) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        wrapper.eq(CommonMeasurePointDO::getDeviceId, deviceId).eq(CommonMeasurePointDO::getCategory, PointCategoryEnum.OIL_QUALITY.getValue());
        return this.list(wrapper);
    }

    @Override
    public List<CommonMeasurePointDO> listSensorByGroup(CommonQueryBO query) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = wrapperByConditions(query);
        wrapper.select(CommonMeasurePointDO::getId, CommonMeasurePointDO::getSensorCode, CommonMeasurePointDO::getCategory, CommonMeasurePointDO::getSensorName, CommonMeasurePointDO::getPointType, CommonMeasurePointDO::getPointName);
        wrapper.groupBy(CommonMeasurePointDO::getSensorCode, CommonMeasurePointDO::getPointType);
        wrapper.orderByAsc(CommonMeasurePointDO::getPointId);
        return this.list(wrapper);
    }

    @Override
    public Map<String, Long> listPointByDeviceIdInModel(List<String> pointIds) {
        if (CollectionUtils.isEmpty(pointIds)) {
            return null;
        }
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        wrapper.in(CommonMeasurePointDO::getPointId, pointIds);
        List<CommonMeasurePointDO> list = this.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Map<Long, CommonMeasurePointDO> ids = list.stream().collect(Collectors.toMap(BaseDO::getId, x -> x));
        Map<String, CommonMeasurePointDO> pointDOMap = list.stream().collect(Collectors.toMap(CommonMeasurePointDO::getPointId, x -> x, (a, b) -> a));
        LambdaQueryWrapper<AlgorithmModelPointDO> modelWrapper = Wrappers.lambdaQuery(AlgorithmModelPointDO.class);
        modelWrapper.in(AlgorithmModelPointDO::getPointId, ids.keySet());
        modelWrapper.last("and exists  (select model_id from algorithm_model where id=model_id and model_type=1)");
        List<AlgorithmModelPointDO> modelList = algorithmModelPointService.list(modelWrapper);
        //一个测点在多个模型中，取第一个模型的id
        Map<Long, AlgorithmModelPointDO> collect = modelList.stream().collect(Collectors.toMap(AlgorithmModelPointDO::getPointId, x -> x, (a, b) -> a));
        return pointIds.stream().distinct().collect(Collectors.toMap(x -> x, x -> {
            CommonMeasurePointDO pointDO = pointDOMap.get(x);
            if (Objects.isNull(pointDO)) {
                return -1L;
            }
            AlgorithmModelPointDO modelPointDO = collect.get(pointDO.getId());
            if (Objects.isNull(modelPointDO)) {
                return -1L;
            }
            return modelPointDO.getModelId();
        }));
    }

    @Override
    public List<CommonMeasurePointDO> listPointAliasAndName(List<String> pointIDList, CommonQueryBO queryBO) {
        if (CollectionUtils.isEmpty(pointIDList)) {
            return null;
        }
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = initWrapper(queryBO);
        wrapper.in(CommonMeasurePointDO::getPointId, pointIDList);
        wrapper.select(CommonMeasurePointDO::getPointId, CommonMeasurePointDO::getAlias, CommonMeasurePointDO::getPointName);
        return this.list(wrapper);
    }

    @Override
    public List<FeatureExtractionParamDTO> listFeatureExtraction(Integer value) {
        return this.getBaseMapper().listFeatureExtraction(value);
    }

    @Override
    public boolean modifyCommonMeasurePoint(CommonMeasurePointDO dto) {
        LambdaUpdateWrapper<CommonMeasurePointDO> update = Wrappers.lambdaUpdate(CommonMeasurePointDO.class);
        update.eq(CommonMeasurePointDO::getId, dto.getId());
        update.set(CommonMeasurePointDO::getThresholdLow, dto.getThresholdLow());
        update.set(CommonMeasurePointDO::getThresholdHigh, dto.getThresholdHigh());
        update.set(CommonMeasurePointDO::getThresholdLower, dto.getThresholdLower());
        update.set(CommonMeasurePointDO::getThresholdHigher, dto.getThresholdHigher());
        update.set(CommonMeasurePointDO::getEarlyWarningLow, dto.getEarlyWarningLow());
        update.set(CommonMeasurePointDO::getEarlyWarningHigh, dto.getEarlyWarningHigh());
        return this.getBaseMapper().update(dto, update) > 0;
    }

    @Override
    public PointFeatureVO listFeaturesByConditions(CommonQueryBO query) {
        if (StringUtils.hasText(query.getSensorCode())) {
            List<String> collect = Arrays.stream(query.getSensorCode().split(COMMA)).collect(Collectors.toList());
            return listFeatures(collect);
        }
        Map<String, CommonSensorDO> data = this.listSensorCodeLocation(query);
        if (MapUtils.isEmpty(data)) {
            return null;
        }
        Set<String> collect = data.values().stream().filter(x -> StringUtils.hasText(x.getSensorCode())).map(CommonSensorDO::getSensorCode).collect(Collectors.toSet());
        return listFeatures(Lists.newArrayList(collect));
    }

    /**
     * 组装查询条件
     * 目前能支持到系统下公共测点
     *
     * @param query 查询条件
     * @return 封装后的条件
     */
    private LambdaQueryWrapper<CommonMeasurePointDO> initWrapper(CommonQueryBO query) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        if (Objects.isNull(query.getSystemId()) && Objects.isNull(query.getSubSystemId()) && Objects.isNull(query.getDeviceId()) && Objects.isNull(query.getVisible())) {
            return wrapper;
        }
        if (Objects.nonNull(query.getSystemId())) {
            wrapper.eq(CommonMeasurePointDO::getSystemId, query.getSystemId());
            return wrapper;
        }
        if (Objects.nonNull(query.getSubSystemId())) {
            CommonSubSystemDO subSystem = subSystemServiceExt.getById(query.getSubSystemId());
            if (Objects.isNull(subSystem)) {
                throw new CustomMessageException("该子系统下没有数据");
            }
            wrapper.and(w -> w.eq(CommonMeasurePointDO::getSubSystemId, subSystem.getId())
                    .or().eq(CommonMeasurePointDO::getSystemId, subSystem.getSystemId()).isNull(CommonMeasurePointDO::getSubSystemId));
            return wrapper;
        }
        CommonDeviceDO device = deviceServiceExt.getById(query.getDeviceId());
        if (Objects.isNull(device)) {
            throw new CustomMessageException("该设备下没有数据");
        }
        wrapper.and(w -> w.eq(CommonMeasurePointDO::getDeviceId, device.getId())
                .or().eq(CommonMeasurePointDO::getSubSystemId, device.getSubSystemId()).isNull(CommonMeasurePointDO::getDeviceId)
                .or().eq(CommonMeasurePointDO::getSystemId, device.getSystemId()).isNull(CommonMeasurePointDO::getDeviceId).isNull(CommonMeasurePointDO::getSubSystemId)
        );
        return wrapper;
    }

    @Override
    public void store2Redis(MeasurePointVO vo, Double value, Long timestamp) {
        if (Objects.nonNull(value)) {
            vo.setValue(value);
        }
        //计算健康状况
        calculateHealthStatus(vo);
        if (StringUtils.hasText(vo.getStatusCause())) {
            if (PointCategoryEnum.ALARM.getValue() != vo.getCategory().byteValue()) {
                //设置单位
                vo.setStatusCause(vo.getStatusCause() + vo.getUnit());
            }
        }
        //数据产生时间
        vo.setValueDate(DateUtils.format(timestamp));
        String storeKey = getStoreKey(vo);
        redis.opsForValue().set(storeKey, vo);
    }

    /**
     * 获取存储到redis中的key
     * 如果设备id存在的话就将deviId拼接上
     *
     * @param vo 对象
     * @return 字符
     */
    @Override
    public String getStoreKey(CommonMeasurePointDO vo) {
        String keyPre = REDIS_POINT_REAL_TIME_PRE + vo.getPointId() + REDIS_KEY_UNDERLINE + vo.getSubSystemId();
        //如果有设备编号
        if (Objects.nonNull(vo.getDeviceId())) {
            return keyPre + REDIS_KEY_UNDERLINE + vo.getDeviceId();
        }
        return keyPre;
    }

    /**
     * 计算健康状态
     *
     * @param vo 对象
     * @return 布尔
     */
    private boolean calculateHealthStatus(MeasurePointVO vo) {
        if (Objects.isNull(vo.getCategory()) || Objects.isNull(vo.getValue())) {
            return false;
        }
        //报警测点
        if (PointCategoryEnum.ALARM.getValue().equals(vo.getCategory())) {
            //等于1看作为异常
            if (Objects.nonNull(vo.getValue()) && vo.getValue().intValue() == 1) {
                vo.setStatus(AlarmMessageEnum.ALARM_TEXT.getColor());
                vo.setAlarmLevel(AlarmMessageEnum.ALARM_TEXT.getLevel());
                vo.setStatusCause(AlarmMessageEnum.ALARM_TEXT.getText());
                return true;
            }
            return false;
        }
        //一般测点
        //高高报
        if (Objects.nonNull(vo.getThresholdHigher()) && Objects.nonNull(vo.getValue()) && vo.getValue() > vo.getThresholdHigher()) {
            vo.setStatus(AlarmMessageEnum.HIGH_HIGH_ALARM.getColor());
            vo.setAlarmLevel(AlarmMessageEnum.HIGH_HIGH_ALARM.getLevel());
            vo.setStatusCause(AlarmMessageEnum.HIGH_HIGH_ALARM.getText() + vo.getThresholdHigher());
            return true;
        }
        //低低报
        if (Objects.nonNull(vo.getThresholdLower()) && Objects.nonNull(vo.getValue()) && vo.getValue() < vo.getThresholdLower()) {
            vo.setStatus(AlarmMessageEnum.LOW_LOW_ALARM.getColor());
            vo.setAlarmLevel(AlarmMessageEnum.LOW_LOW_ALARM.getLevel());
            vo.setStatusCause(AlarmMessageEnum.LOW_LOW_ALARM.getText() + vo.getThresholdLower());
            return true;
        }
        //高报
        boolean isHighAlarm = Objects.nonNull(vo.getThresholdHigh()) && Objects.nonNull(vo.getValue()) && Objects.nonNull(vo.getThresholdHigher())
                && vo.getValue() > vo.getThresholdHigh() && vo.getValue() <= vo.getThresholdHigher();
        if (isHighAlarm) {
            vo.setStatus(AlarmMessageEnum.HIGH_ALARM.getColor());
            vo.setAlarmLevel(AlarmMessageEnum.HIGH_ALARM.getLevel());
            vo.setStatusCause(AlarmMessageEnum.HIGH_ALARM.getText() + vo.getThresholdHigh());
            return true;
        }
        //低报
        boolean isLowAlarm = Objects.nonNull(vo.getThresholdLower()) && Objects.nonNull(vo.getThresholdLow())
                && vo.getValue() >= vo.getThresholdLower() && vo.getValue() < vo.getThresholdLow();
        if (isLowAlarm) {
            vo.setStatus(AlarmMessageEnum.LOW_ALARM.getColor());
            vo.setAlarmLevel(AlarmMessageEnum.LOW_ALARM.getLevel());
            vo.setStatusCause(AlarmMessageEnum.LOW_ALARM.getText() + vo.getThresholdLow());
            return true;
        }
        //高预警
        if (Objects.nonNull(vo.getEarlyWarningHigh()) && Objects.nonNull(vo.getValue()) && vo.getValue() > vo.getEarlyWarningHigh()) {
            vo.setStatus(AlarmMessageEnum.HIGH_EARLY_ALARM.getColor());
            vo.setAlarmLevel(AlarmMessageEnum.HIGH_EARLY_ALARM.getLevel());
            vo.setStatusCause(AlarmMessageEnum.HIGH_EARLY_ALARM.getText() + vo.getEarlyWarningHigh());
            return true;
        }
        //低预警
        if (Objects.nonNull(vo.getEarlyWarningLow()) && Objects.nonNull(vo.getValue()) && vo.getValue() < vo.getEarlyWarningLow()) {
            vo.setStatus(AlarmMessageEnum.LOW_EARLY_ALARM.getColor());
            vo.setAlarmLevel(AlarmMessageEnum.LOW_EARLY_ALARM.getLevel());
            vo.setStatusCause(AlarmMessageEnum.LOW_EARLY_ALARM.getText() + vo.getEarlyWarningLow());
            return true;
        }
        return false;
    }
}
