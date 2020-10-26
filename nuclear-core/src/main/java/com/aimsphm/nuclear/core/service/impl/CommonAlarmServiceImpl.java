package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.entity.MdVibrationFeature;
import com.aimsphm.nuclear.common.entity.TxAlarmEvent;
import com.aimsphm.nuclear.common.entity.TxAlarmRealtime;
import com.aimsphm.nuclear.common.entity.bo.AlarmEventQueryPageBO;
import com.aimsphm.nuclear.common.entity.bo.AlarmRealtimeQueryPageBO;
import com.aimsphm.nuclear.common.enums.AlarmEvaluationEnum;
import com.aimsphm.nuclear.common.service.MdDeviceService;
import com.aimsphm.nuclear.common.service.MdSensorService;
import com.aimsphm.nuclear.common.service.TxAlarmEventService;
import com.aimsphm.nuclear.common.service.TxAlarmRealtimeService;
import com.aimsphm.nuclear.core.service.CommonAlarmService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class CommonAlarmServiceImpl implements CommonAlarmService {

    @Autowired
    TxAlarmEventService txAlarmEventService;
    @Autowired
    TxAlarmRealtimeService txAlarmRealtimeService;
    @Autowired
    MdDeviceService mdDeviceService;
    @Autowired
    MdSensorService mdSensorService;

    @Override
    public Page<TxAlarmEvent> searchTxAlarmEvents(AlarmEventQueryPageBO alarmEventQueryPageBO) {
        Page<TxAlarmEvent> page = new Page<>(alarmEventQueryPageBO.getCurrent(), alarmEventQueryPageBO.getSize(), 5000, true);
        QueryWrapper<TxAlarmEvent> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TxAlarmEvent> lw = queryWrapper.lambda();
        if (!ObjectUtils.isEmpty(alarmEventQueryPageBO.getSubSystemId())) {
            lw.eq(TxAlarmEvent::getSubSystemId, alarmEventQueryPageBO.getSubSystemId());
        }
        if (!ObjectUtils.isEmpty(alarmEventQueryPageBO.getTagId())) {
            lw.and(Wrapper -> Wrapper.like(TxAlarmEvent::getSensorTagids, alarmEventQueryPageBO.getTagId()+",").or().like(TxAlarmEvent::getSensorTagids, ","+alarmEventQueryPageBO.getTagId()).or().eq(TxAlarmEvent::getSensorTagids,alarmEventQueryPageBO.getTagId()));
        }
        if (!ObjectUtils.isEmpty(alarmEventQueryPageBO.getAlarmType())) {
            lw.eq(TxAlarmEvent::getAlarmType, alarmEventQueryPageBO.getAlarmType());
        }
        if (!ObjectUtils.isEmpty(alarmEventQueryPageBO.getDeviceId())) {
            lw.eq(TxAlarmEvent::getDeviceId, alarmEventQueryPageBO.getDeviceId());
        }
        if (!ObjectUtils.isEmpty(alarmEventQueryPageBO.getReason())) {
            String reasons[] = alarmEventQueryPageBO.getReason().split(",");
            lw.in(TxAlarmEvent::getAlarmReason, reasons);
        }

        if (!ObjectUtils.isEmpty(alarmEventQueryPageBO.getAlarmStatus())) {
            lw.eq(TxAlarmEvent::getAlarmStatus, alarmEventQueryPageBO.getAlarmStatus());
        }
        if (!ObjectUtils.isEmpty(alarmEventQueryPageBO.getActive())) {
            if (alarmEventQueryPageBO.getActive() == 0) {
                lw.eq(TxAlarmEvent::getStopFlag, true);
            } else if (alarmEventQueryPageBO.getActive() == 1) {
                lw.eq(TxAlarmEvent::getStopFlag, false);
            }
        }
        if (!ObjectUtils.isEmpty(alarmEventQueryPageBO.getAlarmLevel())) {
            String[] levels = alarmEventQueryPageBO.getAlarmLevel().split(",");
            lw.in(TxAlarmEvent::getAlarmLevel, levels);
        }


        if (!ObjectUtils.isEmpty(alarmEventQueryPageBO.getRemark())) {
            if (alarmEventQueryPageBO.getRemark() == 0) {
                lw.isNull(TxAlarmEvent::getRemark);
            } else if (alarmEventQueryPageBO.getRemark() == 1) {
                lw.isNotNull(TxAlarmEvent::getRemark);
            }
        }
        if (!ObjectUtils.isEmpty(alarmEventQueryPageBO.getAlarmContent())) {
            lw.and(Wrapper -> Wrapper.like(TxAlarmEvent::getAlarmContent, alarmEventQueryPageBO.getAlarmContent()).or().like(TxAlarmEvent::getRemark, alarmEventQueryPageBO.getAlarmContent()));
        }

        if (!ObjectUtils.isEmpty(alarmEventQueryPageBO.getAlarmStartTime()) && !ObjectUtils.isEmpty(alarmEventQueryPageBO.getAlarmEndTime())) {
            lw.and(WrapperOut -> WrapperOut.between(TxAlarmEvent::getLastAlarm, alarmEventQueryPageBO.getAlarmStartTime(), alarmEventQueryPageBO.getAlarmEndTime())
                    .or().between(TxAlarmEvent::getFirstAlarm, alarmEventQueryPageBO.getAlarmStartTime(), alarmEventQueryPageBO.getAlarmEndTime())
                    .or(Wrapper -> Wrapper.ge(TxAlarmEvent::getLastAlarm, alarmEventQueryPageBO.getAlarmEndTime()).le(TxAlarmEvent::getFirstAlarm, alarmEventQueryPageBO.getAlarmEndTime())));

        }
        if (ObjectUtils.isEmpty(alarmEventQueryPageBO.getOrder()) || ObjectUtils.isEmpty(alarmEventQueryPageBO.getOrderColumn())) {
            lw.orderByDesc(TxAlarmEvent::getLastAlarm);
            lw.orderByDesc(TxAlarmEvent::getAlarmLevel);
            lw.orderByDesc(TxAlarmEvent::getAlarmCount);
        } else {
            Boolean asc = true;
            if (alarmEventQueryPageBO.getOrder() == 1) {
                asc = false;
            }
            if (alarmEventQueryPageBO.getOrderColumn() == 0) {
                lw.orderBy(true, asc, TxAlarmEvent::getLastAlarm);
            } else if (alarmEventQueryPageBO.getOrderColumn() == 1) {
                lw.orderBy(true, asc, TxAlarmEvent::getAlarmCount);
            } else if (alarmEventQueryPageBO.getOrderColumn() == 2) {
                lw.orderBy(true, asc, TxAlarmEvent::getAlarmLevel);
            }
        }
        Page<TxAlarmEvent> pg = txAlarmEventService.page(page, lw);
        pg.getRecords().stream().forEach(te -> {
            String tags = te.getSensorTagids();
            String[] tagsA = tags.split(",");
            List<String> displaySensors = Lists.newArrayList();
            for(String tag:tagsA) {
                Boolean isNonePi = mdSensorService.isNonePiViSensor(tag);

                if (isNonePi) {
                    String[] baseTagIdFeature = mdSensorService.getBaseTagAndFeatureForViSensor(tag);
                    MdSensor mdSensor = mdSensorService.getMdSensorsByTagIds(baseTagIdFeature[0]);
                    if (mdSensor != null && !ObjectUtils.isEmpty(mdSensor.getAlias()) && baseTagIdFeature[1] != null) {
                        String displaySensorTag = mdSensor.getAlias() + "_" + baseTagIdFeature[1];
                        displaySensors.add(displaySensorTag);
                    }
                }else{
                    displaySensors.add(tag);
                }
            }
            te.setDisplaySensorTagids(String.join(",", displaySensors));
        });
        return pg;
    }

    @Override
    public Page<TxAlarmRealtime> searchTxAlarmRealTime(AlarmRealtimeQueryPageBO alarmRealtimeQueryPageBO) {
        Page<TxAlarmRealtime> page = new Page<>(alarmRealtimeQueryPageBO.getCurrent(), alarmRealtimeQueryPageBO.getSize(), 5000, true);
        QueryWrapper<TxAlarmRealtime> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<TxAlarmRealtime> lw = queryWrapper.lambda();
        if (!ObjectUtils.isEmpty(alarmRealtimeQueryPageBO.getSubSystemId())) {
            lw.eq(TxAlarmRealtime::getSubSystemId, alarmRealtimeQueryPageBO.getSubSystemId());
        }
        if (!ObjectUtils.isEmpty(alarmRealtimeQueryPageBO.getAlarmType())) {
            lw.eq(TxAlarmRealtime::getAlarmType, alarmRealtimeQueryPageBO.getAlarmType());
        }
        if (!ObjectUtils.isEmpty(alarmRealtimeQueryPageBO.getDeviceId())) {
            lw.eq(TxAlarmRealtime::getDeviceId, alarmRealtimeQueryPageBO.getDeviceId());
        }
        if (!ObjectUtils.isEmpty(alarmRealtimeQueryPageBO.getEvaluation())) {
            lw.eq(TxAlarmRealtime::getEvaluation, alarmRealtimeQueryPageBO.getEvaluation());
        }
        if (!ObjectUtils.isEmpty(alarmRealtimeQueryPageBO.getTagId())) {
            lw.eq(TxAlarmRealtime::getSensorTagid, alarmRealtimeQueryPageBO.getTagId());
        }
        if (!ObjectUtils.isEmpty(alarmRealtimeQueryPageBO.getAlarmStartTime()) && !ObjectUtils.isEmpty(alarmRealtimeQueryPageBO.getAlarmEndTime())) {
            lw.ge(TxAlarmRealtime::getAlarmTime, alarmRealtimeQueryPageBO.getAlarmStartTime());
            lw.le(TxAlarmRealtime::getAlarmTime, alarmRealtimeQueryPageBO.getAlarmEndTime());
        }
        if (ObjectUtils.isEmpty(alarmRealtimeQueryPageBO.getOrder()) || ObjectUtils.isEmpty(alarmRealtimeQueryPageBO.getOrderColumn())) {
            lw.orderByDesc(TxAlarmRealtime::getAlarmTime);
        } else {
            Boolean asc = true;
            if (alarmRealtimeQueryPageBO.getOrder() == 1) {
                asc = false;
            }
            if (alarmRealtimeQueryPageBO.getOrderColumn() == 0) {
                lw.orderBy(true, asc, TxAlarmRealtime::getAlarmTime);
            }
        }
        Page<TxAlarmRealtime> pg = txAlarmRealtimeService.page(page, lw);

        pg.getRecords().stream().forEach(rt -> {
            Boolean isNonePi = mdSensorService.isNonePiViSensor(rt.getSensorTagid());
            rt.setDeviceName(mdDeviceService.getOnlyNameById(rt.getDeviceId()));

            String tagId = rt.getSensorTagid();
            rt.setDisplaySensor(tagId);
            if(isNonePi)
            {
              String[] baseTagIdFeature = mdSensorService.getBaseTagAndFeatureForViSensor(tagId);
              MdSensor mdSensor = mdSensorService.getMdSensorsByTagIds(baseTagIdFeature[0]);
              String featureName = "";
              if(mdSensor!=null && !ObjectUtils.isEmpty(mdSensor.getAlias())&& baseTagIdFeature[1]!=null)
              {
                MdVibrationFeature mdVibrationFeature = mdSensorService.getViFeatureMdSensor(baseTagIdFeature[0]).stream().filter(vi->vi.getVifPostfix().equals(baseTagIdFeature[1])).findAny().orElse(null);
                  rt.setDisplaySensor(mdSensor.getAlias()+"_"+baseTagIdFeature[1]);
                  featureName = mdVibrationFeature.getVifName();
              }
                rt.setSensorTagName(mdSensor.getSensorName()+":"+featureName);
            }else{
                rt.setSensorTagName(mdSensorService.getMdSensorsByTagIds(rt.getSensorTagid()).getSensorName());
            }
            rt.setEvaluationContent(AlarmEvaluationEnum.getByValue(rt.getEvaluation()).getDesc());
        });
        return pg;
    }
}
