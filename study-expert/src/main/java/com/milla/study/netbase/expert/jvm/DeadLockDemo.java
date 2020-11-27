package com.milla.study.netbase.expert.jvm;

/**
 * @Description: <模拟死锁>
 * @Author: MILLA
 * @CreateDate: 2020/4/23 15:16
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/23 15:16
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class DeadLockDemo {
    public static String obj1 = "obj1";
    public static String obj2 = "obj2";

    public static void main(String[] args) {
        // 处理用户请求时，出现了死锁。用户无响应，多次重试，大量资源被占用（）
        //线程a 先获取obj1锁，然后等待3秒之后获取obj2锁，此时obj2锁已经被线程b占有，线程a就一直等待线程b释放obj2锁
        //线程b 先获取obj2锁，然后等待3秒之后获取obj1锁，此时obj1锁已经被线程a占有，线程b就一直等待线程a释放obj2锁
        //最终线程a和线程b陷入一个先有蛋还是先有鸡的循环等待问题，此时产生死锁
        Thread a = new Thread(new Lock1());
        Thread b = new Thread(new Lock2());
        a.start();
        b.start();
    }
}

class Lock1 implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("Lock1 running");
            while (true) {
                synchronized (DeadLockDemo.obj1) {
                    System.out.println("Lock1 lock obj1");
                    //获取obj1后先等一会儿，让Lock2有足够的时间锁住obj2
                    Thread.sleep(3000);
                    synchronized (DeadLockDemo.obj2) {
                        System.out.println("Lock1 lock obj2");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Lock2 implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("Lock2 running");
            while (true) {
                synchronized (DeadLockDemo.obj2) {
                    System.out.println("Lock2 lock obj2");
                    Thread.sleep(3000);
                    synchronized (DeadLockDemo.obj1) {
                        System.out.println("Lock2 lock obj1");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}