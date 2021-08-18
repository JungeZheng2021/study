package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.bo.*;
import com.aimsphm.nuclear.algorithm.entity.dto.AlarmEventDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.StateMonitorParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.StateMonitorResponseDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmAsyncService;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesObjectDTO;
import com.aimsphm.nuclear.common.enums.DeviceHealthEnum;
import com.aimsphm.nuclear.common.enums.PointTypeEnum;
import com.aimsphm.nuclear.common.service.*;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
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
import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_KEY_DEVICE_CONDITION;
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
@Scope("prototype")
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
    private JobAlarmProcessRecordService recordService;
    @Resource
    private HBaseUtil hBase;
    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redis;
    /**
     * 15天毫秒值 残差是15天内的
     */
    private Integer days15 = 10 * 3600 * 1000;

    @Override
    public void deviceStateMonitorInfo(AlgorithmTypeEnum algorithmType, Long deviceId, Integer algorithmPeriod) {
        StateMonitorParamDTO data = operateParams(deviceId, algorithmType, 1);
        if (Objects.isNull(data)) {
            return;
        }
        data.setAlgorithmPeriod(algorithmPeriod);
        data.setInvokingTime(System.currentTimeMillis());
        StateMonitorResponseDTO response = (StateMonitorResponseDTO) handlerService.getInvokeCustomerData(data);
        saveResult(response);
    }

    @Override
    public void deviceThresholdMonitorInfo(AlgorithmTypeEnum algorithmType, Long deviceId, Integer algorithmPeriod) {
        StateMonitorParamDTO data = operateParams(deviceId, algorithmType, 0);
        if (Objects.isNull(data)) {
            return;
        }
        data.setAlgorithmPeriod(algorithmPeriod);
        data.setInvokingTime(System.currentTimeMillis());
        StateMonitorResponseDTO response = (StateMonitorResponseDTO) handlerService.getInvokeCustomerData(data);
        if (Objects.isNull(response)) {
            return;
        }
        saveResult(response);
    }

    @Transactional(rollbackFor = Exception.class)
    void saveResult(StateMonitorResponseDTO response) {
        if (Objects.isNull(response)) {
            return;
        }
        saveDeviceCondition(response);
        saveOrUpdateEvent(response);
        saveEstimateResult(response.getModelEstimateResult());
        updateDeviceStatus(response.getDeviceId(), response.getHealthStatus());
    }

    private void saveDeviceCondition(StateMonitorResponseDTO response) {
        Long timestamp = response.getTimestamp();
        Integer value = response.getOperationCondition();
        if (Objects.isNull(timestamp) || Objects.isNull(value)) {
            return;
        }
        HBaseTimeSeriesObjectDTO lastCondition = new HBaseTimeSeriesObjectDTO();
        lastCondition.setValue(value);
        lastCondition.setTimestamp(timestamp);
        redis.opsForValue().set(REDIS_KEY_DEVICE_CONDITION + response.getDeviceId(), lastCondition);
        try {
            hBase.familyExists(H_BASE_TABLE_NPC_PHM_DATA, H_BASE_FAMILY_NPC_CONDITION, true, Compression.Algorithm.SNAPPY);
            hBase.insertObject(H_BASE_TABLE_NPC_PHM_DATA, response.getDeviceId() + ROW_KEY_SEPARATOR + timestamp, H_BASE_FAMILY_NPC_CONDITION, response.getDeviceId(), value, timestamp);
        } catch (IOException e) {
            log.error("HBase insert failed...{}", e);
        }
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
    }

    private void saveOrUpdateEvent(StateMonitorResponseDTO response) {
        List<AlarmEventDTO> values = response.getTxAlarmEvent();
        if (CollectionUtils.isEmpty(values)) {
            return;
        }
        List<JobAlarmRealtimeDO> realTimeList = Lists.newArrayList();
        for (int i = 0, len = values.size(); i < len; i++) {
            AlarmEventDTO eventDTO = values.get(i);
            List<JobAlarmRealtimeDO> realTimeAlarms = eventDTO.getRealTimeAlarms();
            if (eventDTO.getId() <= -1) {
                eventDTO.setId(null);
                eventDTO.setDeviceId(response.getDeviceId());
                eventDTO.setSubSystemId(response.getSubSystemId());
            }
            eventDTO.setDeviceCode(response.getDeviceCode());
            eventDTO.setDeviceName(response.getDeviceName());
            //保存事件
            eventService.saveOrUpdate(eventDTO);
            if (CollectionUtils.isNotEmpty(realTimeAlarms)) {
                realTimeAlarms.stream().forEach(x -> {
                    x.setEventId(eventDTO.getId());
                    x.setDeviceId(response.getDeviceId());
                    x.setSubSystemId(response.getSubSystemId());
                });
                realTimeList.addAll(realTimeAlarms);
            }
            //保存record
            JobAlarmProcessRecordDO processRecord = eventDTO.getProcessRecord();
            if (Objects.nonNull(processRecord)) {
                processRecord.setDeviceId(response.getDeviceId());
                processRecord.setSubSystemId(response.getSubSystemId());
                processRecord.setEventId(eventDTO.getId());
                recordService.save(processRecord);
            }
        }
        //保存时时报警
        saveRealtimeAlarm(realTimeList);
    }

    /**
     * 更新设备状态启停状态
     *
     * @param deviceId     设备id
     * @param healthStatus 健康状态
     */
    private void updateDeviceStatus(Long deviceId, Integer healthStatus) {
        //如果是算法给个null不做任何处理
        if (Objects.isNull(healthStatus)) {
            return;
        }
        CommonDeviceDO device = deviceService.getById(deviceId);
        if (Objects.isNull(device)) {
            return;
        }
        JobDeviceStatusDO status = statusService.getDeviceRunningStatus(deviceId);
        //之前没有状态，且现在是停机需要保存
        if (Objects.isNull(status)) {
            Integer newStatus = statusService.getDeviceCurrentStatus(device, healthStatus);
            JobDeviceStatusDO newOne = new JobDeviceStatusDO();
            newOne.setStatus(newStatus);
            newOne.setDeviceId(deviceId);
            newOne.setGmtStart(new Date());
            statusService.save(newOne);
            return;
        }
        // 停机状态1.本次和上次都是停机状态
        if (status.getStatus().equals(healthStatus) && DeviceHealthEnum.STOP.getValue().equals(healthStatus)) {
            return;
        }
        // 停机状态2.直接修改上次状态持续时间，记录本次停机开始时间
        if (DeviceHealthEnum.STOP.getValue().equals(healthStatus)) {
            statusService.saveOrUpdateDeviceStatus(status, healthStatus);
            return;
        }
        //以下是非停机状态
        //上一次是停机，本次是启动需要改动 最近启动配置
        if (DeviceHealthEnum.STOP.getValue().equals(status.getStatus()) && !DeviceHealthEnum.STOP.getValue().equals(healthStatus)) {
            detailsService.updateLastStartTime(deviceId);
            //异步删除一些数据
            asyncService.deleteData(deviceId, null);
        }
        //启动报警状态且启停状态是启动状态，需要计算设备状态
        statusService.updateDeviceStatusWithCalculate(status, device, healthStatus);
    }

    /**
     * 获取状态监测算法、设备、模型、测点之间的数据
     *
     * @return
     */
    private StateMonitorParamDTO operateParams(Long deviceId, AlgorithmTypeEnum type, Integer modelType) {
        LambdaQueryWrapper<AlgorithmConfigDO> wrapper = Wrappers.lambdaQuery(AlgorithmConfigDO.class);
        wrapper.eq(AlgorithmConfigDO::getAlgorithmType, type.getType());
        AlgorithmConfigDO algorithmConfig = configService.getOne(wrapper);
        if (Objects.isNull(algorithmConfig)) {
            return null;
        }
        LambdaQueryWrapper<AlgorithmModelDO> modelWrapper = Wrappers.lambdaQuery(AlgorithmModelDO.class);
        modelWrapper.eq(AlgorithmModelDO::getAlgorithmId, algorithmConfig.getId()).eq(AlgorithmModelDO::getDeviceId, deviceId);
        List<AlgorithmModelDO> modelList = modelService.list(modelWrapper);
        if (CollectionUtils.isEmpty(modelList)) {
            return null;
        }
        LambdaQueryWrapper<AlgorithmModelPointDO> modePointWrapper = Wrappers.lambdaQuery(AlgorithmModelPointDO.class);
        modePointWrapper.in(AlgorithmModelPointDO::getModelId, modelList.stream().map(x -> x.getId()).collect(Collectors.toSet()));
        List<AlgorithmModelPointDO> pointDOS = pointService.list(modePointWrapper);
        if (CollectionUtils.isEmpty(pointDOS)) {
            return null;
        }
        Map<Long, List<AlgorithmModelPointDO>> pointMap = pointDOS.stream().collect(Collectors.groupingBy(x -> x.getModelId()));

        CommonDeviceDO device = deviceService.getById(deviceId);
        //设备不存在或者是 设备未开启报警且modelType不是启停模型 1：开启 0:未开启
        boolean isNeed = Objects.isNull(device) || (!device.getEnableMonitor() && modelType != 0);
        if (isNeed) {
            return null;
        }
        StateMonitorParamDTO param = new StateMonitorParamDTO();
        param.setTypeEnum(type);
        param.setDeviceId(deviceId);
        param.setDeviceName(device.getDeviceName());
        param.setDeviceCode(device.getDeviceCode());
        param.setSubSystemId(device.getSubSystemId());
        param.setModelIds(modelList.stream().map(v -> v.getId()).distinct().collect(Collectors.toList()));
        param.setOnlyCondition(1);
        //启停算法不需要残值值
        if (modelType != 0) {
            param.setOnlyCondition(0);
            param.setModelEstimateResult(listEstimateData(modelList, pointMap));
        }
        //原始值
        param.setSensorData(listPointData(pointDOS));
        //两个参数都是空的话不调用算法
        if (CollectionUtils.isEmpty(param.getSensorData()) && CollectionUtils.isEmpty(param.getModelEstimateResult())) {
            return null;
        }
        param.setTxAlarmEvent(listAlarmEventByDeviceId(deviceId));
        setLastCondition(param);
        return param;
    }

    /**
     * 获取最后一次工况信息
     *
     * @param param
     */
    private void setLastCondition(StateMonitorParamDTO param) {
        HBaseTimeSeriesObjectDTO lastCondition = (HBaseTimeSeriesObjectDTO) redis.opsForValue().get(REDIS_KEY_DEVICE_CONDITION + param.getDeviceId());
        if (Objects.nonNull(lastCondition)) {
            param.setLastCondition(lastCondition);
        }
    }

    /**
     * 获取没有结束的报警
     *
     * @param deviceId 设备id
     * @return
     */
    private List<JobAlarmEventDO> listAlarmEventByDeviceId(Long deviceId) {
        LambdaQueryWrapper<JobAlarmEventDO> wrapper = Wrappers.lambdaQuery(JobAlarmEventDO.class);
        wrapper.eq(JobAlarmEventDO::getDeviceId, deviceId).ne(JobAlarmEventDO::getAlarmStatus, 0);
        return eventService.list(wrapper);
    }

    private List<EstimateParamDataBO> listEstimateData(List<AlgorithmModelDO> modelList, Map<Long, List<AlgorithmModelPointDO>> pointMap) {
        return modelList.stream().map(m -> {
            Integer modelType = m.getModelType();
            if (modelType != 1) {
                return null;
            }
            EstimateParamDataBO bo = new EstimateParamDataBO();
            bo.setModelId(m.getId());
            bo.setModelName(m.getModelName());
            List<AlgorithmModelPointDO> pointDOS = pointMap.get(m.getId());
            if (CollectionUtils.isEmpty(pointDOS)) {
                return bo;
            }
            LambdaQueryWrapper<CommonMeasurePointDO> pointsWrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
            pointsWrapper.select(CommonMeasurePointDO::getPointId);
            pointsWrapper.in(CommonMeasurePointDO::getId, pointDOS.stream().map(item -> item.getPointId()).collect(Collectors.toList()));
            List<CommonMeasurePointDO> list = pointsService.list(pointsWrapper);
            bo.setEstimateTotal(listPointEstimateResultsData(m.getId(), list));
            return bo;
        }).filter(Objects::nonNull).collect(Collectors.toList());
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
                CommonMeasurePointDO::getSensorCode, CommonMeasurePointDO::getFeatureType, CommonMeasurePointDO::getFeature);
        pointsWrapper.in(CommonMeasurePointDO::getId, pointList.stream().map(item -> item.getPointId()).collect(Collectors.toSet()));
        List<CommonMeasurePointDO> list = pointsService.list(pointsWrapper);
        CountDownLatch countDownLatch = new CountDownLatch(list.size());
        List<PointDataBO> collect = Lists.newArrayList();
        for (CommonMeasurePointDO item : list) {
            PointDataBO data = new PointDataBO();
            data.setPointId(item.getPointId());
            String family = PointTypeEnum.PI.getValue().equals(item.getPointType()) ? H_BASE_FAMILY_NPC_PI_REAL_TIME : item.getFeatureType() + DASH + item.getFeature();
//            asyncService.listPointDataFromHBase(family, item.getId(), item.getSensorCode(), data, countDownLatch);
            asyncService.listPointDataFromHBase(item, data, countDownLatch);
            collect.add(data);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return collect;
    }
}