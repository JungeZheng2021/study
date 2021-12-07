package com.example.order.core.config;

/**
 * <p>
 * 功能描述: 实现自动过期枚举类(两个交换器和队列)
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/12/06 17:26
 */
public enum QueueEnum {

    /**
     * 自动取消队列
     */
    QUEUE_AUTO_CANCEL("vv_x", "zz_test", "key"),
    /**
     * 延迟队列
     */
    QUEUE_DELAY_ORDER("vv_x_ttl", "zz_test_ttl", "key_ttl");

    QueueEnum(String exchange, String queue, String routeKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routeKey = routeKey;
    }

    public String getExchange() {
        return exchange;
    }

    public String getQueue() {
        return queue;
    }

    public String getRouteKey() {
        return routeKey;
    }

    private String exchange;
    public String queue;
    private String routeKey;
}
