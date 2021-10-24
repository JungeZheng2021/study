package com.aimsphm.nuclear.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 功能描述:动态表格存储类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/01/18 17:33
 */
@Slf4j
@Component
public class RedisLock {

    /**
     * 解锁脚本，原子操作
     */
    private static final String UN_LOCK_SCRIPT =
            "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n"
                    + "then\n"
                    + "    return redis.call(\"del\",KEYS[1])\n"
                    + "else\n"
                    + "    return 0\n"
                    + "end";

    private StringRedisTemplate redisTemplate;

    public RedisLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 加锁，有阻塞
     *
     * @param name
     * @param expire
     * @param timeout
     * @return
     */
    public String lock(String name, long expire, long timeout) {
        long startTime = System.currentTimeMillis();
        String token;
        do {
            token = tryLock(name, expire);
            if (token == null) {
                if ((System.currentTimeMillis() - startTime) > (timeout - 50)) {
                    break;
                }
                try {
                    //try 50 per sec
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("{}", e);
                    return null;
                }
            }
        } while (token == null);

        return token;
    }

    /**
     * 加锁，无阻塞
     *
     * @param name
     * @param expire
     * @return
     */
    public String tryLock(String name, long expire) {
        String token = UUID.randomUUID().toString();
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection conn = factory.getConnection();
        try {
            Boolean result = conn.set(name.getBytes(StandardCharsets.UTF_8), token.getBytes(StandardCharsets.UTF_8),
                    Expiration.from(expire, TimeUnit.MILLISECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT);
            if (result != null && result) {
                return token;
            }
        } finally {
            RedisConnectionUtils.releaseConnection(conn, factory, false);
        }
        return null;
    }

    /**
     * 解锁
     *
     * @param name
     * @param token
     * @return
     */
    public boolean unlock(String name, String token) {
        byte[][] keysAndArgs = new byte[2][];
        keysAndArgs[0] = name.getBytes(StandardCharsets.UTF_8);
        keysAndArgs[1] = token.getBytes(StandardCharsets.UTF_8);
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        if (Objects.isNull(factory)) {
            return false;
        }
        RedisConnection conn = factory.getConnection();
        try {
            Long result = conn.scriptingCommands().eval(UN_LOCK_SCRIPT.getBytes(StandardCharsets.UTF_8), ReturnType.INTEGER, 1, keysAndArgs);
            if (result != null && result > 0) {
                return true;
            }
        } finally {
            RedisConnectionUtils.releaseConnection(conn, factory, false);
        }
        return false;
    }
}

