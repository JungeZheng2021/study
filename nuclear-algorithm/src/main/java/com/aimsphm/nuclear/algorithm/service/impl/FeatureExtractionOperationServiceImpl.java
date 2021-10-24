package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.FeatureExtractionParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.FeatureExtractionResponseDTO;
import com.aimsphm.nuclear.algorithm.enums.FeatureNameEnum;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.algorithm.service.FeatureExtractionOperationService;
import com.aimsphm.nuclear.common.enums.PointFeatureEnum;
import com.aimsphm.nuclear.common.enums.PointTypeEnum;
import com.aimsphm.nuclear.common.service.AlgorithmNormalFaultFeatureService;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.CommonSensorComponentService;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_TABLE_NPC_PHM_DATA;
import static com.aimsphm.nuclear.common.constant.HBaseConstant.ROW_KEY_SEPARATOR;
import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_WAVE_DATA_ACC;
import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_WAVE_DATA_VEC;
import static com.baomidou.mybatisplus.core.toolkit.StringPool.DASH;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/06/03 12:44
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
    private long timeGap = 3600_000;

    @Override
    public void operationFeatureExtractionData(PointTypeEnum calculate) {
        List<FeatureExtractionParamDTO> params = pointService.listFeatureExtraction(calculate.getValue());
        if (CollectionUtils.isEmpty(params)) {
            return;
        }
        setRedisKey(params);
        List<FeatureExtractionParamDTO> collect = params.stream().filter(this::filterParams).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            log.debug("has no data, invoke algorithm not active.....");
            return;
        }
        List<FeatureExtractionResponseDTO> values = (List<FeatureExtractionResponseDTO>) handlerService.getInvokeCustomerData(collect);
        if (CollectionUtils.isEmpty(values) || collect.size() != values.size()) {
            log.error("algorithm server's response Data is wrong...");
            return;
        }
        operateResponseData(collect, values);
    }

    private void operateResponseData(List<FeatureExtractionParamDTO> collect, List<FeatureExtractionResponseDTO> values) {
        List<Put> list = Lists.newArrayList();
        for (int i = 0, len = collect.size(); i < len; i++) {
            FeatureExtractionParamDTO param = collect.get(i);
            FeatureExtractionResponseDTO response = values.get(i);
            if (Objects.isNull(response) || Objects.isNull(response.getTimestamp()) || Objects.isNull(response.getFeatValue())) {
                continue;
            }
            Long timestamp = response.getTimestamp();
            Integer index = hBase.indexOf3600(timestamp);
            String rowKey = param.getSensorCode() + ROW_KEY_SEPARATOR + hBase.rowKeyOf3600(timestamp);
            String familyName = param.getType() + DASH + param.getFeatName();
            try {
                hBase.familyExists(H_BASE_TABLE_NPC_PHM_DATA, familyName, true, Compression.Algorithm.SNAPPY);
            } catch (IOException e) {
                log.error("create family failed.....");
            }
            Put put = hBase.creatNewPut(rowKey, timestamp, familyName, index, response.getFeatValue());
            list.add(put);
//            删除redis中的波形数据
            redis.delete(param.getSignalKey());
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

    private void setRedisKey(List<FeatureExtractionParamDTO> params) {
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