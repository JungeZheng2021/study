package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.AlgorithmParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.StateMonitorParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.StateMonitorResponseDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.feign.AlgorithmServiceFeignClient;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

    private AlgorithmServiceFeignClient algorithmClient;

    public StateMonitorServiceImpl(AlgorithmServiceFeignClient algorithmClient) {
        this.algorithmClient = algorithmClient;
    }

    @Override
    public Object getInvokeCustomerData(StateMonitorParamDTO params) {
        StateMonitorResponseDTO data = invokeServer(params, AlgorithmTypeEnum.STATE_MONITOR.getType(), StateMonitorResponseDTO.class);
        return data;
    }

    @Override
    public ResponseData<StateMonitorResponseDTO> getInvokeServer(AlgorithmParamDTO<StateMonitorParamDTO> params) {
        log.info("请求参数");
        try {
            log.info("{}", new ObjectMapper().writeValueAsString(params));
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkParams(params);
        return algorithmClient.algorithmInvokeByParams(params);
    }
}
