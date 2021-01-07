package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.AlgorithmModelPointDO;
import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.CommonSubSystemDO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.entity.vo.PointFeatureVO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.common.enums.AlarmMessageEnum;
import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import com.aimsphm.nuclear.common.enums.PointFeatureEnum;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.CommonMeasurePointMapper;
import com.aimsphm.nuclear.common.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.*;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.DASH;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.STAR;

/**
 * @Package: com.aimsphm.nuclear.ext.service.impl
 * @Description: <测点信息扩展服务实现类>
 * @Author: milla
 * @CreateDate: 2020-11-16
 * @UpdateUser: milla
 * @UpdateDate: 2020-11-16
 * @UpdateRemark: <>
 * @Version: 1.0
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
    private CommonSubSystemService subSystemServiceExt;
    @Resource
    private JobAlarmThresholdService thresholdService;
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
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
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
        List<MeasurePointVO> vos = this.getMeasurePointsByTagId(itemId);
        if (CollectionUtils.isEmpty(vos)) {
            return;
        }
        vos.stream().forEach(item -> store2Redis(item, value));
        //缓存指定长度的队列
        cacheQueueData(vos, timestamp);
        //        TODO :// 上线要将下列代码恢复
//        thresholdService.saveOrUpdateThresholdAlarmList(vos);

    }

    /**
     * 缓存队列数据
     *
     * @param vos
     * @param timestamp
     */
    private void cacheQueueData(List<MeasurePointVO> vos, Long timestamp) {
        for (MeasurePointVO point : vos) {
            Long id = point.getId();
            LambdaQueryWrapper<AlgorithmModelPointDO> query = Wrappers.lambdaQuery(AlgorithmModelPointDO.class);
            query.eq(AlgorithmModelPointDO::getPointId, point.getId());
            int count = algorithmModelPointService.count(query);
            if (count == 0) {
                continue;
            }
            String key = REDIS_QUEUE_REAL_TIME_PRE + id;
            Long size = redis.opsForList().size(key);
            HBaseTimeSeriesDataDTO data = new HBaseTimeSeriesDataDTO();
            data.setValue(point.getValue());
            data.setTimestamp(timestamp);
            if (size.intValue() == CACHE_QUEUE_DATA_SIZE) {
                redis.opsForList().rightPush(key, data);
                redis.opsForList().leftPop(key);
                continue;
            }
            redis.opsForList().rightPush(key, data);
        }
    }

    @Override
    @Cacheable(value = REDIS_POINT_INFO_LIST, key = "#itemId")
    public List<MeasurePointVO> getMeasurePointsByTagId(String itemId) {
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

    @Override
    public PointFeatureVO listFeatures(String sensorCode) {
        PointFeatureVO vo = new PointFeatureVO();
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonMeasurePointDO::getSensorCode, sensorCode).isNotNull(CommonMeasurePointDO::getFeatureType).isNotNull(CommonMeasurePointDO::getFeature);
        List<CommonMeasurePointDO> pointDOList = this.list(wrapper);
        if (CollectionUtils.isEmpty(pointDOList)) {
            return vo;
        }
        Map<String, List<CommonMeasurePointDO>> collect = pointDOList.stream().collect(Collectors.groupingBy(CommonMeasurePointDO::getFeatureType));
        List<TreeVO> list = collect.entrySet().stream().map(item -> {
            List<CommonMeasurePointDO> features = item.getValue();
            TreeVO treeVO = new TreeVO(item.getKey(), PointFeatureEnum.getLabel(item.getKey()));
            List<TreeVO> children = features.stream().map(o -> new TreeVO(o.getFeature(), o.getFeatureName())).collect(Collectors.toList());
            treeVO.setChildren(children);
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
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = initWrapper(query);
        if (Objects.nonNull(query.getVisible())) {
            wrapper.last("and visible%" + query.getVisible() + "=0");
        }
        return this.list(wrapper);
    }

    /**
     * 组装查询条件
     * 目前能支持到系统下公共测点
     *
     * @param query 查询条件
     * @return
     */
    private LambdaQueryWrapper<CommonMeasurePointDO> initWrapper(CommonQueryBO query) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        if (Objects.isNull(query.getSystemId()) && Objects.isNull(query.getSubSystemId()) && Objects.isNull(query.getDeviceId()) && Objects.isNull(query.getVisible())) {
            throw new CustomMessageException("参数不全");
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
    public void store2Redis(MeasurePointVO vo, Double value) {
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
        String storeKey = getStoreKey(vo);
        redis.opsForValue().set(storeKey, vo);
    }

    /**
     * 获取存储到redis中的key
     * 如果设备id存在的话就将deviId拼接上
     *
     * @param vo
     * @return
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
     * @param vo
     * @return
     */
    private boolean calculateHealthStatus(MeasurePointVO vo) {
        if (Objects.isNull(vo.getCategory()) || Objects.isNull(vo.getValue())) {
            return false;
        }
        //报警测点
        if (PointCategoryEnum.ALARM.getValue().equals(vo.getCategory().byteValue())) {
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
