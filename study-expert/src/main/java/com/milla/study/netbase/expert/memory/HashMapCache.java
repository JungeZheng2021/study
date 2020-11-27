package com.milla.study.netbase.expert.memory;


import lombok.Data;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.milla.study.netbase.expert.memory
 * @Description: <HashMapCache缓存>
 * @Author: milla
 * @CreateDate: 2020/07/10 15:32
 * @UpdateUser: milla
 * @UpdateDate: 2020/07/10 15:32
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class HashMapCache {
    private volatile Map<String, CacheObject> data = new ConcurrentHashMap<>(16);

    public static void main(String[] args) throws InterruptedException {
        HashMapCache cache = new HashMapCache();
        cache.add("AAA", "AAA", 2000);
        cache.add("BBB", "BBB", 2000);
        cache.add("CCC", "CCC", 2000);
        cache.add("DDD", "DDD", 2000);
        System.out.println(cache.get("BBB"));
        Thread.sleep(5001);
        System.out.println(cache.get("BBB"));
    }

    public HashMapCache() {
        init();
    }

    /**
     * 定时任务删除过期的数据
     */
    private void init() {
        ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(5);

        scheduled.scheduleWithFixedDelay(() -> {
            data.entrySet().removeIf(entry -> Objects.nonNull(entry) && entry.getValue().isExpire());
        }, 100, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * @param key    要存储的key
     * @param value  要存储的值
     * @param expire 过期时间
     * @return
     */
    public boolean add(String key, Object value, long expire) {
        CacheObject cacheObject = new CacheObject(value, expire + System.currentTimeMillis());
        data.put(key, cacheObject);
        return true;
    }

    /**
     * 根据key获取值
     *
     * @param key 指定的key
     * @return
     */
    public Object get(String key) {
        CacheObject cacheObject = data.get(key);
        if (Objects.isNull(cacheObject)) {
            return null;
        }
        return cacheObject.getValue();
    }


    @Data
    class CacheObject {
        /**
         * 需要存储的值
         */
        private Object value;
        /**
         * dateline(存储的时候计算)
         */
        private long expire;

        public CacheObject() {
        }

        public boolean isExpire() {
            return System.currentTimeMillis() > expire;
        }

        public CacheObject(Object value, long expire) {
            this.value = value;
            this.expire = expire;
        }
    }
}
