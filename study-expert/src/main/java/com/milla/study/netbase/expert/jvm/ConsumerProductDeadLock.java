package com.milla.study.netbase.expert.jvm;

/**
 * @Description: <消费代码写的有问题带来的死锁>
 * @Author: MILLA
 * @CreateDate: 2020/4/23 15:16
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/23 15:16
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class ConsumerProductDeadLock {
    /**
     * 生产货物
     */
    public static Object product = null;

    /**
     * 会导致程序永久等待的wait/notify
     */
    public void waitNotifyDeadLockTest() throws Exception {
        // 启动消费者线程
        new Thread(() -> {
            //没有货物可消费需要进行等待
            if (product == null) {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                synchronized (this) {
                    try {
                        System.out.println("1、进入等待，线程ID为： " + Thread.currentThread().getId());
                        this.wait(); // 多次查看
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("2、买到获取，回家");
        }).start();
        // 此时休眠的时间比没有货物休眠的时间端，导致下面的代码先运行，已经执行了this.notifyAll();
        // 致使wait的线程永远也不会被唤醒导致死锁
        Thread.sleep(3000L);
        product = new Object();
        synchronized (this) {
            this.notifyAll();
            System.out.println("3、通知消费者");
        }
    }

    public static void main(String[] args) throws Exception {
        new ConsumerProductDeadLock().waitNotifyDeadLockTest();
    }
}
