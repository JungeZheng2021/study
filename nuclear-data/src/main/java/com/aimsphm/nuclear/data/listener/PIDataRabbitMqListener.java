package com.aimsphm.nuclear.data.listener;

import com.aimsphm.nuclear.data.service.CommonDataService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Package: com.aimsphm.nuclear.data.config.rabbit
 * @Description: <rabbitMqPi数据消费者>
 * @Author: MILLA
 * @CreateDate: 2020/5/20 14:52
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/20 14:52
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Component
public class PIDataRabbitMqListener {
    @Resource
    @Qualifier("pi")
    private CommonDataService service;

    final LongAdder count = new LongAdder();

    private Long LOG_PRINT_TIME = 10000L;

    @RabbitListener(queues = "durableQueuePI")
    public void messageConsumer(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            if (count.longValue() % LOG_PRINT_TIME == 0) {
                log.info("data:{}", new String(message.getBody()));
            } else {
                log.info("接收到数据");
            }
            count.increment();
            service.operateData(message.getMessageProperties().getReceivedRoutingKey(), new String(message.getBody()));
        } catch (Exception e) {
//            产生异常
            log.error("consumer get a error :{}", e);
        }
    }
}