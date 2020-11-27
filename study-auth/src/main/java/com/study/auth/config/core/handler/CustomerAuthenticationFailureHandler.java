package com.study.auth.config.core.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Package: com.study.auth.config.core.handler
 * @Description: <登录失败操作类>
 * @Author: milla
 * @CreateDate: 2020/09/08 17:42
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/08 17:42
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
public class CustomerAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        HttpServletResponseUtil.loginFailure(response, exception);
    }
}
