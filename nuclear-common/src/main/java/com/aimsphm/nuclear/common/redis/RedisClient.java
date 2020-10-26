package com.aimsphm.nuclear.common.redis;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import redis.clients.jedis.JedisCluster;

@Component
public class RedisClient {
	Logger logger = LoggerFactory.getLogger(RedisClient.class);

	private volatile boolean isLocked = false;
	/**
	 * timeout, after timeout, the lock will be released
	 */
	private int expireMsecs = 1 * 60 * 1000; // 2 min
	public final static String COMMON_USERNAME = "common_username";

	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;

	private JedisCluster getRedis() {
		RedisClusterConnection redisConnection = jedisConnectionFactory.getClusterConnection();
		ConnectionLocal.INSTANCE.setInfo(redisConnection);
		return (JedisCluster) redisConnection.getNativeConnection();
	}

	private void close(){
		try{
			RedisClusterConnection clusterConnection=ConnectionLocal.INSTANCE.getInfo();
			if(null!=clusterConnection){
				clusterConnection.close();
			}
		}catch (Exception e){
			logger.error("RedisClient-close():{}", e);
		}
	}

	public void set(String key, String value) {
		JedisCluster jedis=getRedis();
		try {
			jedis.set(key, value);
		} finally {
			close();
		}
	}

	public String get(String key)  {
		JedisCluster jedis=getRedis();
		try {
			return jedis.get(key);
		} finally {
			close();
		}
	}

	/**
	 * 批量删除对应的value
	 * @param keys
	 */
	public void remove(final String... keys) {
		JedisCluster jedis=getRedis();
		try {
			for (String key : keys)
				jedis.del(key);
		} finally {
			close();
		}
	}

	/**
	 * 删除对应的value
	 * @param keys
	 */
	public void remove(final Set<String> keys) {
		JedisCluster jedis=getRedis();
		try {
			for (String key : keys) {
				jedis.del(key);
			}
		} finally {
			// 返还到连接池
			close();
		}
	}

	/**
	 * 删除对应的value
	 * @param key
	 */
	public void remove(final String key) {
		JedisCluster jedis=getRedis();
		try {
			if (exists(key)) {
				jedis.del(key);
			}
		} finally {
			// 返还到连接池
			close();
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		JedisCluster jedis=getRedis();
		try {

			return jedis.exists(key);
		} finally {
			// 返还到连接池
			close();
		}
	}

	/**
	 * 写入缓存
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean set(final String key, Object value) throws Exception {
		JedisCluster jedis=getRedis();
		try {

			jedis.set(key, value.toString());
		} finally {
			// 返还到连接池
			close();
		}
		return false;
	}

	/**
	 * 写入缓存
	 * @param key
	 * @param value
	 * @param expireTime
	 * @return
	 * @throws Exception
	 */
	public boolean set(final String key, String value, int expireTime) throws Exception {
		JedisCluster jedis=getRedis();
		try {

			jedis.setex(key, expireTime, value);
		} finally {
			// 返还到连接池
			close();
		}
		return false;
	}

	/**
	 * 写入缓存Map
	 * @param key
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public boolean mSet(final String key, Map<String, String> map) throws Exception {
		JedisCluster jedis=getRedis();
		try {

			jedis.hmset(key, map);
		} finally {
			// 返还到连接池
			close();
		}
		return false;
	}

	/**
	 * 写入缓存Map 设置过期时间
	 * @param key
	 * @param map
	 * @param validtime
	 * @return
	 * @throws Exception
	 */
	public boolean mSet(final String key, Map<String, String> map, int validtime) throws Exception {
		JedisCluster jedis=getRedis();
		try {

			jedis.hmset(key, map);
			jedis.expire(key, validtime);
		} finally {
			// 返还到连接池
			close();
		}
		return false;
	}

	/**
	 * 查询缓存Map
	 * @param key
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public List<String> mGet(final String key, String field) throws Exception {
		JedisCluster jedis=getRedis();
		try {

			return jedis.hmget(key, field);
		} finally {
			// 返还到连接池
			close();
		}
	}
	public boolean setNX(final String key, final String value) throws Exception {

		JedisCluster jedis = null;
		try {
			jedis = getRedis();
			return jedis.setnx(key, value) == 1 ? true : false;
		} finally {
			// 返还到连接池
			close();
		}

	}
	/**
	 * setnx
	 * @param key
	 * @param value
	 * @return
	 */
	public Long setnx(final String key, final String value) {
		JedisCluster jedis=getRedis();
		try {

			return jedis.setnx(key, value);
		} finally {
			// 返还到连接池
			close();
		}
	}

	/**
	 * 获取剩余存活时间（秒）
	 * @param key
	 * @return
	 */
	public Long getTtl(final String key) {
		JedisCluster jedis=getRedis();
		try {

			return jedis.ttl(key);
		} finally {
			// 返还到连接池
			close();
		}
	}
	/**
	 * 封装和jedis方法
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public String getSet(final String key, final String value) throws Exception {
		JedisCluster jedis = null;
		try {
			jedis = getRedis();
			return jedis.getSet(key, value);
		} finally {
			// 返还到连接池
			close();
		}

	}
	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public String gSet(final String key, final String value) {
		JedisCluster jedis=getRedis();
		try {

			return jedis.getSet(key, value);
		} finally {
			// 返还到连接池
			close();
		}
	}

	/**
	 * 写入缓存bytes
	 * @param key
	 * @param t
	 * @param <T>
	 * @return
	 */
	public <T> boolean setObject(final String key, T object) {
		JedisCluster jedis=getRedis();
		try {

			jedis.set(key.getBytes(), SerializationUtils.serialize(object));
//			jedis.expire(key.getBytes(), getSecondsNextEarlyMorning());
		} finally {
			// 返还到连接池
			close();
		}
		return false;
	}
	/**
	 * 读取缓存对象
	 * @param key
	 * @param <T>
	 * @return
	 */
	public <T> T getObject(final String key) {
		JedisCluster jedis=getRedis();
		try {

			byte[] in = jedis.get(key.getBytes());
			return (T) SerializationUtils.deserialize(in);
		} finally {
			// 返还到连接池
			close();
		}
	}
	/**
	 * 写入缓存bytes
	 * @param key
	 * @param list
	 * @param <T>
	 * @return
	 */
	public <T> boolean setList(final String key, List<T> list) {
		JedisCluster jedis=getRedis();
		try {

			jedis.set(key.getBytes(), SerializationUtils.serialize(list));
			jedis.expire(key.getBytes(), getSecondsNextEarlyMorning());
		} finally {
			// 返还到连接池
			close();
		}
		return false;
	}

	/**
	 * 写入缓存Map
	 * @param key
	 * @param map
	 * @return
	 */
	public boolean setMap(final String key, Map map) {
		JedisCluster jedis=getRedis();
		try {

			jedis.set(key.getBytes(), SerializationUtils.serialize(map));
		} finally {
			// 返还到连接池
			close();
		}
		return false;
	}

	/**
	 * 读取缓存对象
	 * @param key
	 * @param <T>
	 * @return
	 */
	public <T> List<T> getList(final String key) {
		JedisCluster jedis=getRedis();
		try {

			byte[] in = jedis.get(key.getBytes());
			return (List<T>) SerializationUtils.deserialize(in);
		} finally {
			// 返还到连接池
			close();
		}
	}

	/**
	 * 读取Map
	 * @param key
	 * @return
	 */
	public Map getMap(final String key) {
		JedisCluster jedis=getRedis();
		try {

			byte[] in = jedis.get(key.getBytes());
			return (Map) SerializationUtils.deserialize(in);
		} finally {
			// 返还到连接池
			close();
		}
	}

	/**
	 * 删除
	 * @param key
	 */
	public void del(final String key) {
		JedisCluster jedis=getRedis();
		try {

			jedis.del(key.getBytes());
		} finally {
			// 返还到连接池
			close();
		}
	}

	/**
	 * 自增序列
	 * @param key
	 * @return
	 */
	public Long incrWithExpire(final String key) {
		JedisCluster jedis=getRedis();
		try {

			if (!jedis.exists(key)) {
				jedis.expire(key, getSecondsNextEarlyMorning());
			}
			return jedis.incr(key);
		} finally {
			// 返还到连接池
			close();
		}
	}

	/**
	 * 自增序列
	 * @param key
	 * @return
	 */
	public Long incrWithExpireTwoMonth(final String key) {
		JedisCluster jedis=getRedis();
		try {

			if (!jedis.exists(key)) {
				jedis.expire(key, getTwoMonthNextEarlyMorning());
			}
			return jedis.incr(key);
		} finally {
			// 返还到连接池
			close();
		}
	}

	/**
	 * 自增序列
	 * @param key
	 * @return
	 */
	public Long incr(final String key) {
		JedisCluster jedis=getRedis();
		try {

			return jedis.incr(key);
		} finally {
			// 返还到连接池
			close();
		}
	}

	// 获取当前时间到第二天凌晨的秒数
	private Integer getSecondsNextEarlyMorning() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);
		// 坑就在这里
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Long seconds = (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
		return seconds.intValue();
	}
	// 获取当前时间到第五天凌晨的秒数
	private Integer getTwoMonthNextEarlyMorning() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 60);
		// 坑就在这里
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Long seconds = (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
		return seconds.intValue();
	}

	/**
	 * 尝试获取分布式锁
	 * @param lockKey 锁
	 * @param requestId 请求标识
	 * @param expireTime 超期时间
	 * @return 是否获取成功
	 */
	public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
		JedisCluster jedis=getRedis();
		try {

			Long result = jedis.setnx(lockKey, requestId);
			if (1==result) {
				return true;
			}
		} finally {
			// 返还到连接池
			close();
		}
		return false;
	}

	private static final Long RELEASE_SUCCESS = 1L;

	/**
	 * 释放分布式锁
	 * @param lockKey 锁
	 * @param requestId 请求标识
	 * @return 是否释放成功
	 */
	public boolean releaseDistributedLock(String lockKey, String requestId) {
		JedisCluster jedis=getRedis();
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
		if (RELEASE_SUCCESS.equals(result)) {
			return true;
		}
		return false;
	}

	public String flush() {
		JedisCluster jedis=getRedis();
		try {

			return jedis.flushAll();
		} finally {
			// 返还到连接池
			close();
		}
	}
	/**
	 * 获取锁
	 *
	 * @return 获取锁成功返回ture，超时返回false
	 * @throws InterruptedException
	 */
	public synchronized boolean lock(String key) throws InterruptedException, Exception {
		// int timeout = timeoutMsecs;
		// while (timeout >= 0) {
		long expires = System.currentTimeMillis() + expireMsecs + 1;
		String expiresStr = String.valueOf(expires); // 锁到期时间
		if (this.setNX(key, expiresStr)) {
			isLocked = true;
			return true;
		}
		// redis里key的时间
		String currentValue = this.get(key);
		// 判断锁是否已经过期，过期则重新设置并获取
		if (currentValue != null && Long.parseLong(currentValue) < System.currentTimeMillis()) {
			// 设置锁并返回旧值
			String oldValue = this.getSet(key, expiresStr);
			// 比较锁的时间，如果不一致则可能是其他锁已经修改了值并获取
			if (oldValue != null && oldValue.equals(currentValue)) {
				isLocked = true;
				return true;
			}
		}
		// timeout -= DEFAULT_ACQUIRY_RETRY_MILLIS;
		// 延时
		// Thread.sleep(DEFAULT_ACQUIRY_RETRY_MILLIS);
		// }
		return false;
	}
	/**
	 * 释放获取到的锁
	 */
	public synchronized void unlock(String key) {
		/*
		 * if (isLocked) { redisTemplate.delete(lockKey); isLocked = false; }
		 */

		JedisCluster jedis = null;
		try {
			jedis = getRedis();
			jedis.del(key);
		} finally {
			// 返还到连接池
			close();
		}
		isLocked = false;
	}
}