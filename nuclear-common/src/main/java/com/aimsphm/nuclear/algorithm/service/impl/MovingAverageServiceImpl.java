package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.AlgorithmParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.MovingAverageParamDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.MovingAverageResponseDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.feign.AlgorithmServiceFeignClient;
import com.aimsphm.nuclear.algorithm.service.AlgorithmHandlerService;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

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
@Service("MA")
@ConditionalOnProperty(prefix = "spring.config", name = "enableAlgorithm", havingValue = "true")
public class MovingAverageServiceImpl implements AlgorithmHandlerService<MovingAverageParamDTO, MovingAverageResponseDTO> {

    private AlgorithmServiceFeignClient client;

    public MovingAverageServiceImpl(AlgorithmServiceFeignClient client) {
        this.client = client;
    }

    @Override
    public Object getInvokeCustomerData(MovingAverageParamDTO params) {
        MovingAverageResponseDTO data = invokeServer(client, params, AlgorithmTypeEnum.MOVING_AVERAGE.getType(), MovingAverageResponseDTO.class);
        if (Objects.isNull(data) || CollectionUtils.isEmpty(data.getSmoothedSignal())) {
            return null;
        }
        List<Double> smoothedSignal = data.getSmoothedSignal();
        List<List<Object>> signal = params.getSignal();
        if (signal.size() != smoothedSignal.size()) {
            throw new CustomMessageException("算法结果异常");
        }
        IntStream.range(0, signal.size()).forEach(x -> {
            List<Object> objects = signal.get(x);
            Double aDouble = smoothedSignal.get(x);
            objects.set(1, aDouble);
        });
        return signal;
    }
}
