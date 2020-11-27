package com.study.cache.redis.a6_sentinel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
class SentinelRedisAppConfig {
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        System.out.println("使用哨兵版本");


        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master("mymaster")
                // 哨兵地址
                .sentinel("192.168.16.40", 26380)
                .sentinel("192.168.16.40", 26381)
                .sentinel("192.168.16.40", 26381);
        return new LettuceConnectionFactory(sentinelConfig);
    }
}