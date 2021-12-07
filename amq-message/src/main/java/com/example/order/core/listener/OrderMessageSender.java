package com.example.order.core.listener;

import com.example.order.core.config.QueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 功能描述: 消息发送
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/12/06 17:36
 */
@Slf4j
@Component
public class OrderMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送信息
     *
     * @param orderId    订单号
     * @param delayTimes 过期时间(毫秒)
     */
    public void sendMessage(Long orderId, final long delayTimes) {
        //给延迟队列发送消息
        rabbitTemplate.convertAndSend(QueueEnum.QUEUE_DELAY_ORDER.getExchange(), QueueEnum.QUEUE_DELAY_ORDER.getRouteKey()
                , orderId, message -> {
                    //给消息设置延迟毫秒值
                    message.getMessageProperties().setExpiration(String.valueOf(delayTimes));
                    return message;
                });
        log.info("send delay message orderId:{}", orderId);
    }
}

