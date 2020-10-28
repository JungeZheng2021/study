package com.aimsphm.nuclear.data.feign;

import com.aimsphm.nuclear.common.entity.dto.HBaseColumnItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Package: com.aimsphm.nuclear.data.feign
 * @Description: <服务调用>
 * @Author: MILLA
 * @CreateDate: 2020/3/31 15:22
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/31 15:22
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@FeignClient(value = "nuclear-hbase/hbase")//, fallback = HbaseHystrixClientFallback.class)
public interface HBaseServiceFeignClient {

    @PostMapping(value = "column/hour")
    void saveItemData2TableByHour(@RequestBody HBaseColumnItemDTO itemDTO);
}
