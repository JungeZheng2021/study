package com.milla.study.netbase.expert.concurrent.lock;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Package: com.milla.study.netbase.expert.concurrent.lock
 * @Description: <生产者消费者>
 * @Author: MILLA
 * @CreateDate: 2020/6/2 13:22
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/2 13:22
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class ProducerAndConsumerTest {
    public static void main(String[] args) {
        ProducerAndConsumerTest test = new ProducerAndConsumerTest();
        Thread t1 = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(800L);
                    test.producer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "producer");

        Thread t2 = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500L);
                    System.out.println(Thread.currentThread().getName() + ",消费数据：" + test.consumer());
                    System.out.println();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "consumer");
        t1.start();
        t2.start();
    }

    //产品code
    private String productCode;
    //锁
    private ReentrantLock lock = new ReentrantLock();
    //消费者信号量
    private Condition consumer = lock.newCondition();
    //生产者信号量
    private Condition producer = lock.newCondition();


    /**
     * 生产者方法
     *
     * @throws InterruptedException
     */
    public void producer() throws InterruptedException {
        //加锁
        lock.lock();
        try {
            //如果当前产品code为空，需要阻塞
            while (!Objects.isNull(productCode)) {
                producer.await();//阻塞
            }
            //TODO 真正的生产数据(模拟)
            productCode = UUID.randomUUID().toString();
            System.out.println(Thread.currentThread().getName() + ",生产数据：" + productCode);
            //需要唤醒消费者信号量
            consumer.signal();
        } finally {
            //最终需要释放锁
            lock.unlock();
        }
    }

    /**
     * 消费者方法
     *
     * @return
     * @throws InterruptedException
     */
    public String consumer() throws InterruptedException {
        //加锁
        lock.lock();
        try {
//            如果当前不存在可用的产品code需要阻塞
            while (Objects.isNull(productCode)) {
                consumer.await();//阻塞
            }
            //产品被消费掉后，需要唤醒生产者
            producer.signal();
            return productCode;
        } finally {
            //TODO  消费掉数据(模拟)
            productCode = null;
            //最终释放锁
            lock.unlock();
        }
    }

}

