package com.aimsphm.nuclear.data.service.impl;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.enums.AlarmMessageEnum;
import com.aimsphm.nuclear.common.enums.SensorTypeEnum;
import com.aimsphm.nuclear.data.service.HotSpotDataUpdateService;
import com.aimsphm.nuclear.data.service.MeasurePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.aimsphm.nuclear.common.constant.CommonConstant.REDIS_KEY_UNDERLINE;

/**
 * @Package: com.aimsphm.nuclear.data.service.impl
 * @Description: <热点数据更新操作>
 * @Author: MILLA
 * @CreateDate: 2020/4/2 11:42
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/2 11:42
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class HotSpotDataUpdateServiceImpl implements HotSpotDataUpdateService {
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redis;

    @Autowired
    private MeasurePointService service;

    @Async
    @Override
    public void updatePIMeasurePoints(String itemId, Double value) {
        if (StringUtils.isEmpty(itemId)) {
            return;
        }
        List<MeasurePointVO> vos = service.getMeasurePointsByTagId(itemId);
        if (CollectionUtils.isEmpty(vos)) {
            return;
        }
        vos.stream().forEach(item -> store2Redis(item, value));
    }

    @Async
    @Override
    public void updateNonePIMeasurePoints(String itemId, Double value, Double temp1, Double temp2) {
        if (StringUtils.isEmpty(itemId)) {
            return;
        }
        List<MeasurePointVO> vos = service.getNonPiMeasurePointsByTagId(itemId);
        if (CollectionUtils.isEmpty(vos)) {
            return;
        }
        vos.stream().forEach(item -> store2Redis(item, value, temp1, temp2));
    }

    private void store2Redis(MeasurePointVO vo, Double value, Double temp1, Double temp2) {
        vo.setValue(value);
        //计算健康状况
        boolean isSave = calculateHealthStatus(vo);
        if (StringUtils.hasText(vo.getStatusCause())) {
            if (SensorTypeEnum.ALARM.getValue() != vo.getSensorType()) {
                //设置单位
                vo.setStatusCause(vo.getStatusCause() + vo.getUnit());
            }
        }
        vo.setTemp1(temp1);
        vo.setTemp2(temp2);
        String storeKey = getStoreKey(vo);
        redis.opsForValue().set(storeKey, vo);

        String suffix = StringUtils.hasText(vo.getParentTag()) ? vo.getParentTag() : "common";
        if (isSave) {
            redis.opsForSet().add(CommonConstant.REDIS_POINT_REAL_TIME_WARNING_LIST_PRE + suffix, vo.getTag());
        } else {
            redis.opsForSet().remove(CommonConstant.REDIS_POINT_REAL_TIME_WARNING_LIST_PRE + suffix, vo.getTag());
        }
    }

    private void store2Redis(MeasurePointVO vo, Double value) {
        store2Redis(vo, value, null, null);
    }

    /**
     * 获取存储到redis中的key
     * 如果设备id存在的话就将deviId拼接上
     *
     * @param vo
     * @return
     */
    private String getStoreKey(MeasurePointVO vo) {
        String keyPre = CommonConstant.REDIS_POINT_REAL_TIME_PRE + vo.getTag() + REDIS_KEY_UNDERLINE + vo.getSubSystemId();
        //如果有设备编号
        if (StringUtils.hasText(vo.getParentTag())) {
            return keyPre + REDIS_KEY_UNDERLINE + vo.getParentTag();
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
        //报警测点
        if (SensorTypeEnum.ALARM.getValue().equals(vo.getSensorType())) {
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
        if (Objects.nonNull(vo.getThresholdHighHigh()) && Objects.nonNull(vo.getValue()) && vo.getValue() > vo.getThresholdHighHigh()) {
            vo.setStatus(AlarmMessageEnum.HIGH_HIGH_ALARM.getColor());
            vo.setStatusCause(AlarmMessageEnum.HIGH_HIGH_ALARM.getText() + vo.getThresholdHighHigh());
            return true;
        }
        //低低报
        if (Objects.nonNull(vo.getThresholdLowLow()) && Objects.nonNull(vo.getValue()) && vo.getValue() < vo.getThresholdLowLow()) {
            vo.setStatus(AlarmMessageEnum.LOW_LOW_ALARM.getColor());
            vo.setStatusCause(AlarmMessageEnum.LOW_LOW_ALARM.getText() + vo.getThresholdLowLow());
            return true;
        }
        //高报
        boolean isHighAlarm = Objects.nonNull(vo.getThresholdHigh()) && Objects.nonNull(vo.getValue()) && Objects.nonNull(vo.getThresholdHighHigh())
                && vo.getValue() > vo.getThresholdHigh() && vo.getValue() <= vo.getThresholdHighHigh();
        if (isHighAlarm) {
            vo.setStatus(AlarmMessageEnum.HIGH_ALARM.getColor());
            vo.setStatusCause(AlarmMessageEnum.HIGH_ALARM.getText() + vo.getThresholdHigh());
            return true;
        }
        //低报
        boolean isLowAlarm = Objects.nonNull(vo.getThresholdLowLow()) && Objects.nonNull(vo.getThresholdLow())
                && vo.getValue() >= vo.getThresholdLowLow() && vo.getValue() < vo.getThresholdLow();
        if (isLowAlarm) {
            vo.setStatus(AlarmMessageEnum.LOW_ALARM.getColor());
            vo.setStatusCause(AlarmMessageEnum.LOW_ALARM.getText() + vo.getThresholdLow());
            return true;
        }
        //高预警
        if (Objects.nonNull(vo.getAlarmThresholdHigh()) && Objects.nonNull(vo.getValue()) && vo.getValue() > vo.getAlarmThresholdHigh()) {
            vo.setStatus(AlarmMessageEnum.HIGH_EARLY_ALARM.getColor());
            vo.setStatusCause(AlarmMessageEnum.HIGH_EARLY_ALARM.getText() + vo.getAlarmThresholdHigh());
            return true;
        }
        //低预警
        if (Objects.nonNull(vo.getAlarmThresholdLow()) && Objects.nonNull(vo.getValue()) && vo.getValue() < vo.getAlarmThresholdLow()) {
            vo.setStatus(AlarmMessageEnum.LOW_EARLY_ALARM.getColor());
            vo.setStatusCause(AlarmMessageEnum.LOW_EARLY_ALARM.getText() + vo.getAlarmThresholdLow());
            return true;
        }
        return false;
    }
}
