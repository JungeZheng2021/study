package com.aimsphm.nuclear.common.redis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aimsphm.nuclear.common.exception.RedisCountLockException;



@Component
public class RedisCountLock {

	private Logger logger = LoggerFactory.getLogger(RedisCountLock.class);
	@Autowired
	private RedisClient redisUtil;

	private Map<String,Long> localCount = new HashMap<>();
	// private Long redisCount;
	private Date setRedisCountTimestamp;

	public Long getLocalCount(String key) {
		return localCount.get(key);
	}

	public void setLocalCount(String key,Long localCount) {
		this.localCount.put(key, localCount);
	}

	/*
	 * public Long getRedisCount() { return redisCount; }
	 * 
	 * public void setRedisCount(Long redisCount) { this.redisCount =
	 * redisCount; }
	 */

	public Date getSetRedisCountTimestamp() {
		return setRedisCountTimestamp;
	}

	public void setSetRedisCountTimestamp(Date setRedisCountTimestamp) {
		this.setRedisCountTimestamp = setRedisCountTimestamp;
	}

	public synchronized void syncRedisCount() {

	}

	public synchronized void initLockCount(String redisKey) throws Exception {
		if (!redisUtil.exists(redisKey)) {// 没有这个Key
			Boolean canSet = redisUtil.setNX(redisKey, String.valueOf(0L));
			if (!canSet) {// 另外的线程已经初始化了
//				localCount = Long.parseLong(redisUtil.get(redisKey));
				setLocalCount(redisKey,Long.parseLong(redisUtil.get(redisKey)));
				logger.info("local count is sync with redis");
			} else {
//				localCount = 0L;
				setLocalCount(redisKey,0L);
				logger.info("local count set to 0");
				
			}
		} else {// 已经有这个Key了，证明是程序重启了
//			localCount = Long.parseLong(redisUtil.get(redisKey));
			setLocalCount(redisKey,Long.parseLong(redisUtil.get(redisKey)));
			logger.info("local count is sync with redis");
		}
	}

	public synchronized Boolean lock(String redisKey) throws Exception {
		Boolean getLock = false;

		if (localCount == null) {
			initLockCount(redisKey);
			logger.info("local count is null, init the lock");
		}
		// if (localCount != null) {
		Long redisCount = Long.parseLong(redisUtil.get(redisKey));
		if (redisCount.longValue() == localCount.get(redisKey).longValue()) {
			logger.info("local count is equal with redis");
			redisUtil.set(redisKey, String.valueOf(redisCount + 1));
			Long currentRedisCount = Long.parseLong(redisUtil.get(redisKey));//在此过程中，别的进程中的线程可能会已经改变值，所以要再做判断
			if (currentRedisCount.longValue() - localCount.get(redisKey).longValue()==1) {
//				localCount = currentRedisCount;
				setLocalCount(redisKey,currentRedisCount);
				getLock = true;
				logger.info("get lock");
			} else {
//				localCount = currentRedisCount;
				setLocalCount(redisKey,currentRedisCount);
				getLock = false;
				logger.info("cann not get lock due to status conflict");
			}
		} else {// some other thread get the lock already
			if (redisCount.longValue() > localCount.get(redisKey).longValue()) {
				setLocalCount(redisKey,redisCount);
//				localCount = redisCount;
				getLock = false;
				logger.info("cann not get lock due to other already got");
			} else {
				if (redisCount.longValue() < localCount.get(redisKey).longValue()) {
					logger.error("redis count not in sync");
					throw new RedisCountLockException("redisCountNumber is smaller than local value, please check");
				}
			}
		}

		// }

		return getLock;
	}

}
