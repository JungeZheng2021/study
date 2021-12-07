package com.example.order.core.listener;

import com.example.order.core.service.ShopOrderOperateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/12/06 17:38
 */
@Slf4j
@Component
public class AutoCancelListener {
    @Autowired
    private ShopOrderOperateService operateService;

    @RabbitListener(queues = "zz_test")
    @RabbitHandler
    public void handle(Long orderId) {
        boolean b = operateService.cancelOrder(orderId);
        log.info("receive delay message orderId:{},and auto cancel flag:{}", orderId, b);
    }
}

