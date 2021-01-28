package com.aimsphm.nuclear.data.feign.fallback;

import com.aimsphm.nuclear.algorithm.entity.dto.AlgorithmParamDTO;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.aimsphm.nuclear.data.feign.DataServiceFeignClient;
import com.aimsphm.nuclear.data.feign.entity.dto.PublishParamDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

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
//@Slf4j
//@Component
//@ConditionalOnProperty(prefix = "spring.config", name = "enableDataService", havingValue = "true")
public class DataServiceFeignFallback {//implements DataServiceFeignClient {

    //    @Override
    public ResponseData<Boolean> dataServiceInvokeByParams(PublishParamDTO param) {
        System.out.println("--------------------------" + System.currentTimeMillis());
        return null;
    }
}
