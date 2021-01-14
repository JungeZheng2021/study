package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.AlgorithmParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.PredictionParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.PredictionResponseDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.feign.AlgorithmServiceFeignClient;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @Package: com.aimsphm.nuclear.history.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/22 13:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/22 13:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service("TF")
@ConditionalOnProperty(prefix = "spring.config", name = "enableAlgorithm", havingValue = "true")
public class PredictionServiceImpl implements AlgorithmHandlerService<PredictionParamDTO, PredictionResponseDTO> {

    @Resource
    private AlgorithmServiceFeignClient client;

    @Override
    public Object getInvokeCustomerData(PredictionParamDTO params) {
        PredictionResponseDTO data = invokeServer(client, params, AlgorithmTypeEnum.TREND_FORECAST.getType(), PredictionResponseDTO.class);
        if (Objects.isNull(data) || CollectionUtils.isEmpty(data.getPredSignal()) ||
                CollectionUtils.isEmpty(data.getPredTimestamp())) {
            return null;
        }
        List<Double> predSignalList = data.getPredSignal();
        List<Long> timestampList = data.getPredTimestamp();
        if (timestampList.size() != predSignalList.size()) {
            throw new CustomMessageException("算法结果异常");
        }
        List<List<Object>> list = Lists.newArrayList();
        IntStream.range(0, predSignalList.size()).forEach(x -> {
            Double value = predSignalList.get(x);
            Long timestamp = timestampList.get(x);
            list.add(Lists.newArrayList(timestamp, value));
        });
        return list;
    }
}
