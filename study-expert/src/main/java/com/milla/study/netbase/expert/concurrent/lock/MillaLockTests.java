package com.milla.study.netbase.expert.concurrent.lock;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @Package: com.milla.study.netbase.expert.concurrent.lock
 * @Description: <自己实现lock>
 * @Author: MILLA
 * @CreateDate: 2020/6/2 16:33
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/2 16:33
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class MillaLockTests {
    private static int count;

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 300; i++) {
            new Thread(() -> add(), i + "").start();
        }
        Thread.sleep(500L);
        System.out.println(count);
    }

    private static final Lock lock = new MillaLock();

    private static void add() {
        lock.lock();
        for (int i = 0; i < 1000; i++) {
            count++;
        }
        lock.unlock();
    }
}

/**
 * 自己实现独享锁
 */
class MillaLock implements Lock {

    //定义是否被锁住的原子标识
    private final AtomicBoolean locked = new AtomicBoolean();
    //定义一个等待队列
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();

    @Override
    public void lock() {
        boolean wasInterrupted = false;
        Thread current = Thread.currentThread();
        waiters.add(current);
        //尝试加锁 在队列中不是第一个或无法获取锁时阻塞
        while (waiters.peek() != current || !tryLock()) {
            //挂起当前线程，没有获取到锁，不继续执行
            LockSupport.park();
            //忽略已经被中断的线程
            if (Thread.interrupted()) {
                wasInterrupted = true;
            }
        }
        waiters.remove();//从队列中移除线程
        //退出时重申中断状态
        if (wasInterrupted) {
            current.interrupt();
        }
    }

    @Override
    public boolean tryLock() {
        //原子操作
        return locked.compareAndSet(false, true);
    }

    @Override
    public void unlock() {
        //释放锁
        locked.set(false);
        //唤醒队列中第一个线程
        LockSupport.unpark(waiters.peek());
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }
}
