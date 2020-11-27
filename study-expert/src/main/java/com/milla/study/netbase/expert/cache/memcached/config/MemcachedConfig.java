package com.milla.study.netbase.expert.cache.memcached.config;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @Package: com.milla.study.netbase.expert.config.cache.memcached
 * @Description: <单机集群等各种配置>
 * @Author: milla
 * @CreateDate: 2020/07/30 17:27
 * @UpdateUser: milla
 * @UpdateDate: 2020/07/30 17:27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Configuration
public class MemcachedConfig {
    @Bean
    @Qualifier("cluster")
    public MemcachedClient cluster() throws IOException {
        String servers = "192.168.16.37:11211 192.168.16.37:11212 192.168.16.37:11213";
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil
                .getAddresses(servers));
        // 默认的客户端计算就是 key的哈希值模以连接数
        // KetamaMemcachedSessionLocator 一致性hash算法
        builder.setSessionLocator(new KetamaMemcachedSessionLocator());
        MemcachedClient client = builder.build();
        return client;
    }

    @Bean
    @Qualifier("single")
    public MemcachedClient single() throws IOException {
        return new XMemcachedClient("192.168.16.37", 11211);
    }

    @Bean
    @Qualifier("proxy")
    public MemcachedClient proxy() throws IOException {
        return new XMemcachedClient("192.168.16.37", 10010);
    }
}
