package com.aimsphm.nuclear.common.annotation.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.redis.RedisClient;

@Aspect
@Component
public class DistributedLockAscept {
	private static final Logger log = LoggerFactory.getLogger(DistributedLockAscept.class);

	@Autowired
	RedisClient redisUtil;

	@Pointcut("@annotation(com.aimsphm.nuclear.common.annotation.DistributedLock)")
	public void distributedLockPointcut() {

	}

	
	
	@Around("distributedLockPointcut()")
	public void around(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature methodSignature =  (MethodSignature) joinPoint.getSignature();
	    Method method = methodSignature.getMethod();
	    DistributedLock annotation = method.getAnnotation(DistributedLock.class);
	    String value = annotation.value();
		boolean bool = false;
		try {
		 bool = redisUtil.lock(value);
		if (bool)
			joinPoint.proceed();
		else
			log.error("current lock:"+value+" exits");
		}finally {
			if(bool)
			{
				Thread.sleep(2000);
				redisUtil.unlock(value);
			}
		}
		
		
	}
}
