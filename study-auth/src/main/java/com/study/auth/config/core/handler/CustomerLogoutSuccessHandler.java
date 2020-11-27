package com.study.auth.config.core.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Package: com.study.auth.config.core.handler
 * @Description: <登出成功>
 * @Author: milla
 * @CreateDate: 2020/09/08 17:44
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/08 17:44
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
public class CustomerLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpServletResponseUtil.logoutSuccess(response);
    }
}
