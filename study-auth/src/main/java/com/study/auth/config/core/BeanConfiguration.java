package com.study.auth.config.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Package: com.study.auth.config.core
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/09/08 11:28
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/08 11:28
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Configuration
public class BeanConfiguration {
    @Autowired
    private AuthenticationManager manager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
