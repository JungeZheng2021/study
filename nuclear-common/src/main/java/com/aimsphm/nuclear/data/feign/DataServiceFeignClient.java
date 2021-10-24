package com.aimsphm.nuclear.data.feign;

import com.aimsphm.nuclear.common.response.ResponseData;
import com.aimsphm.nuclear.data.feign.entity.dto.ConfigSettingsCommandDTO;
import com.aimsphm.nuclear.data.feign.entity.dto.ConfigSettingsDTO;
import com.aimsphm.nuclear.data.feign.entity.dto.ConfigSettingsPacketDTO;
import com.aimsphm.nuclear.data.feign.entity.dto.PublishParamDTO;
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
@FeignClient(name = "nuclear-data")//, fallback = AlgorithmServiceFeignFallback.class)
@ConditionalOnProperty(prefix = "spring.config", name = "enableDataService", havingValue = "true")
public interface DataServiceFeignClient {
    /**
     * 调用算法通用入口
     *
     * @param param Json参数
     * @return
     */
    @PostMapping("config/settings")
    ResponseData<Boolean> dataServiceInvokeByParams(@RequestBody PublishParamDTO param);


    /**
     * 直接执行方法
     *
     * @param edgeCode
     * @param settingDetails
     * @return
     */
    default ResponseData<Boolean> invokeService(String edgeCode, ConfigSettingsDTO settingDetails) {
        PublishParamDTO param = new PublishParamDTO();
        ConfigSettingsPacketDTO<ConfigSettingsCommandDTO> settings = new ConfigSettingsPacketDTO();
        settings.setType(21);
        ConfigSettingsCommandDTO packet = new ConfigSettingsCommandDTO();
        packet.setEdgeCode(edgeCode);
        packet.setTimestamp(System.currentTimeMillis());
        packet.setConfigCommand(settingDetails);
        settings.setPacket(packet);
        param.setSettings(settings);
        return dataServiceInvokeByParams(param);
    }

}