package com.aimsphm.nuclear.common.annotation.aop;

import com.aimsphm.nuclear.common.annotation.LogAop;
import com.google.gson.Gson;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

@Aspect
@Component
public class LogAopAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAopAspect.class);


    @Pointcut("@annotation(com.aimsphm.nuclear.common.annotation.LogAop)")
    public void logPointcut() {
        log.debug("logPointcut");
    }

    @Autowired
    Gson gson;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Around("@annotation(logAop)")
    public Object logHandler(ProceedingJoinPoint process, LogAop logAop) throws Throwable {
        long startTime = System.currentTimeMillis();
        MethodSignature methodSignature = (MethodSignature) process.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        String className = method.getDeclaringClass().getName();
        Object[] args = process.getArgs();
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                params.append("参数" + i + ":");
                params.append(gson.toJson(args[i]));
                params.append(";");
            }
        }
        Object result = null;
        try {
            result = process.proceed();
        } catch (Throwable throwable) {
            String exception = throwable.getClass() + ":" + throwable.getMessage();
            long costTime = System.currentTimeMillis() - startTime;
            log.error("请求时间：{}，请求耗时：{}，请求类名：{}，请求方法：{}，请求参数:{}，请求结果：{}", formatDate(startTime), costTime + "毫秒",
                    className, methodName, params, exception);
            throw throwable;
        }
        long costTime = System.currentTimeMillis() - startTime;

        if (logAop.allowShowResult()) {
            log.info("请求参数:{}", params);
        }
        if (logAop.allowShowResult()) {
            log.info("结果:{}", formatJSON(result));
        }
        log.info("请求时间：{}，请求耗时：{}，请求类名：{}，请求方法：{}，请求结果：{}", formatDate(startTime), costTime + "毫秒", className,
                methodName, formatJSON(result));
        return result;
    }

    private String formatDate(long startTime) {
        return sdf.format(startTime);
    }

    private String formatJSON(Object result) {
        return gson.toJson(result);
    }
}
