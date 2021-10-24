package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.FaultDiagnosisParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.FaultDiagnosisResponseDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.feign.AlgorithmServiceFeignClient;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 功能描述:故障推理
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-02-01 16:49
 */
@Slf4j
@Service("DIAGNOSIS")
@ConditionalOnProperty(prefix = "spring.config", name = "enableAlgorithm", havingValue = "true")
public class FaultDiagnosisServiceImpl implements AlgorithmHandlerService<FaultDiagnosisParamDTO, FaultDiagnosisResponseDTO> {

    private final AlgorithmServiceFeignClient client;

    public FaultDiagnosisServiceImpl(AlgorithmServiceFeignClient client) {
        this.client = client;
    }

    @Override
    public FaultDiagnosisResponseDTO getInvokeCustomerData(FaultDiagnosisParamDTO params) {
        return invokeServer(client, params, AlgorithmTypeEnum.FAULT_DIAGNOSIS.getType(), FaultDiagnosisResponseDTO.class);
    }
}
