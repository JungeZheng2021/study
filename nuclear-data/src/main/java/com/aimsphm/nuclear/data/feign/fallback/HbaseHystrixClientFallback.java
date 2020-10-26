package com.aimsphm.nuclear.data.feign.fallback;

import com.aimsphm.nuclear.common.entity.dto.HColumnItemDTO;
import com.aimsphm.nuclear.data.feign.HbaseServiceFeignClient;
import lombok.extern.slf4j.Slf4j;
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
public class HbaseHystrixClientFallback implements HbaseServiceFeignClient {

    @Override
    public void saveItemData2TableByHour(HColumnItemDTO itemDTO) {
        //TODO 服务调用失败处理逻辑
        System.out.println("...重试之后，仍然异常了.............................");
    }
}
