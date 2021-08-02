package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.PrognosticForecastItemResponseDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.PrognosticForecastResponseDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.SymptomParamDTO;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.service.PrognosticForecastService;
import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.service.AlgorithmPrognosticFaultFeatureService;
import com.aimsphm.nuclear.common.service.BizDownSampleService;
import com.aimsphm.nuclear.common.service.CommonComponentService;
import com.aimsphm.nuclear.common.service.JobForecastResultService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.SymbolConstant.*;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/07/15 16:11
 */
@Slf4j
@Service
public class PrognosticForecastServiceImpl implements PrognosticForecastService {
    @Resource
    private AlgorithmPrognosticFaultFeatureService prognosticFaultFeatureService;
    @Resource
    private JobForecastResultService forecastResultService;
    @Resource
    private CommonComponentService componentService;

    @Resource(name = "Forecast")
    private AlgorithmHandlerService handlerService;

    @Resource
    private BizDownSampleService downSampleService;

    @Override
    public void prognosticForecast() {
        List<AlgorithmPrognosticFaultFeatureDO> list = prognosticFaultFeatureService.list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Long endTime = System.currentTimeMillis();
        list.stream().filter(x -> Objects.nonNull(x.getComponentId())).collect(Collectors.groupingBy(AlgorithmPrognosticFaultFeatureDO::getComponentId)).forEach((key, value) -> {
            try {
                prognosticForecast(key, value, endTime);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void prognosticForecastByComponentId(Long componentId) {
        LambdaQueryWrapper<AlgorithmPrognosticFaultFeatureDO> query = Wrappers.lambdaQuery(AlgorithmPrognosticFaultFeatureDO.class);
        query.eq(AlgorithmPrognosticFaultFeatureDO::getComponentId, componentId);
        List<AlgorithmPrognosticFaultFeatureDO> list = prognosticFaultFeatureService.list(query);
        prognosticForecast(componentId, list, System.currentTimeMillis());
    }

    public void prognosticForecast(Long componentId, List<AlgorithmPrognosticFaultFeatureDO> value, Long endTime) {
        if (CollectionUtils.isEmpty(value)) {
            return;
        }
        List<AlgorithmNormalFaultFeatureDO> collect = value.stream().map(x -> {
            AlgorithmNormalFaultFeatureDO featureDO = new AlgorithmNormalFaultFeatureDO();
            BeanUtils.copyProperties(x, featureDO);
            return featureDO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        try {
            LambdaQueryWrapper<BizDownSampleDO> query = Wrappers.lambdaQuery(BizDownSampleDO.class);
            List<BizDownSampleDO> list = downSampleService.list(query);
            if (CollectionUtils.isEmpty(list)) {
                log.warn("this no data to invoker algorithm server");
                return;
            }
            Map<String, List<List>> featureValue = list.stream().collect(Collectors.toMap(x -> x.getComponentId() + UNDERLINE + x.getPointId(), x -> JSON.parseArray(x.getData(), List.class)));
            SymptomParamDTO dto = new SymptomParamDTO();
            dto.setFeatureInfo(collect);
            dto.setInvokingTime(endTime);
            dto.setFeatureValue(featureValue);
            PrognosticForecastResponseDTO response = (PrognosticForecastResponseDTO) handlerService.getInvokeCustomerData(dto);
            operateResponse(componentId, response, featureValue);
        } catch (Exception e) {
            log.error("operate data get a failed....{}", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void operateResponse(Long componentId, PrognosticForecastResponseDTO response, Map<String, List<List>> map) {
        if (Objects.isNull(response)) {
            return;
        }
        List<PrognosticForecastItemResponseDTO> predData = response.getPredData();
        if (CollectionUtils.isEmpty(predData)) {
            return;
        }
        CommonComponentDO componentDO = componentService.getById(componentId);
        if (Objects.isNull(componentDO)) {
            return;
        }
        predData.forEach(x -> {
            String pointId = x.getPointId();
            if (StringUtils.isBlank(pointId)) {
                return;
            }
            List<List> history = map.get(componentId + UNDERLINE + pointId);
            JobForecastResultDO forecast = new JobForecastResultDO();
            forecast.setPointId(pointId);
            forecast.setComponentId(componentId);
            forecast.setDeviceId(componentDO.getDeviceId());
            LambdaUpdateWrapper<JobForecastResultDO> update = Wrappers.lambdaUpdate(JobForecastResultDO.class);
            update.eq(JobForecastResultDO::getComponentId, componentId);
            update.eq(JobForecastResultDO::getPointId, pointId);
            //只有预测值和趋势值都不为空的时候才会存储
            if (CollectionUtils.isEmpty(x.getPred()) || CollectionUtils.isEmpty(x.getHistory())) {
                forecast.setGmtModified(new Date());
                forecastResultService.saveOrUpdate(forecast, update);
                return;
            }
            String symptomIds = null;
            if (!CollectionUtils.isEmpty(response.getSymptomList())) {
                symptomIds = response.getSymptomList().stream().map(String::valueOf).collect(Collectors.joining(COMMA));
            }
            forecast.setForecastRange(response.getPredRange());
            forecast.setGmtForecast(new Date(response.getPredTime()));
            forecast.setHistoryData(Objects.isNull(history) ? null : JSON.toJSONString(history));
            forecast.setForecastData(Objects.isNull(x.getPred()) ? null : JSON.toJSONString(x.getPred()));
            forecast.setTrendData(Objects.isNull(x.getHistory()) ? null : JSON.toJSONString(x.getHistory()));
            update.set(JobForecastResultDO::getSymptomIds, symptomIds);
            forecastResultService.saveOrUpdate(forecast, update);
        });
    }
}
