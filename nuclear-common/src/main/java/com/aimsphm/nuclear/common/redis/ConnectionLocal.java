package com.aimsphm.nuclear.common.redis;


import org.springframework.data.redis.connection.RedisClusterConnection;

/**
 * <p>
 * 功能描述:暂时没用
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum ConnectionLocal {
    INSTANCE;
    ThreadLocal<RedisClusterConnection> connection = new ThreadLocal<>();

    public RedisClusterConnection getInfo() {
        return connection.get();
    }

    public void info(RedisClusterConnection param) {
        connection.set(param);
    }

    public void removeInfo() {
        connection.remove();
    }
}
