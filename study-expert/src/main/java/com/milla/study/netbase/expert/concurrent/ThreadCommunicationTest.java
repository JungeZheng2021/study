package com.milla.study.netbase.expert.concurrent;

import java.util.Objects;
import java.util.concurrent.locks.LockSupport;

/**
 * @Package: com.milla.study.netbase.expert.concurrent
 * @Description: <线程通信类>
 * @Author: MILLA
 * @CreateDate: 2020/4/21 14:29
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/21 14:29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class ThreadCommunicationTest {
    private Object product;

    public static void main(String[] args) throws InterruptedException {
        ThreadCommunicationTest test = new ThreadCommunicationTest();
        //对调用有顺序要求，开发中容易出现死锁现象，也容易永久挂起，已经被废弃
//        test.oldJdkCommunication();//正常情况
        test.oldJdkCommunicationWaitingBySynchronized();//因为同步块导致永久阻塞
        test.oldJdkCommunicationWaitingByOrder();//唤醒顺序问题导致永久挂起

        //wait/notify要求在同步关键字中使用，避免了死锁现象，但是如果不先调用wait，而先调用notify的情况下，容易导致线程永久挂起
//        test.waitAndNotifyAll();//正常情况
        test.waitAndNotifyAllWaitingByOrder();//顺序问题导致一直阻塞

        //park/unpark没有顺序要求，但park并不会释放锁，在同步代码快中依然有永久阻塞的问题
//        test.parkAndUnPark();//正常情况
        test.parkAndUnParkWaitingBySynchronized();//顺序问题导致一直阻塞


    }

    private void parkAndUnParkWaitingBySynchronized() throws InterruptedException {
        Thread consumer = new Thread(() -> {
            System.out.println("开始消费....");
            while (Objects.isNull(product)) {
                System.out.println("没有许可,阻塞中....");
                synchronized (this) {//同步代码块，park方法不会释放锁，所以，一直在阻塞状态
                    LockSupport.park();//许可是不可用的，将线程阻塞
                }
            }
            System.out.println("继续消费....");
        });
        consumer.start();
        Thread.sleep(3000L);
        product = new Object();

        synchronized (this) {
            LockSupport.unpark(consumer);
        }
        System.out.println("发放许可，使线程继续执行....");
    }

    private void parkAndUnPark() throws InterruptedException {
        Thread consumer = new Thread(() -> {
            System.out.println("开始消费....");
            while (Objects.isNull(product)) {
                System.out.println("没有许可,阻塞中....");
                LockSupport.park();//许可是不可用的，将线程阻塞
            }
            System.out.println("继续消费....");
        });
        consumer.start();
        Thread.sleep(3000L);
        product = new Object();

        LockSupport.unpark(consumer);//发送许可，使线程能继续执行
        System.out.println("发放许可，使线程继续执行....");
    }

    private void waitAndNotifyAllWaitingByOrder() throws InterruptedException {
        Thread consumer = new Thread(() -> {

            if (Objects.isNull(product)) {
                System.out.println("没有产品可以消费,进行等待...");
                try {
                    Thread.sleep(5000L);//外接因素导致线程先被唤醒后才进行等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (this) {
                    try {
                        this.wait();//该方法会执行后会失去锁，被唤醒后需从该语句的后一个语句开始执行
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("继续消费...........");
            }
        });
        consumer.start();
        System.out.println("启动消费者线程...");
        Thread.sleep(1000L);
        synchronized (this) {
            this.notifyAll();
            System.out.println("生产出产品，唤醒所有等待线程...");
        }
    }

    private void waitAndNotifyAll() throws InterruptedException {
        Thread consumer = new Thread(() -> {
            synchronized (this) {
                while (Objects.isNull(product)) {
                    System.out.println("没有产品可以消费,进行等待...");
                    try {
                        this.wait();//被唤醒后需从该语句的后一个语句开始执行
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("继续消费...........");
            }
        });
        consumer.start();
        System.out.println("启动消费者线程...");
        Thread.sleep(1000L);
        synchronized (this) {
//            product = new Object();//当生产者不被创建的时候时候，唤醒线程，线程会出现伪唤醒的情况。所以在使用wait的时候应采用while的判断模式[JDK推荐方式]，而不是if
            this.notifyAll();
            System.out.println("生产出产品，唤醒所有等待线程...");
        }
    }

    //方法调用顺序错误导致死锁
    private void oldJdkCommunicationWaitingByOrder() throws InterruptedException {
        Thread producer = new Thread(() -> {

            if (Objects.isNull(product)) {
                System.out.println("如果产品是空的话，挂起当前线程[需要获取到锁]，进入等等");
                synchronized (this) {
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Thread.currentThread().suspend();//挂起线程
                }
            }
            System.out.println("继续消费");
        }, "生产者");

        producer.start();
        Thread.sleep(1000L);
        product = new Object();
        producer.resume();//恢复消费线程,在线程被挂起之前唤醒线程，线程挂起之后就一直处于阻塞状态
        System.out.println("重新唤起线程进行消费");
        Thread.sleep(3001L);
        producer.resume();//继续唤醒,此时如果在线程被挂起之后继续唤醒线程，还是可以唤醒成功的()
        System.out.println("继续唤醒...");
    }

    //同步代码块会死锁
    private void oldJdkCommunicationWaitingBySynchronized() throws InterruptedException {
        Thread producer = new Thread(() -> {

            if (Objects.isNull(product)) {
                System.out.println("如果产品是空的话，挂起当前线程[需要获取到锁]，进入等等");
                synchronized (this) {
                    Thread.currentThread().suspend();//挂起线程
                }
            }
            System.out.println("继续消费");
        }, "生产者");

        producer.start();
        Thread.sleep(3000L);
        product = new Object();
        synchronized (this) {
            producer.resume();//恢复消费线程
        }
        System.out.println("重新唤起线程进行消费");
    }

    private void oldJdkCommunication() throws InterruptedException {
        Thread producer = new Thread(() -> {

            if (Objects.isNull(product)) {
                System.out.println("如果产品是空的话，挂起当前线程[需要获取到锁]，进入等等");
                synchronized (this) {
                    Thread.currentThread().suspend();//挂起线程
                }
            }
            System.out.println("继续消费");
        }, "生产者");

        producer.start();
        Thread.sleep(3000L);
        product = new Object();
        producer.resume();//恢复消费线程
        System.out.println("重新唤起线程进行消费");

    }
}