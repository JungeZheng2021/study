package com.aimsphm.nuclear.core.feign;

import com.aimsphm.nuclear.common.entity.bo.CustSensorConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(value = "nuclear-customized-sensor-data")
public interface CustomSensorDataServiceFeignClient {

    @PostMapping("setSensorParameter/setFwsParam")
    public Object setFwsParam(@RequestBody CustSensorConfig config);

    @PostMapping("setSensorParameter/setWifiParam")
    public Object setWifiParam(@RequestBody CustSensorConfig config);
}
