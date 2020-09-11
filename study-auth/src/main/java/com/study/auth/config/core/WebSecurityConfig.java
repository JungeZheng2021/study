package com.study.auth.config.core;

import com.study.auth.config.core.authentication.AccountAuthenticationProvider;
import com.study.auth.config.core.authentication.MailAuthenticationProvider;
import com.study.auth.config.core.authentication.PhoneAuthenticationProvider;
import com.study.auth.config.core.filter.CustomerUsernamePasswordAuthenticationFilter;
import com.study.auth.config.core.handler.CustomerAuthenticationFailureHandler;
import com.study.auth.config.core.handler.CustomerAuthenticationSuccessHandler;
import com.study.auth.config.core.handler.CustomerLogoutSuccessHandler;
import com.study.auth.config.core.observer.CustomerUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Package: com.study.auth.config
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/09/04 11:27
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/04 11:27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccountAuthenticationProvider provider;
    @Autowired
    private MailAuthenticationProvider mailProvider;
    @Autowired
    private PhoneAuthenticationProvider phoneProvider;
    @Autowired
    private CustomerUserDetailsService userDetailsService;
    @Autowired
    private CustomerAuthenticationSuccessHandler successHandler;
    @Autowired
    private CustomerAuthenticationFailureHandler failureHandler;
    @Autowired
    private CustomerLogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
/**        http.addFilterBefore(verifyCodeFilter, UsernamePasswordAuthenticationFilter.class);
 //开启登录配置
 */      //配置HTTP基本身份验证//使用自定义过滤器-兼容json和表单登录
        http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic()
                .and().authorizeRequests()
                //表示访问 /setting 这个接口，需要具备 admin 这个角色
                .antMatchers("/setting").hasRole("admin")
                //表示剩余的其他接口，登录之后就能访问
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                //定义登录页面，未登录时，访问一个需要登录之后才能访问的接口，会自动跳转到该页面
                .loginPage("/noToken")
                //登录处理接口-登录时候访问的接口地址
                .loginProcessingUrl("/account/login")
                //定义登录时,表单中用户名的 key，默认为 username
                .usernameParameter("username")
                //定义登录时,表单中用户密码的 key，默认为 password
                .passwordParameter("password")
                //登录成功的处理器
                .successHandler(successHandler)
                //登录失败的处理器
                .failureHandler(failureHandler)
                //允许所有用户访问
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                //登出成功的处理
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();
        //关闭csrf跨域攻击防御
        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //权限校验-只要有一个认证通过即认为是通过的(有一个认证通过就跳出认证循环)-适用于多登录方式的系统
//        auth.authenticationProvider(provider);
//        auth.authenticationProvider(mailProvider);
//        auth.authenticationProvider(phoneProvider);
        //直接使用userDetailsService
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //忽略拦截的接口
        web.ignoring().antMatchers("/noToken", "/vercode");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * 注册自定义的UsernamePasswordAuthenticationFilter
     *
     * @return
     * @throws Exception
     */
    @Bean
    public AbstractAuthenticationProcessingFilter customAuthenticationFilter() throws Exception {
        AbstractAuthenticationProcessingFilter filter = new CustomerUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        //过滤器拦截的url要和登录的url一致，否则不生效
        filter.setFilterProcessesUrl("/account/login");

        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }
}
