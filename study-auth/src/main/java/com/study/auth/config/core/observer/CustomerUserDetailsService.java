package com.study.auth.config.core.observer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @Package: com.study.auth.config.core
 * @Description: <自定义用户处理类>
 * @Author: milla
 * @CreateDate: 2020/09/04 13:53
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/04 13:53
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Component
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private Subject login;

    //    @Autowired
//    private MailLoginObserver loginObserver;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //通知所有的观察者执行
        //        login.notifyObservers();
        //剔除出某个观察者
//        login.removeObserver(loginObserver);
//        return login.notifyObservers();
        //测试直接使用固定账户代替
        return User.withUsername("admin").password(passwordEncoder.encode("admin")).roles("admin", "user").build();
    }
}
