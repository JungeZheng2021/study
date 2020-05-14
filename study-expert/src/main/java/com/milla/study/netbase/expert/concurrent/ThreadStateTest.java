package com.milla.study.netbase.expert.concurrent;

import lombok.extern.slf4j.Slf4j;

/**
 * @Package: com.milla.study.netbase.expert.concurrent
 * @Description: <线程状态>
 * @Author: MILLA
 * @CreateDate: 2020/4/20 17:58
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/20 17:58
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class ThreadStateTest {
    public static void main(String[] args) throws Exception {


        timedWaitingState();
        //lockState();
//        noLockState();
    }

    private static void timedWaitingState() throws InterruptedException {
        Thread thread = new MyThread();
        thread.start();
        Thread.sleep(100L);
        System.out.println(thread.getState());
        Thread.sleep(1000L);
        System.out.println(thread.getState());
    }

    static class MyThread extends Thread {

        @Override
        public void run() {
            super.run();
            synchronized (this) {
                try {
                    this.wait(1000L);// 带有超时时间的是TIMED_WAITING状态，过期之后会自动释放
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void lockState() throws InterruptedException {
        Thread thread = new Thread(() -> {
            synchronized (ThreadStateTest.class) {
                System.out.println("获取锁后开始执行：");
                printCurrentThread();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "lock");

        System.out.println("创建后：");
        printThreadInfo(thread);
        synchronized (ThreadStateTest.class) {
            thread.start();
            System.out.println("执行后：");
            printThreadInfo(thread);
            Thread.sleep(2000L);
            System.out.println("获取锁前：");
            printThreadInfo(thread);

        }
        Thread.sleep(100L);
        System.out.println("获取锁后开始等待：");
        printThreadInfo(thread);
        Thread.sleep(1200L);
        System.out.println("执行结束后：");
        printThreadInfo(thread);
    }

    //创建的状态
    private static void noLockState() throws Exception {
        Thread thread = new Thread(() -> {
            printCurrentThread();
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "test");
        printThreadInfo(thread);
        thread.start();
        Thread.sleep(1000L);
        printThreadInfo(thread);
        Thread.sleep(200L);
        printThreadInfo(thread);
    }

    //打印线程状态
    private static void printThreadInfo(Thread thread) {
        log.info("当前线程名称：{}, 线程状态：{}", thread.getName(), thread.getState().toString());
    }

    //打印线程状态
    private static void printCurrentThread() {
        log.info("run(),当前线程名称：{}, 线程状态：{}", Thread.currentThread().getName(), Thread.currentThread().getState().toString());
    }

}
