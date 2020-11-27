package com.study.auth.config.core.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Package: com.study.auth.config.core.handler
 * @Description: <登录成功处理类>
 * @Author: milla
 * @CreateDate: 2020/09/08 17:39
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/08 17:39
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
public class CustomerAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpServletResponseUtil.loginSuccess(response);
    }
}
