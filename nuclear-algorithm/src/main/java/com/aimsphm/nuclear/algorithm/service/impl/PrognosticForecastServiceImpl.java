package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.PrognosticForecastItemResponseDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.PrognosticForecastResponseDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.SymptomParamDTO;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.service.PrognosticForecastService;
import com.aimsphm.nuclear.common.entity.AlgorithmNormalFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.AlgorithmPrognosticFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.CommonComponentDO;
import com.aimsphm.nuclear.common.entity.JobForecastResultDO;
import com.aimsphm.nuclear.common.enums.TimeUnitEnum;
import com.aimsphm.nuclear.common.service.AlgorithmPrognosticFaultFeatureService;
import com.aimsphm.nuclear.common.service.CommonComponentService;
import com.aimsphm.nuclear.common.service.JobForecastResultService;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.*;
import static com.aimsphm.nuclear.common.constant.ReportConstant.BLANK;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.COMMA;

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
    private HBaseUtil hBase;
    @Resource
    private AlgorithmPrognosticFaultFeatureService prognosticFaultFeatureService;
    @Resource
    private JobForecastResultService forecastResultService;
    @Resource
    private CommonComponentService componentService;

    @Resource(name = "Forecast")
    private AlgorithmHandlerService handlerService;

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
        List<Get> gets = new ArrayList<>();
        List<AlgorithmNormalFaultFeatureDO> collect = value.stream().map(x -> {
            AlgorithmNormalFaultFeatureDO featureDO = new AlgorithmNormalFaultFeatureDO();
            BeanUtils.copyProperties(x, featureDO);
            calculateGets(x, gets, endTime);
            return featureDO;
        }).collect(Collectors.toList());
        try {
            Map<String, List<List<Object>>> map = hBase.selectByGets(H_BASE_TABLE_NPC_PHM_DATA, gets);
            System.out.println(JSON.toJSONString(map));
            List<List<List<Object>>> featureValue = new ArrayList<>();
            collect.forEach(x -> featureValue.add(map.get(x.getSensorDesc())));
            SymptomParamDTO dto = new SymptomParamDTO();
            dto.setFeatureInfo(collect);
            dto.setInvokingTime(endTime);
            dto.setFeatureValue(featureValue);
            PrognosticForecastResponseDTO response = (PrognosticForecastResponseDTO) handlerService.getInvokeCustomerData(dto);
            operateResponse(componentId, response, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void operateResponse(Long componentId, PrognosticForecastResponseDTO response, Map<String, List<List<Object>>> map) {
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
            List<List<Object>> history = map.get(pointId);
            JobForecastResultDO forecast = new JobForecastResultDO();
            forecast.setPointId(pointId);
            forecast.setComponentId(componentId);
            forecast.setDeviceId(componentDO.getDeviceId());
            forecast.setForecastRange(response.getPredRange());
            forecast.setGmtForecast(new Date(response.getPredTime()));
            forecast.setHistoryData(Objects.isNull(history) ? null : JSON.toJSONString(history));
            forecast.setForecastData(Objects.isNull(x.getPred()) ? null : JSON.toJSONString(x.getPred()));
            forecast.setTrendData(Objects.isNull(x.getHistory()) ? null : JSON.toJSONString(x.getHistory()));
            LambdaUpdateWrapper<JobForecastResultDO> update = Wrappers.lambdaUpdate(JobForecastResultDO.class);
            update.eq(JobForecastResultDO::getComponentId, componentId);
            update.eq(JobForecastResultDO::getPointId, pointId);
            String symptomIds = null;
            if (!CollectionUtils.isEmpty(x.getSymptomList())) {
                symptomIds = x.getSymptomList().stream().map(String::valueOf).collect(Collectors.joining(COMMA));
            }
            update.set(JobForecastResultDO::getSymptomIds, symptomIds);
            forecastResultService.saveOrUpdate(forecast, update);
        });
    }

    private void calculateGets(AlgorithmPrognosticFaultFeatureDO x, List<Get> gets, Long endTime) {
        if (Objects.isNull(x) || Objects.isNull(x.getTimeRange()) || Objects.isNull(x.getTimeGap())) {
            return;
        }
        String timeRange = x.getTimeRange();
        Long gapValue = TimeUnitEnum.getGapValue(timeRange);
        if (Objects.isNull(gapValue)) {
            return;
        }
        String timeGap = x.getTimeGap();
        Long timeGapValue = TimeUnitEnum.getGapValue(timeGap);
        if (Objects.isNull(timeGapValue)) {
            return;
        }
        Long startTime = endTime - gapValue;
        while (startTime <= endTime) {
            Integer index = hBase.indexOf3600(startTime);
            Long key = hBase.rowKeyOf3600(startTime);
            Get get = new Get((x.getSensorCode() + ROW_KEY_SEPARATOR + key).getBytes(StandardCharsets.UTF_8));
            String pointId = x.getSensorDesc();
            String sensorCode = x.getSensorCode();
            if (StringUtils.isBlank(sensorCode)) {
                continue;
            }
            String family = H_BASE_FAMILY_NPC_PI_REAL_TIME;
            if (!StringUtils.equals(pointId, sensorCode)) {
                family = pointId.replace(sensorCode, BLANK).substring(1);
            }
            get.addColumn(family.getBytes(StandardCharsets.UTF_8), Bytes.toBytes(index));
            gets.add(get);
            startTime = startTime + timeGapValue;
        }
    }
}
