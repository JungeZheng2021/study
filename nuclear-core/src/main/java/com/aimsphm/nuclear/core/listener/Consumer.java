package com.aimsphm.nuclear.core.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.aimsphm.nuclear.common.response.ReturnResponse;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Consumer {
 
	@Autowired
	Gson gson;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(), //注意这里不要定义队列名称,系统会随机产生
            exchange = @Exchange(value = "test",type = ExchangeTypes.TOPIC),key = "test.test1"
    ),containerFactory="myFactory"
    )

    public void process( @Payload ReturnResponse payload) {
        log.info("receive:{}",payload);
        System.out.println(gson.toJson(payload));
    }
}
