package com.aimsphm.nuclear.opc.client;

import com.aimsphm.nuclear.opc.model.DataItem;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Package: com.aimphm.nuclear.opc.client
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/10/22 09:41
 * @UpdateUser: milla
 * @UpdateDate: 2020/10/22 09:41
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
public class MqPushClient {
    @Autowired
    private RabbitTemplate template;

    public void send2Mq(List<DataItem> dataItems, String queue) {
        this.template.convertAndSend("amq.topic", queue, JSON.toJSONString(dataItems));
    }

    public void send2Mq(String message, String queue) {
        this.template.convertAndSend("amq.topic", queue, message);
    }
}

