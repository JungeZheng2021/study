package com.milla.study.netbase.expert.cache.memcached.controller;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeoutException;

/**
 * @Package: com.milla.study.netbase.expert.cache.memcached.controller
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/07/30 17:31
 * @UpdateUser: milla
 * @UpdateDate: 2020/07/30 17:31
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
public class MemcachedController {
    @Autowired
    @Qualifier("single")
    MemcachedClient single;
    @Autowired
    @Qualifier("cluster")
    MemcachedClient cluster;
    @Autowired
    @Qualifier("proxy")
    MemcachedClient proxy;

    @GetMapping("single/{key}")
    public Object single(@PathVariable String key) throws InterruptedException, MemcachedException, TimeoutException {
        return single.get(key);
    }

    @GetMapping("single/{key}/{value}")
    public Object singleAdd(@PathVariable String key, @PathVariable String value) throws InterruptedException, MemcachedException, TimeoutException {
        return single.set(key, 0, value);
    }

    @GetMapping("cluster/{key}")
    public Object cluster(@PathVariable String key) throws InterruptedException, MemcachedException, TimeoutException {
        return cluster.get(key);
    }

    @GetMapping("cluster/{key}/{value}")
    public Object clusterAdd(@PathVariable String key, @PathVariable String value) throws InterruptedException, MemcachedException, TimeoutException {
        return cluster.set(key, 0, value);
    }

    @GetMapping("proxy/{key}")
    public Object proxy(@PathVariable String key) throws InterruptedException, MemcachedException, TimeoutException {
        return proxy.get(key);
    }

    @GetMapping("proxy/{key}/{value}")
    public Object proxyAdd(@PathVariable String key, @PathVariable String value) throws InterruptedException, MemcachedException, TimeoutException {
        return proxy.set(key, 0, value);
    }
}