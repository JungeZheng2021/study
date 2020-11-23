package com.aimsphm.nuclear.opc.config.rabbit;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @Package: com.aimsphm.nuclear.data.config.mqtt
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/3/30 17:09
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/30 17:09
 * @UpdateRemark: <>
 * @Version: 1.0
 */
//@Component
//@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {
    void sendToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic);
}
