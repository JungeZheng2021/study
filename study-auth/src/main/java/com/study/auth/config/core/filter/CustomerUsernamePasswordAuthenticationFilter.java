package com.study.auth.config.core.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.auth.config.core.util.AuthenticationStoreUtil;
import com.study.auth.entity.bo.LoginBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Package: com.study.auth.config.core.filter
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/09/11 16:04
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/11 16:04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class CustomerUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * 空字符串
     */
    private final String EMPTY = "";


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //如果不是json使用自带的过滤器获取参数
        if (!MediaType.APPLICATION_JSON_UTF8_VALUE.equals(request.getContentType()) && !MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            storeAuthentication(username, password);
            Authentication authentication = super.attemptAuthentication(request, response);
            return authentication;
        }

        //如果是json请求使用取参数逻辑
        ObjectMapper mapper = new ObjectMapper();
        UsernamePasswordAuthenticationToken authRequest = null;
        try (InputStream is = request.getInputStream()) {
            LoginBO account = mapper.readValue(is, LoginBO.class);
            storeAuthentication(account.getUsername(), account.getPassword());
            authRequest = new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword());
        } catch (IOException e) {
            log.error("验证失败：{}", e);
            authRequest = new UsernamePasswordAuthenticationToken(EMPTY, EMPTY);
        } finally {
            setDetails(request, authRequest);
            Authentication authenticate = this.getAuthenticationManager().authenticate(authRequest);
            return authenticate;
        }
    }

    /**
     * 保存用户名和密码
     *
     * @param username 帐号/邮箱/手机号
     * @param password 密码/验证码
     */
    private void storeAuthentication(String username, String password) {
        AuthenticationStoreUtil.setUsername(username);
        AuthenticationStoreUtil.setPassword(password);
    }
}
