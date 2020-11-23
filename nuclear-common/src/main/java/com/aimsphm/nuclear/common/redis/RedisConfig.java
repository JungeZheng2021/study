package com.aimsphm.nuclear.common.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Package: com.aimsphm.nuclear.common.redis
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/11/21 16:19
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/21 16:19
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.config", name = "enableRedis", havingValue = "true")
public class RedisConfig {
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }
}
