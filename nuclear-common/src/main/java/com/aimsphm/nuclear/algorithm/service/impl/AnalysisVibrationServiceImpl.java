package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.AnalysisVibrationParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.AnalysisVibrationResponseDTO;
import com.aimsphm.nuclear.algorithm.feign.AlgorithmServiceFeignClient;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Package: com.aimsphm.nuclear.algorithm.service.impl
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2021/01/14 13:35
 * @UpdateUser: milla
 * @UpdateDate: 2021/01/14 13:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service("ANALYSIS")
@ConditionalOnProperty(prefix = "spring.config", name = "enableAlgorithm", havingValue = "true")
public class AnalysisVibrationServiceImpl implements AlgorithmHandlerService<AnalysisVibrationParamDTO, AnalysisVibrationResponseDTO> {

    @Resource
    private AlgorithmServiceFeignClient client;

    @Override
    public Object getInvokeCustomerData(AnalysisVibrationParamDTO params) {
        AnalysisVibrationResponseDTO responseDTO = invokeServer(client, params, params.getType(), AnalysisVibrationResponseDTO.class);
        return responseDTO;
    }
}
