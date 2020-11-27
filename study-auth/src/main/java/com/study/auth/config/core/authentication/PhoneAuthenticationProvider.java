package com.study.auth.config.core.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * @Package: com.study.auth.config.core.authentication
 * @Description: <手机验证码登录>
 * @Author: milla
 * @CreateDate: 2020/09/08 19:21
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/08 19:21
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
@Slf4j
public class PhoneAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("手机验证码验证：", System.currentTimeMillis());
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
