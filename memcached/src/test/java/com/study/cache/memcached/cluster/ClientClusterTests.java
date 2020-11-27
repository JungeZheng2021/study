package com.study.cache.memcached.cluster;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("cluster") // 设置profile
public class ClientClusterTests {

    @Autowired
    UserServiceByCustom userServiceByCustom;

    @Test
    public void customClientClusterTest() throws Exception {
        userServiceByCustom.findUser("hello");
    }


    @Autowired
    UserService userService;

    @Test
    public void xmemcachedclientClusterTest() throws Exception {
        // 每次都向上面这么写一段获取client的代码，太麻烦，memcacheclient已经内置了这样的功能
        // 在调用set和其他方法时，自动帮我们进行选择
        userService.findUser("tony33");
        userService.findUser("mengmeng33");
        userService.findUser("jack33");
        userService.findUser("tony44");
        userService.findUser("mengmeng44");
        userService.findUser("jack44");

    }

    // 使用推特中间件代码memcached
    @Test
    public void twemproxyClusterTest() throws IOException, InterruptedException, MemcachedException, TimeoutException {
        // 和代理直接连接即可，客户端无感知
        XMemcachedClient xMemcachedClient = new XMemcachedClient("192.168.16.37", 10010);
        xMemcachedClient.set("uid10001", 0, "{uname:tony,age:18}");
    }
}
