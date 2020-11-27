package com.milla.study.netbase.expert.memory;

import java.util.concurrent.ExecutionException;

/**
 * @Package: com.milla.study.netbase.expert.memory
 * @Description: <guava缓存>
 * @Author: milla
 * @CreateDate: 2020/07/10 15:32
 * @UpdateUser: milla
 * @UpdateDate: 2020/07/10 15:32
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class GuavaDemo {
    public static void main(String[] args) throws ExecutionException {
//        CacheBuilder<Object, Object> build = CacheBuilder.newBuilder();
//        //并发级别
//        LoadingCache<Object, Object> cache = build.concurrencyLevel(8)
//                //设置过期时间
//                .expireAfterWrite(8, TimeUnit.SECONDS)
//                //写缓存之后刷新
//                .refreshAfterWrite(1, TimeUnit.SECONDS)
//                //初始化容量
//                .initialCapacity(10)
//                //删除监听事件
//                .removalListener(notification -> System.out.println("我已经被删除了"))
//                //统计命中率
//                .recordStats()
//                //最大容量是100
//                .maximumSize(100)
//                //如果没有数据进行数据的获取等
//                .build(new CacheLoader<Object, Object>() {
//                    @Override
//                    public Object load(Object key) throws Exception {
//                        System.out.println("缓存没有，从数据库中加载：" + key);
//                        return UUID.randomUUID().toString();
//                    }
//                });
//
//        Object o = cache.get("1111");
//        System.out.println(o);
//        System.out.println(cache.get("1111"));
//        //命中率
//        System.out.println(cache.stats().toString());


    }
}