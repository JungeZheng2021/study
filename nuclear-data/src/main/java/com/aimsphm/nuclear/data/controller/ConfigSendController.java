package com.aimsphm.nuclear.data.controller;

import com.aimsphm.nuclear.data.feign.entity.dto.ConfigSettingsPacketDTO;
import com.aimsphm.nuclear.data.feign.entity.dto.MqConfigDTO;
import com.aimsphm.nuclear.data.feign.entity.dto.PublishParamDTO;
import com.aimsphm.nuclear.data.util.MqPushClient;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Package: com.aimsphm.nuclear.data.controller
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2021/01/22 16:50
 * @UpdateUser: MILLA
 * @UpdateDate: 2021/01/22 16:50
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@RestController
@Api(tags = "配置信息下发操作-算法配置-相关接口")
@RequestMapping(value = "config", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfigSendController {

    @Resource
    private MqPushClient client;

    @PostMapping("settings")
    Boolean dataServiceInvokeByParams(@RequestBody PublishParamDTO param) {
        log.info("接收到配置信息，开始下发配置");
        try {
            MqConfigDTO config = param.getConfig();
            ConfigSettingsPacketDTO<ConfigSettingsPacketDTO> settings = param.getSettings();
            client.send2Mq(config.getExchange(), config.getRoutingKey(), settings);
            log.info("发送成功,配置信息为:{}", settings);
            return true;
        } catch (Exception e) {
            log.error("发送失败:{}", e);
            return false;
        }
    }
}
