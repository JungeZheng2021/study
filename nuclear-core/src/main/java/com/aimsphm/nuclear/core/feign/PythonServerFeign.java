package com.aimsphm.nuclear.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Package: com.aimsphm.nuclear.core.feign
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/10/20 13:16
 * @UpdateUser: milla
 * @UpdateDate: 2020/10/20 13:16
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@FeignClient(value = "TORNADO-EUREKA")
public interface PythonServerFeign {
    @PostMapping("health")
    Object health(@RequestBody Object o);
}
