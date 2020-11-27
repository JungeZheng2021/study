package com.study.auth.controller;

import com.study.auth.exception.CustomMessageException;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Package: com.study.auth.controller
 * @Description: <配置中需要的控制类-需要给定指定的返回code用以判断是否有权限>
 * @Author: milla
 * @CreateDate: 2020/09/04 14:36
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/04 14:36
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class WebSecurityConfigController {
    @GetMapping(value = "noToken")
    public String noToken() {
        throw new CustomMessageException("noToken");
    }

    @GetMapping(value = "test")
    public String test() {
        throw new CustomMessageException("我是测试");
    }

    @GetMapping(value = "test1")
    @PreAuthorize("hasAuthority('admin')")
    public String test1() {
        return "I am just a test1";
    }

    @GetMapping(value = "test2")
    @PreAuthorize("hasAuthority('admin1')")
    public String test2() {
        return "I am just a test2";
    }

    @GetMapping(value = "test3")
    @PreAuthorize("hasRole('admin')")
    public String test3() {
        return "I am just a test3... 测试role";
    }

    @GetMapping(value = "test4")
    @PreAuthorize("hasPermission('admin')")
    public String test4() {
        return "I am just a test4... 测试hasPermission";
    }

    @GetMapping(value = "setting")
    public String setting() {
        return "I am just a setting";
    }
}
