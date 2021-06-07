package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.FaultReasoningParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.FaultReasoningResponseDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.SymptomParamDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.feign.AlgorithmServiceFeignClient;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

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
@Service("DIAGNOSIS-RE")
@ConditionalOnProperty(prefix = "spring.config", name = "enableAlgorithm", havingValue = "true")
public class FaultReasoningHandlerServiceImpl implements AlgorithmHandlerService<FaultReasoningParamDTO, FaultReasoningResponseDTO> {

    private AlgorithmServiceFeignClient client;

    public FaultReasoningHandlerServiceImpl(AlgorithmServiceFeignClient client) {
        this.client = client;
    }

    @Override
    public Object getInvokeCustomerData(FaultReasoningParamDTO params) {
        return invokeServer(client, params, AlgorithmTypeEnum.FAULT_DIAGNOSIS_RE.getType(), FaultReasoningResponseDTO.class);
    }
}
