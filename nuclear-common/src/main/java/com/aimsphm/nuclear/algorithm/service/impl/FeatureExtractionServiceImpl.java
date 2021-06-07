package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.FeatureExtractionParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.FeatureExtractionResponseDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.feign.AlgorithmServiceFeignClient;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.history.service
 * @Description: <特征提取算法>
 * @Author: MILLA
 * @CreateDate: 2020/12/22 13:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/22 13:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service("FeatureExtraction")
@ConditionalOnProperty(prefix = "spring.config", name = "enableAlgorithm", havingValue = "true")
public class FeatureExtractionServiceImpl implements AlgorithmHandlerService<List<FeatureExtractionParamDTO>, FeatureExtractionResponseDTO> {

    private AlgorithmServiceFeignClient client;

    public FeatureExtractionServiceImpl(AlgorithmServiceFeignClient client) {
        this.client = client;
    }

    @Override
    public Object getInvokeCustomerData(List<FeatureExtractionParamDTO> params) {
        return invokeServerArray(client, params, AlgorithmTypeEnum.FEATURE_EXTRACTOR.getType(), FeatureExtractionResponseDTO.class);
    }
}
