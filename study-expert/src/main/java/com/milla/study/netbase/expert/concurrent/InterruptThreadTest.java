package com.milla.study.netbase.expert.concurrent;

import lombok.extern.slf4j.Slf4j;

/**
 * @Package: com.milla.study.netbase.expert.concurrent
 * @Description: <结束线程[如何优雅的结束线程]>
 * @Author: MILLA
 * @CreateDate: 2020/4/21 10:51
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/21 10:51
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class InterruptThreadTest {
    public static void main(String[] args) throws InterruptedException {
        stopThread(); //错误的方式
        interruptThread();//正确方式1 采用Thread中的interrupt方法
        interruptThreadByFlag();//正确方式2 使用标志位

    }

    private static void interruptThreadByFlag() throws InterruptedException {
        FlagThread flagThread = new FlagThread();
        flagThread.start();
        Thread.sleep(1500L);
        flagThread.interruptThread();//产生的结果：i:1, j:1 不存在线程安全问题
        flagThread.printInfo();
    }

    private static void interruptThread() throws InterruptedException {
        StopThread stopThread = new StopThread();
        stopThread.start();
        Thread.sleep(1000L);
        if (stopThread.isInterrupted()) {//配合该线程的状态判断使用 可防止线程中断异常
            stopThread.interrupt();//产生的结果：i:1, j:1 不存在线程安全问题
        }
        stopThread.printInfo();
    }

    private static void stopThread() throws InterruptedException {
        StopThread stopThread = new StopThread();
        stopThread.start();
        Thread.sleep(1000L);
        stopThread.stop();//产生的结果：i:1, j:0 存在线程安全问题 废弃不用了
        stopThread.printInfo();
    }

    static class FlagThread extends Thread {
        private int i, j;
        private volatile boolean flag = true;

        @Override
        public void run() {
            while (flag) {
                synchronized (this) {
                    i++;
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    j++;
                }
            }
        }

        void interruptThread() {//设置标志位
            flag = false;
        }

        void printInfo() throws InterruptedException {
            Thread.sleep(501L);
            log.info("产生的结果：i:{}, j:{}", i, j);
        }
    }

    static class StopThread extends Thread {
        private int i, j;

        @Override
        public void run() {
            synchronized (this) {
                ++i;
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("报异常么");
                }
                ++j;
            }
        }

        void printInfo() throws InterruptedException {
            Thread.sleep(10L);
            log.info("产生的结果：i:{}, j:{}", i, j);
        }
    }
}
