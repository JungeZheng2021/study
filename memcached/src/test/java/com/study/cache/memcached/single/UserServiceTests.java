package com.study.cache.memcached.single;

import com.study.cache.memcached.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
@ActiveProfiles("single") // 设置profile
public class UserServiceTests {
    @Autowired
    UserService userService;

    @Test
    public void setTest() {
        try {
            User user = userService.findUser("tom");
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
