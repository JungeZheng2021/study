package com.milla.study.netbase.expert.concurrent.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Package: com.milla.study.netbase.expert.concurrent.lock
 * @Description: <阻塞队列>
 * @Author: MILLA
 * @CreateDate: 2020/6/1 18:14
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/1 18:14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class ListBlockLockQueue {
    /**
     * 1.定义个队列
     * 2.采用可重入锁及信号量进行控制
     * 3.数据存储时，如果数据已经满了，则存储线程进行阻塞，唤醒获取数据线程
     * 4.数据获取时，如果当前数据已经消费完，则获取线程进行阻塞，获取存储数据线程
     * 5.数据的存储按照从0到队列的长度存入，如果存满则存储索引置0
     * 6.数据的获取按照从0到队列的长度获取，如果存满则存储索引置0
     */

    //定义队列容量
    private Integer size = 5;
    //采用集合定义一个队列
    private final Object[] data = new Object[size];

    //定义可重复锁
    ReentrantLock lock = new ReentrantLock();
    //获取为空信号量
    Condition take = lock.newCondition();
    //获取为满信号量
    Condition put = lock.newCondition();
    //获取数据索引值
    int takeIndex;
    //加入数据索引值
    int putIndex;

    //当前有多少个数据可取
    int count;

    /**
     * 拉取一个数据
     *
     * @return
     */
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            //没有数据可取-取数据线程阻塞
            while (count == 0) {
                take.await();//阻塞
            }
            //获取先存如数据
            Object o = data[takeIndex];
            //数据获取索引达到队列长度(说明当前数据取完了)-将获取索引置为0
            if (++takeIndex == data.length) {
                takeIndex = 0;
            }
            --count;
            System.out.println(Thread.currentThread().getName() + ",takeIndex： " + takeIndex + " count: " + count);
            put.signal();
            return o;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 存入数据
     *
     * @param obj
     */
    public void put(Object obj) throws InterruptedException {
        //先上锁
        lock.lock();
        try {
            //如果已经满了-阻塞
            while (count == data.length) {
                //就不能继续添加
                put.await();
            }
            //添加元素
            data[putIndex] = obj;
            //添加数据索引达到队列长度-将索引值重置为0
            if (++putIndex == data.length) {
                putIndex = 0;
            }
            ++count;
            System.out.println(Thread.currentThread().getName() + ",takeIndex： " + takeIndex + " count: " + count);
            take.signal();
        } finally {
            //最后释放锁
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ListBlockLockQueue queue = new ListBlockLockQueue();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    queue.put("" + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "一");
        Thread t2 = new Thread(() -> {
            while (true) {
                try {
                    System.out.println(Thread.currentThread().getName() + ",获取数据： " + queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "二");
        t2.start();
        t1.start();
        Thread.sleep(1000);
        queue.put("123456");
        Thread.sleep(1000);
        queue.put("00000");
        Thread.sleep(1000);
        queue.put("555555");

    }
}
