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
import com.aimsphm.nuclear.common.enums.DeviceHealthEnum;
import com.aimsphm.nuclear.common.enums.EventStatusEnum;
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
    private AlgorithmModelPointService pointService;
    @Resource
    private AlgorithmModelService modelService;
    @Resource
    private JobDeviceStatusService statusService;
    @Resource
    private CommonDeviceDetailsService detailsService;
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
        List<StateMonitorParamDTO> data = operateParams(AlgorithmTypeEnum.STATE_MONITOR.getType(), 1);
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        data.stream().filter(Objects::nonNull).forEach(item -> {
            item.setAlgorithmPeriod(period);
            item.setInvokingTime(System.currentTimeMillis());
            StateMonitorResponseDTO response = (StateMonitorResponseDTO) handlerService.getInvokeCustomerData(item);
            saveResult(response);
        });
    }

    @Override
    public void getDeviceStartAndStopMonitorInfo() {
        List<StateMonitorParamDTO> data = operateParams(AlgorithmTypeEnum.STATE_MONITOR.getType(), 0);
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        data.stream().filter(Objects::nonNull).forEach(item -> {
            item.setAlgorithmPeriod(period);
            item.setInvokingTime(System.currentTimeMillis());
            StateMonitorResponseDTO response = (StateMonitorResponseDTO) handlerService.getInvokeCustomerData(item);
            if (Objects.isNull(response)) {
                return;
            }
            updateDeviceStatus(response.getDeviceId(), response.getHealthStatus());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    void saveResult(StateMonitorResponseDTO response) {
        if (Objects.isNull(response)) {
            return;
        }
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
//        List<String> pointIdList = value.stream().map(x -> x.getPointId()).collect(Collectors.toList());
        //生成故障推理 - 暂时不使用自动推理
//        asyncService.faultDiagnosis(pointIdList);
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
     * 更新设备状态启停状态
     *
     * @param deviceId  设备id
     * @param stopStart 健康状态
     */
    private void updateDeviceStatus(Long deviceId, Integer stopStart) {
        CommonDeviceDO device = deviceService.getById(deviceId);
        if (Objects.isNull(device)) {
            return;
        }
        JobDeviceStatusDO status = statusService.getDeviceRunningStatus(deviceId);
        //之前没有状态，且现在是停机需要保存
        if (Objects.isNull(status)) {
            Integer newStatus = statusService.getDeviceCurrentStatus(deviceId, DeviceHealthEnum.STOP.getValue().equals(stopStart));
            JobDeviceStatusDO newOne = new JobDeviceStatusDO();
            newOne.setStatus(newStatus);
            newOne.setDeviceId(deviceId);
            newOne.setGmtStart(new Date());
            statusService.save(newOne);
            return;
        }
        // 停机状态1.本次和上次都是停机状态
        if (status.getStatus().equals(stopStart) && DeviceHealthEnum.STOP.getValue().equals(stopStart)) {
            return;
        }
        // 停机状态2.直接修改上次状态持续时间，记录本次停机开始时间
        if (DeviceHealthEnum.STOP.getValue().equals(stopStart)) {
            statusService.saveOrUpdateDeviceStatus(status, stopStart);
            return;
        }
        //以下是非停机状态
        //上一次是停机，本次是启动需要改动 最近启动配置
        if (DeviceHealthEnum.STOP.getValue().equals(status.getStatus()) && !DeviceHealthEnum.STOP.getValue().equals(stopStart)) {
            detailsService.updateLastStartTime(deviceId);
        }
        //启动报警状态且启停状态是启动状态，需要计算设备状态
        statusService.updateDeviceStatusWithCalculate(status, device.getEnableMonitor());
    }

    /**
     * 获取状态监测算法、设备、模型、测点之间的数据
     *
     * @return
     */
    private List<StateMonitorParamDTO> operateParams(String type, Integer modelType) {
        LambdaQueryWrapper<AlgorithmConfigDO> wrapper = Wrappers.lambdaQuery(AlgorithmConfigDO.class);
        wrapper.eq(AlgorithmConfigDO::getAlgorithmType, type);
        AlgorithmConfigDO algorithmConfig = configService.getOne(wrapper);
        if (Objects.isNull(algorithmConfig)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<AlgorithmModelDO> modelWrapper = Wrappers.lambdaQuery(AlgorithmModelDO.class);
        modelWrapper.eq(AlgorithmModelDO::getAlgorithmId, algorithmConfig.getId());
//        if (Objects.nonNull(modelType)) {
//            modelWrapper.eq(AlgorithmModelDO::getModelType, modelType);
//        }
        List<AlgorithmModelDO> modelList = modelService.list(modelWrapper);
        if (CollectionUtils.isEmpty(modelList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<AlgorithmModelPointDO> modePointWrapper = Wrappers.lambdaQuery(AlgorithmModelPointDO.class);
        modePointWrapper.in(AlgorithmModelPointDO::getModelId, modelList.stream().map(x -> x.getId()).collect(Collectors.toSet()));
        List<AlgorithmModelPointDO> pointDOS = pointService.list(modePointWrapper);
        if (CollectionUtils.isEmpty(pointDOS)) {
            return Lists.newArrayList();
        }
        Map<Long, List<AlgorithmModelPointDO>> pointMap = pointDOS.stream().collect(Collectors.groupingBy(x -> x.getDeviceId()));
        Map<Long, List<AlgorithmModelDO>> deviceModelList = modelList.stream().collect(Collectors.groupingBy(x -> x.getDeviceId()));
        List<StateMonitorParamDTO> collect = deviceModelList.entrySet().stream().map(x -> {
            Long deviceId = x.getKey();
            List<AlgorithmModelDO> value = x.getValue();
            CommonDeviceDO device = deviceService.getById(deviceId);
            //设备不存在或者是 设备未开启报警且modelType不是启停模型 1：开启 0:未开启
            boolean isNeed = Objects.isNull(device) || (!device.getEnableMonitor() && modelType != 0);
            if (isNeed) {
                return null;
            }
            StateMonitorParamDTO param = new StateMonitorParamDTO();
            param.setDeviceName(device.getDeviceName());
            param.setDeviceId(deviceId);
            param.setModelIds(value.stream().map(v -> v.getId()).distinct().collect(Collectors.toList()));
            param.setOnlyCondition(1);
            //启停算法不需要残值值
            if (modelType != 0) {
                param.setOnlyCondition(0);
                param.setModelEstimateResult(listEstimateData(value, pointMap));
            }
            param.setSensorData(listPointData(pointMap.get(deviceId)));
            //两个参数都是空的话不调用算法
            if (CollectionUtils.isEmpty(param.getSensorData()) && CollectionUtils.isEmpty(param.getModelEstimateResult())) {
                return null;
            }
            return param;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return collect;
    }

    private List<EstimateParamDataBO> listEstimateData(List<AlgorithmModelDO> modelList, Map<Long, List<AlgorithmModelPointDO>> pointMap) {
        return modelList.stream().map(m -> {
            EstimateParamDataBO bo = new EstimateParamDataBO();
            bo.setModelId(m.getId());
            bo.setModelName(m.getModelName());
            List<AlgorithmModelPointDO> pointDOS = pointMap.get(m.getDeviceId());
            if (CollectionUtils.isEmpty(pointDOS)) {
                return bo;
            }
            LambdaQueryWrapper<CommonMeasurePointDO> pointsWrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
            pointsWrapper.select(CommonMeasurePointDO::getPointId);
            pointsWrapper.in(CommonMeasurePointDO::getId, pointDOS.stream().map(item -> item.getPointId()).collect(Collectors.toList()));
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
