package com.aimsphm.nuclear.common.filter;

import com.aimsphm.nuclear.common.constant.AuthConstant;
import com.aimsphm.nuclear.common.entity.SysLog;
import com.aimsphm.nuclear.common.mapper.SysLogMapper;
import com.aimsphm.nuclear.common.token.TokenAuthenticationService;
import com.google.gson.Gson;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Aspect
@Component
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class SystemSettingLogAscept {
    @Autowired
    SysLogMapper sysLogMapper;

    @Pointcut("execution(* com.aimsphm.nuclear.*.service.*.*(..))" +
            "&& !execution(* com.aimsphm.nuclear.*.service.*.get*(..))" +
            "&& !execution(* com.aimsphm.nuclear.*.service.*.list*(..))" +
            "&& !execution(* com.aimsphm.nuclear.*.service.*.find*(..))" +
            "&& !execution(* com.aimsphm.nuclear.*.service.*.get*(..))" +
            "&& !execution(* com.aimsphm.nuclear.*.service.*.search*(..))" +
            "&& !execution(* com.aimsphm.nuclear.*.service.*.page*(..))")
    public void daoChange() {
    }

    @Before("daoChange()")
    public void changeBefore(JoinPoint joinPoint) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (!ObjectUtils.isEmpty(attributes)) {
            HttpServletRequest req = attributes.getRequest();
            if (getMethod(req.getMethod())) {
                SysLog sli = new SysLog();
                Gson gson = new Gson();
                sli.setIp(getTrueRemoteIP(req));
                sli.setMethodType(req.getMethod());
                sli.setServerIp(req.getServerName());
                sli.setUrl(req.getRequestURI());
                sli.setMethod(joinPoint.getSignature().getName());
                if(sli.getMethod().equals("callAlgo"))
                {
                    //will check call algo in log , the param's size is huge
                    return;
                }
                sli.setMethodCnType(transformCnWord(joinPoint.getSignature().getName()));
                sli.setOperation(transformWord(joinPoint.getSignature().getName()));
                sli.setUserName(!ObjectUtils.isEmpty(attributes) ? getUserName(attributes) : "background-job");
                String params = gson.toJson((joinPoint.getArgs())[0]);
                if (params.length() > 5000) {
                    sli.setParams(params.substring(0, 5000));
                } else {
                    sli.setParams(params);
                }
                sli.setTime(new Date());
                sysLogMapper.insert(sli);
            }
        }
    }

    public boolean getMethod(String req) {
        return req.equals("POST") ||
                req.equals("PUT") ||
                req.equals("DELETE");
    }

    public String getUserName(ServletRequestAttributes attributes) {
        String userName = "";
        String authorization = attributes.getRequest().getHeader(AuthConstant.AUTH_HEADER_STRING);
        if (authorization == null) {
            authorization = "";
            userName = "backGround-job";
        } else {
            if (attributes.getRequest() != null) {
                userName = TokenAuthenticationService.getUserName(attributes.getRequest());
            }
        }
        return userName;
    }

    public String transformWord(String methodName) {
        Map<String, String> map = new HashMap<>();
        map.put("^analyse.*", "分析");
        map.put("^add.*", "添加");
        map.put("^modify.*", "修改");
        map.put("^del.*", "删除");
        map.put("^insert.*", "插入");
        map.put("^update.*", "更新");
        map.put("^remove.*", "删除");
        map.put("\\w+Id.*", "信息");
        map.put("\\w+Case.*", "案例");
        map.put("^disable.*", "停止");
        map.put("^enable.*", "开启");
        map.put("^reset.*", "重置");
        map.put("\\w+Alarm.*", "报警");
        map.put("^callAlgo.*", "算法调用");
        StringBuilder sbr = new StringBuilder("");
        map.forEach((k, v) -> {
            if (Pattern.matches(k, methodName)) sbr.append(v);
        });
        return sbr.toString();
    }

    public String transformCnWord(String methodName) {
        Map<String, String> map = new HashMap<>();
        map.put("^add.*", "新增");
        map.put("^save.*", "新增");
        map.put("^insert.*", "新增");
        map.put("^modify.*", "修改");
        map.put("^update.*", "修改");
        map.put("^disable.*", "修改");
        map.put("^enable.*", "修改");
        map.put("^reset.*", "修改");
        map.put("^del.*", "删除");
        map.put("^callAlgo.*", "算法调用");
        StringBuilder sbr = new StringBuilder("");
        map.forEach((k, v) -> {
            if (Pattern.matches(k, methodName)) sbr.append(v);
        });
        if (sbr.length() == 0) sbr.append("其它");
        return sbr.toString();
    }

    public String getTrueRemoteIP(HttpServletRequest request) {
        return ObjectUtils.isEmpty(request.getHeader("X-Forwarded-For"))?request.getRemoteAddr():request.getHeader("X-Forwarded-For");
    }

}
