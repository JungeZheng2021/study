package com.aimsphm.nuclear.data.feign.fallback;

import com.aimsphm.nuclear.common.response.ResponseData;
import com.aimsphm.nuclear.data.feign.entity.dto.PublishParamDTO;
import lombok.extern.slf4j.Slf4j;

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
public class DataServiceFeignFallback {//implements DataServiceFeignClient {

    //    @Override
    public ResponseData<Boolean> dataServiceInvokeByParams(PublishParamDTO param) {
        log.error("---------------------{}-----{}:", System.currentTimeMillis(), param);
        return null;
    }
}
