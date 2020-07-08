package com.milla.study.netbase.expert.concurrent;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.LongBinaryOperator;

/**
 * @Package: com.milla.study.netbase.expert.concurrent
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/5/25 12:56
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/25 12:56
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class UnSafeTests {
    private int value = 0;
    private long valueOffset = 0;
    private LongAdder longAdder = new LongAdder();

    public static void main(String[] args) throws InterruptedException {

        AtomicLong atomicLong = new AtomicLong();
        atomicLong.incrementAndGet();

        LongAccumulator longAccumulator = new LongAccumulator(new LongBinaryOperator() {
            @Override
            public long applyAsLong(long left, long right) {
                return left + right;
            }
        }, 1);
        new ReentrantLock(false);
        longAccumulator.accumulate(100);
        long l = longAccumulator.longValue();
        System.out.println(l);
//        UnSafeTests tests = new UnSafeTests();
//        for (int i = 0; i < 2; i++) {
//            new Thread(() -> {
//                for (int j = 0; j < 10000; j++) {
//                    tests.add();
//                }
//            }).start();
//        }
//        Thread.sleep(1000L);
//        int i = tests.get();
//        System.out.println(i);
//        System.out.println(tests.getLong());

    }

    ReentrantLock lock = new ReentrantLock();
    private Unsafe safe;

    {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            safe = (Unsafe) theUnsafe.get(null);
            valueOffset = safe.objectFieldOffset(UnSafeTests.class.getDeclaredField("value"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void add() {
        //1、使用锁方式，解决并发问题
        //        lock.lock();
        //        value++;
        //        lock.unlock();
        //2、使用unsafe直接操作内存
//safe.compareAndSwapInt()

        //操作是原子性的

        boolean b = safe.compareAndSwapInt(this, valueOffset, value, value + 1);
        while (!b) {
            b = safe.compareAndSwapInt(this, valueOffset, value, value + 1);
        }
        longAdder.increment();
//        value++;
    }

    private int get() {
        return value;
    }

    private long getLong() {
        return longAdder.longValue();
    }
}
