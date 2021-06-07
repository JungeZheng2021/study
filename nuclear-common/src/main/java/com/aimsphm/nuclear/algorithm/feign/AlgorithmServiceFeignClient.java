package com.aimsphm.nuclear.algorithm.feign;

import com.aimsphm.nuclear.algorithm.entity.dto.AlgorithmParamDTO;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.baomidou.mybatisplus.extension.api.R;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Package: com.aimsphm.nuclear.history
 * @Description: <算法调用服务类>
 * @Author: MILLA
 * @CreateDate: 2020/6/28 10:54
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/28 10:54
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@FeignClient(name = "algorithm-server-cargod")//, fallback = AlgorithmServiceFeignFallback.class)
//@FeignClient(name = "algorithm-server")//, fallback = AlgorithmServiceFeignFallback.class)
@ConditionalOnProperty(prefix = "spring.config", name = "enableAlgorithm", havingValue = "true")
public interface AlgorithmServiceFeignClient {
    /**
     * 调用算法通用入口
     *
     * @param param Json参数
     * @return
     */
    @PostMapping("algorithm")
    <T> ResponseData algorithmInvokeByParams(@RequestBody AlgorithmParamDTO<T> param);
}