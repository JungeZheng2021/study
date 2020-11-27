package com.study.cache.memcached.single;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Profile("single")
public class AppConfig {

    @Bean
    public MemcachedClient memcachedClient() throws IOException {
        return new XMemcachedClient("192.168.16.37", 11211);
    }
}
