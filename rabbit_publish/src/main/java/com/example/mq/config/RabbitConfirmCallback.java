package com.example.mq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @Package: com.example.mq.config
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/08/05 09:46
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/05 09:46
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
public class RabbitConfirmCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private static final Logger log = LoggerFactory.getLogger(RabbitConfirmCallback.class);
    @Autowired
    private RabbitTemplate template;
    ThreadLocal<Boolean> isReturnCallback = new ThreadLocal();

    public RabbitConfirmCallback() {
    }

    @PostConstruct
    public void initRabbitTemplate() {
        this.template.setReturnCallback(this);
        this.template.setConfirmCallback(this);
    }

    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
        log.info("3.2.ack :[{}],如果是true-->发送成功,cause:{},correlationData:{}", new Object[]{ack, cause, correlationData});
        if (ack && Objects.isNull(this.isReturnCallback.get())) {
            log.info("4.修改推送记录表数据的状态");
        }

    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("3.1.失败回调 message:{}, replyCode: {} ,replyText :{} ,exchange: {} ,routingKey: {}", new Object[]{new String(message.getBody()), replyCode, replyText, exchange, routingKey});
        this.isReturnCallback.set(true);
    }
}
