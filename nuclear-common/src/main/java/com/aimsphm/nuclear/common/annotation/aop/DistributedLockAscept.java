package com.aimsphm.nuclear.common.annotation.aop;

import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.redis.RedisClient;
import com.aimsphm.nuclear.common.redis.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;

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
        log.debug("distributedLockPointcut");
    }


    @Around("distributedLockPointcut()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        DistributedLock annotation = method.getAnnotation(DistributedLock.class);
        String value = annotation.value();
        String key = null;
        try {
            key = lock.tryLock(value, 3000);
            if (StringUtils.hasText(key)) {
                joinPoint.proceed();
                return;
            }
            log.error("current lock:{} exits", value);
            log.error("current lock:{},methodName:{}", value, method.getName());
        } finally {
            if (StringUtils.hasText(key)) {
                lock.unlock(value, key);
            }
        }
    }
}
