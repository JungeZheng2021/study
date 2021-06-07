package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.FeatureExtractionParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.FeatureExtractionResponseDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.SymptomParamDTO;
import com.aimsphm.nuclear.algorithm.enums.FeatureNameEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.service.FeatureExtractionOperationService;
import com.aimsphm.nuclear.common.entity.AlgorithmNormalFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.CommonSensorComponentDO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.enums.PointFeatureEnum;
import com.aimsphm.nuclear.common.enums.PointTypeEnum;
import com.aimsphm.nuclear.common.enums.TimeUnitEnum;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.service.AlgorithmNormalFaultFeatureService;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.CommonSensorComponentService;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.*;
import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_WAVE_DATA_ACC;
import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_WAVE_DATA_VEC;
import static com.aimsphm.nuclear.common.constant.ReportConstant.BLANK;
import static com.baomidou.mybatisplus.core.toolkit.StringPool.DASH;

/**
 * @Package: com.aimsphm.nuclear.algorithm.service.impl
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2021/06/03 12:44
 * @UpdateUser: milla
 * @UpdateDate: 2021/06/03 12:44
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service
@Scope("prototype")
public class FeatureExtractionOperationServiceImpl implements FeatureExtractionOperationService {
    @Resource(name = "FeatureExtraction")
    private AlgorithmHandlerService handlerService;
    @Resource(name = "SYMPTOM")
    private AlgorithmHandlerService symptomService;

    @Resource
    private CommonMeasurePointService pointService;
    @Resource
    private CommonSensorComponentService sensorComponentService;
    @Resource
    private AlgorithmNormalFaultFeatureService featureService;
    @Resource
    private HBaseUtil hBase;

    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redis;
    /**
     * 1小时的毫秒数
     */
    private long timeGap = 60 * 60 * 1000;

    @Override
    public void operationFeatureExtractionData(PointTypeEnum calculate) {
        List<FeatureExtractionParamDTO> params = pointService.listFeatureExtraction(calculate.getValue());
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        params.stream().forEach(x -> {
            if (Objects.nonNull(FeatureNameEnum.getByValue(x.getFeatName())) && Objects.nonNull(FeatureNameEnum.getByValue(x.getFeatName()).getValue())) {
                queryPointHistory(x);
                return;
            }
            if (PointFeatureEnum.VEC.getValue().equals(x.getType())) {
                x.setSignalKey(String.format(REDIS_WAVE_DATA_VEC, x.getSensorCode()));
                return;
            }
            if (PointFeatureEnum.ACC.getValue().equals(x.getType()) || PointFeatureEnum.ENVE.getValue().equals(x.getType())) {
                x.setSignalKey(String.format(REDIS_WAVE_DATA_ACC, x.getSensorCode()));
                return;
            }
        });
        List<FeatureExtractionParamDTO> collect = params.stream().filter(x -> filterParams(x)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            return;
        }
        List<FeatureExtractionResponseDTO> values = (List<FeatureExtractionResponseDTO>) handlerService.getInvokeCustomerData(collect);
        if (collect.size() != values.size()) {
            log.error("algorithm server's response Data is wrong...");
            return;
        }
        List<Put> list = Lists.newArrayList();
        for (int i = 0, len = collect.size(); i < len; i++) {
            FeatureExtractionParamDTO param = collect.get(i);
            FeatureExtractionResponseDTO response = values.get(i);
            if (Objects.isNull(response) || Objects.isNull(response.getTimestamp()) || Objects.isNull(response.getFeatValue())) {
                continue;
            }
            Long timestamp = response.getTimestamp();
            Integer index = hBase.rowKeyOf3600(timestamp).intValue();
            String rowKey = param.getSensorCode() + ROW_KEY_SEPARATOR + hBase.rowKeyOf3600(timestamp);
            String familyName = param.getType() + DASH + param.getFeatName();
            try {
                hBase.familyExists(H_BASE_TABLE_NPC_PHM_DATA, familyName, true, Compression.Algorithm.SNAPPY);
            } catch (IOException e) {
                log.error("create family failed.....");
            }
            Put put = hBase.creatNewPut(rowKey, timestamp, familyName, index, response.getFeatValue());
            list.add(put);
            //删除redis中的波形数据
//            redis.delete(param.getSignalKey());
        }
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        try {
            hBase.batchSave2HBase(H_BASE_TABLE_NPC_PHM_DATA, list);
        } catch (IOException e) {
            log.error("store data 2 hBase failed： {}", e);
        }
    }

    @Override
    public List<Integer> symptomJudgment(List<String> pointIds) {
        LambdaQueryWrapper<CommonMeasurePointDO> query = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        query.in(CommonMeasurePointDO::getPointId, pointIds);
        List<CommonMeasurePointDO> points = pointService.list(query);
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        LambdaQueryWrapper<CommonSensorComponentDO> sensorQuery = Wrappers.lambdaQuery(CommonSensorComponentDO.class);
        sensorQuery.in(CommonSensorComponentDO::getSensorCode, points.stream().map(x -> x.getSensorCode()).collect(Collectors.toSet()));
        List<CommonSensorComponentDO> sensorComponentList = sensorComponentService.list(sensorQuery);
        if (CollectionUtils.isEmpty(sensorComponentList)) {
            return null;
        }
        LambdaQueryWrapper<AlgorithmNormalFaultFeatureDO> wrapper = Wrappers.lambdaQuery(AlgorithmNormalFaultFeatureDO.class);
        wrapper.in(AlgorithmNormalFaultFeatureDO::getComponentId, sensorComponentList.stream().map(x -> x.getComponentId()).collect(Collectors.toSet()));
        List<AlgorithmNormalFaultFeatureDO> list = featureService.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        SymptomParamDTO params = new SymptomParamDTO();
        params.setFeatureInfo(list);
        List<List<Double>> collect = list.stream().map(x -> {
            String pointId = x.getSensorDesc();
            String sensorCode = x.getSensorCode();
            String family = pointId.replace(sensorCode, BLANK).substring(1);
            String timeRange = x.getTimeRange();
            if (StringUtils.isBlank(timeRange)) {
                return null;
            }
            long end = System.currentTimeMillis();
            Long gapValue = TimeUnitEnum.getGapValue(timeRange);
            if (Objects.isNull(gapValue)) {
                return null;
            }
            try {
                return hBase.listDoubleDataWith3600Columns(H_BASE_TABLE_NPC_PHM_DATA, sensorCode, end - gapValue, end, family);
            } catch (IOException e) {
                log.error("query history data failed.....");
            }
            return null;
        }).collect(Collectors.toList());
        params.setFeatureValue(collect);
        List<Integer> data = (List<Integer>) symptomService.getInvokeCustomerData(params);
        return data;
    }

    private boolean filterParams(FeatureExtractionParamDTO x) {
        return (Objects.nonNull(x.getSignalKey()) && redis.hasKey(x.getSignalKey())) || (FeatureNameEnum.PT.getType().equals(x.getFeatName()));
    }


    private void queryPointHistory(FeatureExtractionParamDTO param) {
        try {
            long end = System.currentTimeMillis();
            long start = end - timeGap;
            List<Double> data = hBase.listDoubleDataWith3600Columns(H_BASE_TABLE_NPC_PHM_DATA, param.getSensorCode(), start, end, FeatureNameEnum.getByValue(param.getFeatName()).getValue());
            param.setPeakList(data);
        } catch (IOException e) {
            log.error("query history data failed.....");
        }

    }
}
