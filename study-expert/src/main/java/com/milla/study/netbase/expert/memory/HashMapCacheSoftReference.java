package com.milla.study.netbase.expert.memory;


import lombok.Data;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author milla
 */
public class HashMapCacheSoftReference {
    private volatile Map<String, SoftReference<CacheObject>> data = new ConcurrentHashMap<>(16);

    public static void main(String[] args) throws InterruptedException {
        HashMapCacheSoftReference cache = new HashMapCacheSoftReference();
        System.out.println(cache.get("BBB"));
        Thread.sleep(5001);
        System.out.println(cache.get("BBB"));

//        Map<String, String> data = new HashMap<>(16);
//
//        data.put("AA", "AA");
//        data.put("BB", "BB");
//        data.entrySet().removeIf(o -> o.getKey().equals("AA"));
//        System.out.println(data);


    }

    public HashMapCacheSoftReference() {
        init();
    }

    private void init() {
        ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(5);

        scheduled.scheduleWithFixedDelay(() -> {
            data.entrySet().removeIf(o -> Optional.ofNullable(o.getValue()).map(SoftReference::get).map(CacheObject::isExpire).orElse(false));
        }, 100, 100, TimeUnit.MILLISECONDS);
    }


    public boolean add(String key, Object value, long expire) {
        if (key == null) {
            return false;
        }
        if (Objects.isNull(value)) {
            return false;
        }
        CacheObject cacheObject = new CacheObject(value, expire + System.currentTimeMillis());
        data.put(key, new SoftReference<>(cacheObject));
        return true;
    }

    public Object get(String key) {
        SoftReference<CacheObject> cacheObjectSoftReference = data.get(key);
        if (Objects.isNull(cacheObjectSoftReference)) {
            return null;
        }
        CacheObject cacheObject = cacheObjectSoftReference.get();
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
