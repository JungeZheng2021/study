package com.aimsphm.nuclear.common.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 用于配置rabbitMQ
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.rabbitmqconfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class RabbitmqConfig {


    /*
    定义连接，我方rabbitMQ
     */
    @Bean(name = "myConnectionFactory")
    @Primary
    public ConnectionFactory myConnectionFactory(
            @Value("${spring.rabbitmq.first.host}") String host,
            @Value("${spring.rabbitmq.first.port}") int port,
            @Value("${spring.rabbitmq.first.username}") String username,
            @Value("${spring.rabbitmq.first.password}") String password
    ) {
        return connectionFactory(host, port, username, password);
    }

    /*
    定义连接
     */
    @Bean(name = "secondConnectionFactory")

    public ConnectionFactory daHuaConnectionFactory(
            @Value("${spring.rabbitmq.second.host}") String host,
            @Value("${spring.rabbitmq.second.port}") int port,
            @Value("${spring.rabbitmq.second.username}") String username,
            @Value("${spring.rabbitmq.second.password}") String password
    ) {
        return connectionFactory(host, port, username, password);
    }


    public CachingConnectionFactory connectionFactory(String host, int port, String username, String password) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean(name = "myRabbitTemplate")
    @Primary
    public RabbitTemplate myRabbitTemplate(
            @Qualifier("myConnectionFactory") ConnectionFactory connectionFactory
    ) {
        RabbitTemplate myRabbitTemplate = new RabbitTemplate(connectionFactory);
        myRabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return myRabbitTemplate;
    }

    @Bean(name = "secondRabbitTemplate")
    public RabbitTemplate secondRabbitTemplate(
            @Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory
    ) {
        RabbitTemplate daHuaRabbitTemplate = new RabbitTemplate(connectionFactory);
        daHuaRabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return daHuaRabbitTemplate;
    }


    @Bean(name = "myFactory")
    @Primary
    public SimpleRabbitListenerContainerFactory myFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("myConnectionFactory") ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean(name = "secondFactory")
    public SimpleRabbitListenerContainerFactory secondFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("secondConnectionFactory") ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}

