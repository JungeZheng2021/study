package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.PrognosticForecastResponseDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.SymptomParamDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.feign.AlgorithmServiceFeignClient;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/07/15 18:39
 */
@Slf4j
@Service("Forecast")
@ConditionalOnProperty(prefix = "spring.config", name = "enableAlgorithm", havingValue = "true")
public class ForecastServiceImpl implements AlgorithmHandlerService<SymptomParamDTO, PrognosticForecastResponseDTO> {

    private AlgorithmServiceFeignClient client;

    public ForecastServiceImpl(AlgorithmServiceFeignClient client) {
        this.client = client;
    }

    @Override
    public Object getInvokeCustomerData(SymptomParamDTO params) {
        return invokeServer(client, params, AlgorithmTypeEnum.FAULT_PROGNOSTIC.getType(), PrognosticForecastResponseDTO.class);
    }
}