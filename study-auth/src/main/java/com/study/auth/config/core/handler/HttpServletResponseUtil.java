package com.study.auth.config.core.handler;

import com.alibaba.fastjson.JSON;
import com.study.auth.comm.ResponseData;
import com.study.auth.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Package: com.study.auth.config.core.handler
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/09/08 17:45
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/08 17:45
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public final class HttpServletResponseUtil {

    public static void loginSuccess(HttpServletResponse resp) throws IOException {
        ResponseData success = ResponseData.success();
        success.setMsg("login success");
        response(resp, success);
    }

    public static void logoutSuccess(HttpServletResponse resp) throws IOException {
        ResponseData success = ResponseData.success();
        success.setMsg("logout success");
        response(resp, success);
    }

    public static void loginFailure(HttpServletResponse resp, AuthenticationException exception) throws IOException {
        log.debug("login failure ... ,{}", exception);
        ResponseData failure = ResponseData.error(CommonConstant.EX_RUN_TIME_EXCEPTION, exception.getMessage());
        response(resp, failure);
    }

    private static void response(HttpServletResponse resp, ResponseData data) throws IOException {
        //直接输出的时候还是需要使用UTF-8字符集
        resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = resp.getWriter();
        out.write(JSON.toJSONString(data));
        out.flush();
    }
}
