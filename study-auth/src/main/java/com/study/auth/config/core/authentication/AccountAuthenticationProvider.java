package com.study.auth.config.core.authentication;

import com.study.auth.config.core.observer.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @Package: com.study.auth.config.core
 * @Description: <用户名密码验证登录>
 * @Author: milla
 * @CreateDate: 2020/09/04 16:18
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/04 16:18
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
public class AccountAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerUserDetailsService myUserDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        Object objPassword = authentication.getCredentials();
        String password = objPassword == null ? "" : objPassword.toString();
        //模糊提示
        if (StringUtils.isEmpty(password)) {
            throw new BadCredentialsException("the password can not matches the account ");
        }

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("the account is not exist ");
        }
        //sso用户不校验密码
        if (StringUtils.isEmpty(password) || !passwordEncoder.matches(password, (userDetails.getPassword()))) {
            throw new BadCredentialsException("the password can not matches the account ");
        }
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
//        for (GrantedAuthority authority : authorities) {
//            // set authority and role
//            if (!ObjectUtils.isEmpty(authority)) {
//                authorities.add(authority);
//            }
//        }
        String authoritiesStr = authorities.stream().map(e -> e.getAuthority()).collect(Collectors.joining(","));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }

    /**
     * 需要设置成true才能走验证逻辑
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
