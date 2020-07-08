package com.milla.study.netbase.expert.concurrent.lock;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * @Package: com.milla.study.netbase.expert.concurrent.lock
 * @Description: <信号量测试>
 * @Author: MILLA
 * @CreateDate: 2020/6/3 14:21
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/3 14:21
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class AbstractQueuedSynchronizerTests {
    public static void main(String[] args) throws InterruptedException {

//        semaphoreTest();
//        countDownLatchTest();
        cyclicBarrierTest();
    }

    private static void cyclicBarrierTest() {
        //只要满足有两个线程就继续往下执行
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
            System.out.println("两人拼团成功了！！！");
        });
        int size = 2;
        for (int i = 0; i < size; i++) {
            String vipNo = "VIP-" + i;
            new Thread(() -> {
                try {
                    execute(vipNo);
                    cyclicBarrier.await();//当所有的部分都指定到await的时候，栅栏才会打开，才会继续往下执行
                    System.out.println("执行了..");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        System.out.println("所有的任务都执行结束了...");

    }

    private static void countDownLatchTest() throws InterruptedException {
        int size = 5;
        //一般是等待线程达到指定个个数就开始一并执行
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            String vipNo = "VIP-" + i;
            new Thread(() -> {
                try {
                    execute(vipNo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println("所有的任务都执行结束了...");
    }

    private static void semaphoreTest() {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 10; i++) {
            String vipNo = "VIP-" + i;
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    execute(vipNo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                semaphore.release();

            }).start();
        }
    }

    private static void execute(String vipNo) throws InterruptedException {
        System.out.println("迎接贵宾[编号：" + vipNo + "] 客人");
        Thread.sleep(new Random().nextInt(3000));
        System.out.println("欢送[编号：" + vipNo + "] 客人");

    }
}
