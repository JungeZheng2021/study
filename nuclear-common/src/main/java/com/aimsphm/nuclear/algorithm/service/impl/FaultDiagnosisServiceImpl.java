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
 * @Package: com.aimsphm.nuclear.history.service
 * @Description: <故障推理>
 * @Author: MILLA
 * @CreateDate: 2020/12/22 13:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/22 13:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service("DIAGNOSIS")
@ConditionalOnProperty(prefix = "spring.config", name = "enableAlgorithm", havingValue = "true")
public class FaultDiagnosisServiceImpl implements AlgorithmHandlerService<FaultDiagnosisParamDTO, FaultDiagnosisResponseDTO> {

    private AlgorithmServiceFeignClient client;

    public FaultDiagnosisServiceImpl(AlgorithmServiceFeignClient client) {
        this.client = client;
    }

    @Override
    public FaultDiagnosisResponseDTO getInvokeCustomerData(FaultDiagnosisParamDTO params) {
        return invokeServer(client, params, AlgorithmTypeEnum.FAULT_DIAGNOSIS.getType(), FaultDiagnosisResponseDTO.class);
    }
}
