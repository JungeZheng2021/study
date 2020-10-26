package com.aimsphm.nuclear.common.redis;


import org.springframework.data.redis.connection.RedisClusterConnection;

/**
 * 
 */
public enum ConnectionLocal {
    INSTANCE;
    ThreadLocal<RedisClusterConnection> connection = new ThreadLocal<>();

    public RedisClusterConnection getInfo() {
        return connection.get();
    }

    public void setInfo(RedisClusterConnection param) {
        connection.set(param);
    }

    public void removeInfo(){
        connection.remove();
    }
}
