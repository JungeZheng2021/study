package com.study.cache.redis.a7_cluster;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.Arrays;

@Configuration
// 在cluster环境下生效
@Profile("a7_cluster")
class ClusterAppConfig {
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(Arrays.asList(
                "192.168.16.40:6381",
                "192.168.16.40:6382",
                "192.168.16.40:6383",
                "192.168.16.40:6384",
                "192.168.16.40:6385",
                "192.168.16.40:6386"
        ));
        // 自适应集群变化
        return new JedisConnectionFactory(redisClusterConfiguration);
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(Arrays.asList(
                "192.168.16.40:6381",
                "192.168.16.40:6382",
                "192.168.16.40:6383",
                "192.168.16.40:6384",
                "192.168.16.40:6385",
                "192.168.16.40:6386"
        ));
        // 自适应集群变化
        return new LettuceConnectionFactory(redisClusterConfiguration);
    }
}