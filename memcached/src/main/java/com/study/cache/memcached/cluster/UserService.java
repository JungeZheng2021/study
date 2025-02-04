package com.study.cache.memcached.cluster;

import com.study.cache.memcached.pojo.User;
import net.rubyeye.xmemcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * 使用xmemcahcedclient自带方案
 */
@Service
@Profile("cluster")
public class UserService {

    @Autowired
    MemcachedClient memcachedClient;

    /**
     * 带缓存
     */
    public User findUser(String userId) throws Exception {
        // 每次根据情况进行选择
        User user = null;
        // 1、 判定缓存中是否存在
        user = memcachedClient.get(userId);
        if (user != null) {
            System.out.println("从缓存中读取到值：" + user);
            return user;
        }

        // TODO 2、不存在则读取数据库或者其他地方的值
        user = new User(userId, "张三");
        System.out.println("从数据库中读取到值：" + user);
        // 3、 同步存储value到memcached，缓存超时为1小时，3600秒。
        memcachedClient.set(userId, 3600, user);
        return user;
    }
}
