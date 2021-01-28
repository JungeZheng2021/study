package com.aimsphm.nuclear.common.annotation.aop;

import java.lang.reflect.Method;

import com.aimsphm.nuclear.common.redis.RedisClient;
import com.aimsphm.nuclear.common.redis.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.aimsphm.nuclear.common.annotation.DistributedLock;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Aspect
@Component
@ConditionalOnProperty(prefix = "spring.config", name = "enableRedis", havingValue = "true")
public class DistributedLockAscept {
    private static final Logger log = LoggerFactory.getLogger(DistributedLockAscept.class);

    RedisClient client;

    public DistributedLockAscept(RedisClient client) {
        this.client = client;
    }

    @Resource
    RedisLock lock;


    @Pointcut("@annotation(com.aimsphm.nuclear.common.annotation.DistributedLock)")
    public void distributedLockPointcut() {

    }


    @Around("distributedLockPointcut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        DistributedLock annotation = method.getAnnotation(DistributedLock.class);
        String value = annotation.value();
        boolean bool = false;
        String key = null;
        try {
//            bool = client.lock(value);
//            if (bool) {
//                joinPoint.proceed();
//                return;
//            }
            key = lock.tryLock(value, 3000);
            if (StringUtils.hasText(key)) {
                joinPoint.proceed();
                return;
            }
            log.error("current lock:" + value + " exits");
            log.error("current lock:{},methodName:{}", value, method.getName());
        } finally {
//            if (bool) {
//                Thread.sleep(2000);
//                client.unlock(value);
//            }
            if (StringUtils.hasText(key)) {
                lock.unlock(value, key);
            }
        }
    }
}
