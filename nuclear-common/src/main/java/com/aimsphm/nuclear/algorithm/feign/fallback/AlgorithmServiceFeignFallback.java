package com.aimsphm.nuclear.algorithm.feign.fallback;

import com.aimsphm.nuclear.algorithm.entity.dto.AlgorithmParamDTO;
import com.aimsphm.nuclear.algorithm.feign.AlgorithmServiceFeignClient;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @Package: com.aimsphm.nuclear.data.feign
 * @Description: <服务调用失败处理>
 * @Author: MILLA
 * @CreateDate: 2020/4/2 17:52
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/2 17:52
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "spring.config", name = "enableAlgorithm", havingValue = "true")
public class AlgorithmServiceFeignFallback implements AlgorithmServiceFeignClient {

    @Override
    public ResponseData algorithmInvokeByParams(AlgorithmParamDTO param) {
        log.error("Invoke server failed........ params:{}", JSON.toJSONString(param));
        return null;
    }
}
