package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.StateMonitorParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.StateMonitorResponseDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.feign.AlgorithmServiceFeignClient;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

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
@Service("HCM-PAF")
@ConditionalOnProperty(prefix = "spring.config", name = "enableAlgorithm", havingValue = "true")
public class StateMonitorServiceImpl implements AlgorithmHandlerService<StateMonitorParamDTO, StateMonitorResponseDTO> {

    private AlgorithmServiceFeignClient client;

    public StateMonitorServiceImpl(AlgorithmServiceFeignClient client) {
        this.client = client;
    }

    @Override
    public Object getInvokeCustomerData(StateMonitorParamDTO params) {
        return invokeServer(client, params, AlgorithmTypeEnum.STATE_MONITOR.getType(), StateMonitorResponseDTO.class);
    }
}
