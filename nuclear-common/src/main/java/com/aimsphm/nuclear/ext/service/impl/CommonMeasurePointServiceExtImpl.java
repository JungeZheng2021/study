package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.entity.vo.PointFeatureVO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.common.enums.AlarmMessageEnum;
import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import com.aimsphm.nuclear.common.enums.PointFeatureEnum;
import com.aimsphm.nuclear.common.service.impl.CommonMeasurePointServiceImpl;
import com.aimsphm.nuclear.ext.service.CommonMeasurePointServiceExt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.*;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.DASH;

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
public class CommonMeasurePointServiceExtImpl extends CommonMeasurePointServiceImpl implements CommonMeasurePointServiceExt {
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redis;

    @Async
    @Override
    public void updateMeasurePointsInRedis(String itemId, Double value) {
        if (StringUtils.isEmpty(itemId)) {
            return;
        }
        List<MeasurePointVO> vos = this.getMeasurePointsByTagId(itemId);
        if (CollectionUtils.isEmpty(vos)) {
            return;
        }
        vos.stream().forEach(item -> store2Redis(item, value));
    }

    @Override
    @Cacheable(value = REDIS_POINT_INFO_LIST, key = "#itemId")
    public List<MeasurePointVO> getMeasurePointsByTagId(String itemId) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonMeasurePointDO::getTagId, itemId);
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
        wrapper.ne(CommonMeasurePointDO::getTagType, 1);
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

    private void store2Redis(MeasurePointVO vo, Double value) {
        vo.setValue(value);
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
        String keyPre = REDIS_POINT_REAL_TIME_PRE + vo.getTagId() + REDIS_KEY_UNDERLINE + vo.getSubSystemId();
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
                vo.setStatusCause(AlarmMessageEnum.ALARM_TEXT.getText());
                return true;
            }
            return false;
        }
        //一般测点
        //高高报
        if (Objects.nonNull(vo.getThresholdHigher()) && Objects.nonNull(vo.getValue()) && vo.getValue() > vo.getThresholdHigher()) {
            vo.setStatus(AlarmMessageEnum.HIGH_HIGH_ALARM.getColor());
            vo.setStatusCause(AlarmMessageEnum.HIGH_HIGH_ALARM.getText() + vo.getThresholdHigher());
            return true;
        }
        //低低报
        if (Objects.nonNull(vo.getThresholdLower()) && Objects.nonNull(vo.getValue()) && vo.getValue() < vo.getThresholdLower()) {
            vo.setStatus(AlarmMessageEnum.LOW_LOW_ALARM.getColor());
            vo.setStatusCause(AlarmMessageEnum.LOW_LOW_ALARM.getText() + vo.getThresholdLower());
            return true;
        }
        //高报
        boolean isHighAlarm = Objects.nonNull(vo.getThresholdHigh()) && Objects.nonNull(vo.getValue()) && Objects.nonNull(vo.getThresholdHigher())
                && vo.getValue() > vo.getThresholdHigh() && vo.getValue() <= vo.getThresholdHigher();
        if (isHighAlarm) {
            vo.setStatus(AlarmMessageEnum.HIGH_ALARM.getColor());
            vo.setStatusCause(AlarmMessageEnum.HIGH_ALARM.getText() + vo.getThresholdHigh());
            return true;
        }
        //低报
        boolean isLowAlarm = Objects.nonNull(vo.getThresholdLower()) && Objects.nonNull(vo.getThresholdLow())
                && vo.getValue() >= vo.getThresholdLower() && vo.getValue() < vo.getThresholdLow();
        if (isLowAlarm) {
            vo.setStatus(AlarmMessageEnum.LOW_ALARM.getColor());
            vo.setStatusCause(AlarmMessageEnum.LOW_ALARM.getText() + vo.getThresholdLow());
            return true;
        }
        //高预警
        if (Objects.nonNull(vo.getEarlyWarningHigh()) && Objects.nonNull(vo.getValue()) && vo.getValue() > vo.getEarlyWarningHigh()) {
            vo.setStatus(AlarmMessageEnum.HIGH_EARLY_ALARM.getColor());
            vo.setStatusCause(AlarmMessageEnum.HIGH_EARLY_ALARM.getText() + vo.getEarlyWarningHigh());
            return true;
        }
        //低预警
        if (Objects.nonNull(vo.getEarlyWarningLow()) && Objects.nonNull(vo.getValue()) && vo.getValue() < vo.getEarlyWarningLow()) {
            vo.setStatus(AlarmMessageEnum.LOW_EARLY_ALARM.getColor());
            vo.setStatusCause(AlarmMessageEnum.LOW_EARLY_ALARM.getText() + vo.getEarlyWarningLow());
            return true;
        }
        return false;
    }
}
