package com.example.mq.util;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Package: com.example.mq.util
 * @Description: <缓存工具类>
 * @Author: MILLA
 * @CreateDate: 2020/6/1 14:19
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/1 14:19
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public abstract class CacheUtil<Q, R> {
    /**
     * 读写锁
     */
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * 读锁
     */
    ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    /**
     * 写锁
     */
    ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    /**
     * 缓存数据
     */
    final Map<String, Object> data = new ConcurrentHashMap<>();
    /**
     * 查询参数
     */
    protected Q params;

    /**
     * 从缓存中获取数据，如果不存在及从数据库或者其他地方获取数据，当前为模拟自动生成数据
     *
     * @param key
     * @return
     */
    public R cache(String key) {
        try {
            //加读锁-读锁属于共享锁可以支持多个线程获取锁，但此时排斥写锁
            readLock.lock();
            //如果有对应的key
            if (Objects.isNull(data.get(key))) {
                //解除读锁
                readLock.unlock();
                //添加写锁，保证写锁内操作只有一个线程能执行-避免该处过多线程操作导致崩溃或数据不一致
                writeLock.lock();
                try {
                    //双重判断，防止有其他线程已经处理，出现重复处理的情况
                    if (Objects.isNull(data.get(key))) {
                        if (Objects.nonNull(this.params)) {
                            R query = this.query(this.params);
                            data.put(key, query);
                        }
                    }
                    //在没释放写锁前，加读锁，将其他线程的可能进行的写锁屏蔽-降级写锁处理，保证数据的一致性
                    readLock.lock();
                } finally {
                    //最终释放写锁-此时仍然存在读锁
                    writeLock.unlock();
                }
            }
            return (R) data.get(key);
        } catch (Exception e) {
            //最终释放读锁
            readLock.unlock();
        }
        return null;
    }

    public Object get(String key) {
        return data.get(key);
    }

    /**
     * 具体查询数据
     *
     * @param params
     * @return
     */
    abstract R query(Q params);

}
