package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.bo.*;
import com.aimsphm.nuclear.algorithm.entity.dto.StateMonitorParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.StateMonitorResponseDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmAsyncService;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.enums.AlarmEvaluationEnum;
import com.aimsphm.nuclear.common.enums.AlgorithmLevelEnum;
import com.aimsphm.nuclear.common.enums.EventStatusEnum;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.service.*;
import com.aimsphm.nuclear.common.util.BigDecimalUtils;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.*;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.COMMA;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.DASH;

/**
 * @Package: com.aimsphm.nuclear.algorithm.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/23 16:16
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/23 16:16
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service
public class AlgorithmServiceImpl implements AlgorithmService {
    @Resource
    private CommonDeviceService deviceService;
    @Resource
    private CommonMeasurePointService pointsService;
    @Resource
    private AlgorithmConfigService configService;
    @Resource
    private AlgorithmDeviceModelService deviceModelService;
    @Resource
    private AlgorithmModelPointService pointService;
    @Resource
    private AlgorithmModelService modelService;
    @Resource
    private JobDeviceStatusService statusService;
    @Resource
    private JobAlarmEventService eventService;
    @Resource
    private JobAlarmRealtimeService realtimeService;
    @Resource
    private AlgorithmAsyncService asyncService;
    @Resource(name = "HCM-PAF")
    private AlgorithmHandlerService handlerService;

    @Resource
    private HBaseUtil hBase;

    /**
     * 周期
     */
    private Integer period = 3600;
    /**
     * 15天毫秒值
     */
    private Integer days15 = 15 * 86400 * 1000;

    @Override
    public void getDeviceStateMonitorInfo() {
        LambdaUpdateWrapper<JobAlarmEventDO> update = Wrappers.lambdaUpdate(JobAlarmEventDO.class);
        update.set(JobAlarmEventDO::getAlarmStatus, EventStatusEnum.FINISHED.getValue()).ne(JobAlarmEventDO::getAlarmStatus, EventStatusEnum.FINISHED.getValue()).le(JobAlarmEventDO::getGmtLastAlarm, new Date(System.currentTimeMillis() - days15));
        eventService.update(update);
        List<StateMonitorParamDTO> data = operateParams(AlgorithmTypeEnum.STATE_MONITOR.getType());
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        long invokingTime = System.currentTimeMillis();
        data.stream().filter(Objects::nonNull).forEach(item -> {
            item.setAlgorithmPeriod(period);
            item.setInvokingTime(invokingTime);
            StateMonitorResponseDTO response = (StateMonitorResponseDTO) handlerService.getInvokeCustomerData(item);
            saveResult(response);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    void saveResult(StateMonitorResponseDTO response) {
        if (Objects.isNull(response)) {
            return;
        }
        Long deviceId = response.getDeviceId();
        Integer healthStatus = response.getHealthStatus();
        updateDeviceStatus(deviceId, healthStatus);
        saveRealtimeAlarm(response.getRealtimeAlarm());
        saveEstimateResult(response.getModelEstimateResult());
    }

    /**
     * 存储算法调用结果
     *
     * @param result
     */
    private void saveEstimateResult(List<EstimateResponseDataBO> result) {
        if (CollectionUtils.isEmpty(result)) {
            return;
        }
        result.stream().filter(Objects::nonNull).forEach(item -> {
            List<PointEstimateDataBO> dataList = item.getEstimateResults();
            if (CollectionUtils.isEmpty(dataList)) {
                return;
            }
            Long modelId = item.getModelId();
            dataList.stream().filter(Objects::nonNull).forEach(x -> {
                Long timestamp = x.getTimestamp();
                try {
                    hBase.insertObject(H_BASE_TABLE_NPC_PHM_DATA, modelId + ROW_KEY_SEPARATOR + timestamp, H_BASE_FAMILY_NPC_ESTIMATE, x.getPointId(), x, timestamp);
                } catch (IOException e) {
                    log.error("HBase insert failed...{}", e);
                }
            });
        });
    }

    /**
     * 保存报警事件
     *
     * @param realtimeList
     */
    private void saveRealtimeAlarm(List<JobAlarmRealtimeDO> realtimeList) {
        if (CollectionUtils.isEmpty(realtimeList)) {
            return;
        }
        realtimeService.saveBatch(realtimeList);
        Map<Long, List<JobAlarmRealtimeDO>> collect = realtimeList.stream().collect(Collectors.groupingBy(item -> item.getModelId()));
        collect.forEach((key, value) -> {
            saveOrUpdateEvent(key, value);
        });
    }

    private void saveOrUpdateEvent(Long modelId, List<JobAlarmRealtimeDO> value) {
        JobAlarmRealtimeDO firstAlarm = value.get(0);
        String pointIds = value.stream().map(x -> x.getPointId()).collect(Collectors.joining(COMMA));
        String codeList = value.stream().map(x -> x.getAlarmCode()).collect(Collectors.joining(COMMA));
        long highLevelCount = value.stream().filter(x -> AlarmEvaluationEnum.HIGHER.getValue().equals(x.getEvaluation()) || AlarmEvaluationEnum.LOWER.getValue().equals(x.getEvaluation())).count();
        long lowLevelCount = value.stream().filter(x -> AlarmEvaluationEnum.HIGH.getValue().equals(x.getEvaluation()) || AlarmEvaluationEnum.LOW.getValue().equals(x.getEvaluation())).count();
        LambdaQueryWrapper<JobAlarmEventDO> query = Wrappers.lambdaQuery(JobAlarmEventDO.class);
        query.eq(JobAlarmEventDO::getModelId, modelId).eq(JobAlarmEventDO::getPointIds, pointIds)
                .ge(JobAlarmEventDO::getGmtLastAlarm, new Date(System.currentTimeMillis() - days15)).lt(JobAlarmEventDO::getGmtLastAlarm, new Date())
                .last("limit 1");
        JobAlarmEventDO one = eventService.getOne(query);
//        活动中和已确认次数+1，已结束新增，其他数量不变
        JobAlarmEventDO event = new JobAlarmEventDO();
        BeanUtils.copyProperties(firstAlarm, event);
        if (Objects.isNull(one)) {
            AlgorithmModelDO model = modelService.getById(modelId);
            if (Objects.isNull(model)) {
                log.error("model is not exists ...modelId:{}", modelId);
                return;
            }
            CommonDeviceDO device = deviceService.getById(firstAlarm.getDeviceId());
            event.setDeviceCode(device.getDeviceCode());
            event.setSubSystemId(device.getSubSystemId());
            event.setDeviceName(device.getDeviceName());
            String modelName = model.getModelName();
            event.setEventName(modelName + "异常");
            event.setPointIds(pointIds);
            event.setAlarmCode(codeList);
            event.setGmtFirstAlarm(firstAlarm.getGmtAlarmTime());
            event.setGmtLastAlarm(firstAlarm.getGmtAlarmTime());
            initAlarmLevel(event, highLevelCount, lowLevelCount, 1);
            eventService.save(event);
            return;
        }
        //已忽略和已暂停不做任何操作
        if (Objects.isNull(one.getAlarmStatus()) || one.getAlarmStatus().equals(EventStatusEnum.PAUSED.getValue()) || one.getAlarmStatus().equals(EventStatusEnum.IGNORED.getValue())) {
            return;
        }
        Integer alarmCount = one.getAlarmCount();
        initAlarmLevel(one, highLevelCount, lowLevelCount, alarmCount + 1);
        Double frequency = BigDecimalUtils.format((alarmCount + 1) / ((firstAlarm.getGmtAlarmTime().getTime() - one.getGmtFirstAlarm().getTime()) / period + 1) * 100, 2);
//        报警次数/【（末报时间-首报时间）/算法调用周期+1】*100%
        one.setAlarmFrequency(frequency);
        one.setGmtLastAlarm(firstAlarm.getGmtAlarmTime());
        one.setAlarmCount(alarmCount + 1);
        eventService.updateById(one);
    }

    private void initAlarmLevel(JobAlarmEventDO event, long highLevelCount, long lowLevelCount, int count) {
//                                                      当报警次数≥100时                              当报警次数＜100时
//        事件所含测点最高报警等级为高高或低低时                    3级                                       2级
//        事件所含测点最高报警等级为高或低时                       2级                                       1级
        int comparisonConstantNumber = 100;
        boolean level2Flag = (highLevelCount > 0 && count < comparisonConstantNumber) || (lowLevelCount > 0 && count >= comparisonConstantNumber);
        if (highLevelCount > 0 && count >= comparisonConstantNumber) {
            event.setAlarmLevel(AlgorithmLevelEnum.LEVEL_THREE.getValue());
        } else if (level2Flag) {
            event.setAlarmLevel(AlgorithmLevelEnum.LEVEL_TWO.getValue());
        } else if (lowLevelCount > 0 && count < comparisonConstantNumber) {
            event.setAlarmLevel(AlgorithmLevelEnum.LEVEL_ONE.getValue());
        }
    }

    /**
     * 更新设备状态
     *
     * @param deviceId     设备id
     * @param healthStatus 健康状态
     */
    private void updateDeviceStatus(Long deviceId, Integer healthStatus) {
        CommonDeviceDO device = deviceService.getById(deviceId);
        if (Objects.isNull(device)) {
            return;
        }
        JobDeviceStatusDO status = statusService.getDeviceRunningStatus(deviceId);
        //保存一个新的状态
        if (Objects.isNull(status)) {
            JobDeviceStatusDO newOne = new JobDeviceStatusDO();
            newOne.setStatus(healthStatus);
            newOne.setDeviceId(deviceId);
            newOne.setGmtStart(new Date());
            statusService.save(newOne);
            return;

        }
        if (status.equals(healthStatus)) {
            return;
        }
        Date currentDate = new Date();
        status.setGmtEnd(currentDate);
        status.setStatusDuration(currentDate.getTime() - status.getGmtStart().getTime());
        //更新结束时间
        statusService.updateById(status);
        status.setGmtEnd(null);
        status.setGmtStart(currentDate);
        status.setStatus(healthStatus);
        status.setStatusDuration(0L);
        statusService.save(status);
    }

    /**
     * 获取状态监测算法、设备、模型、测点之间的数据
     *
     * @return
     */
    private List<StateMonitorParamDTO> operateParams(String type) {
        LambdaQueryWrapper<AlgorithmConfigDO> wrapper = Wrappers.lambdaQuery(AlgorithmConfigDO.class);
        wrapper.eq(AlgorithmConfigDO::getAlgorithmType, type);
        AlgorithmConfigDO algorithmConfig = configService.getOne(wrapper);
        if (Objects.isNull(algorithmConfig)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<AlgorithmDeviceModelDO> deviceModelWrapper = Wrappers.lambdaQuery(AlgorithmDeviceModelDO.class);
        deviceModelWrapper.eq(AlgorithmDeviceModelDO::getAlgorithmId, algorithmConfig.getId());
        List<AlgorithmDeviceModelDO> deviceModelList = deviceModelService.list(deviceModelWrapper);
        if (CollectionUtils.isEmpty(deviceModelList)) {
            return Lists.newArrayList();
        }
        //设备层级
        List<StateMonitorParamDTO> collect = deviceModelList.stream().map(x -> {
            CommonDeviceDO device = deviceService.getById(x.getDeviceId());
            if (Objects.isNull(device)) {
                return null;
            }
            StateMonitorParamDTO param = new StateMonitorParamDTO();
            param.setDeviceName(device.getDeviceName());
            param.setDeviceId(x.getDeviceId());
            Long id = x.getId();
            LambdaQueryWrapper<AlgorithmModelDO> modelWrapper = Wrappers.lambdaQuery(AlgorithmModelDO.class);
            modelWrapper.eq(AlgorithmModelDO::getDeviceModelId, id);
            List<AlgorithmModelDO> modelList = modelService.list(modelWrapper);
            if (CollectionUtils.isEmpty(modelList)) {
                return param;
            }
            List<AlgorithmModelPointDO> pointList = Lists.newArrayList();
            param.setModelEstimateResult(listEstimateData(modelList, pointList));
            param.setSensorData(listPointData(pointList));
            return param;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return collect;
    }

    private List<EstimateParamDataBO> listEstimateData(List<AlgorithmModelDO> modelList, List<AlgorithmModelPointDO> pointList) {
        return modelList.stream().map(m -> {
            EstimateParamDataBO bo = new EstimateParamDataBO();
            bo.setModelId(m.getId());
            bo.setModelName(m.getModelName());
            LambdaQueryWrapper<AlgorithmModelPointDO> modePointWrapper = Wrappers.lambdaQuery(AlgorithmModelPointDO.class);
            modePointWrapper.eq(AlgorithmModelPointDO::getModelId, m.getId());
            List<AlgorithmModelPointDO> pointDOS = pointService.list(modePointWrapper);
            if (CollectionUtils.isEmpty(pointDOS)) {
                return bo;
            }
            pointList.addAll(pointDOS);
            LambdaQueryWrapper<CommonMeasurePointDO> pointsWrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
            pointsWrapper.select(CommonMeasurePointDO::getPointId);
            pointsWrapper.in(CommonMeasurePointDO::getId, pointList.stream().map(item -> item.getPointId()).collect(Collectors.toList()));
            List<CommonMeasurePointDO> list = pointsService.list(pointsWrapper);
            bo.setEstimateTotal(listPointEstimateResultsData(m.getId(), list));
            return bo;
        }).collect(Collectors.toList());
    }

    private List<PointEstimateResultsDataBO> listPointEstimateResultsData(Long modelId, List<CommonMeasurePointDO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        PointEstimateResultsDataBO bo = new PointEstimateResultsDataBO();
        List<PointEstimateResultsDataBO> data = Lists.newArrayList(bo);
        List<String> ids = Lists.newArrayList(list.stream().filter(Objects::nonNull).map(x -> x.getPointId()).collect(Collectors.toSet()));
        try {
            List<PointEstimateDataBO> collect = hBase.selectModelDataList(H_BASE_TABLE_NPC_PHM_DATA, System.currentTimeMillis() - days15
                    , System.currentTimeMillis(), H_BASE_FAMILY_NPC_ESTIMATE, ids, modelId);
            bo.setEstimateResults(collect);
        } catch (IOException e) {
            log.error("select estimate data failed..{}", e);
        }
        return data;
    }

    private List<PointDataBO> listPointData(List<AlgorithmModelPointDO> pointList) {
        if (CollectionUtils.isEmpty(pointList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<CommonMeasurePointDO> pointsWrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        pointsWrapper.select(CommonMeasurePointDO::getId, CommonMeasurePointDO::getImportance, CommonMeasurePointDO::getPointId, CommonMeasurePointDO::getPointType,
                CommonMeasurePointDO::getSensorCode, CommonMeasurePointDO::getFeatureType, CommonMeasurePointDO::getFeature,
                CommonMeasurePointDO::getEarlyWarningLow, CommonMeasurePointDO::getThresholdLow, CommonMeasurePointDO::getThresholdLower,
                CommonMeasurePointDO::getEarlyWarningHigh, CommonMeasurePointDO::getThresholdHigh, CommonMeasurePointDO::getThresholdHigher);
        pointsWrapper.in(CommonMeasurePointDO::getId, pointList.stream().map(item -> item.getPointId()).collect(Collectors.toSet()));
        List<CommonMeasurePointDO> list = pointsService.list(pointsWrapper);
        CountDownLatch countDownLatch = new CountDownLatch(list.size());
        List<PointDataBO> collect = Lists.newArrayList();
        for (CommonMeasurePointDO item : list) {
            PointDataBO data = new PointDataBO();
            data.setImportance(item.getImportance());
            data.setPointId(item.getPointId());
            //低低报值，低报值，低预警值
            data.setThresholdLow(Lists.newArrayList(item.getThresholdLower(), item.getThresholdLow(), item.getEarlyWarningLow()));
            //高高报值，高报值，高预警值
            data.setThresholdHigh(Lists.newArrayList(item.getThresholdHigher(), item.getThresholdHigh(), item.getEarlyWarningHigh()));
            String family = item.getPointType() == 1 ? H_BASE_FAMILY_NPC_PI_REAL_TIME : item.getFeatureType() + DASH + item.getFeature();
            asyncService.listPointDataFromHBase(family, item.getId(), item.getSensorCode(), data, countDownLatch);
            collect.add(data);
            log.info("测试异步是否生成： " + System.currentTimeMillis());
        }
        try {
            countDownLatch.await();
            log.info("最后完成： " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return collect;
    }
}
