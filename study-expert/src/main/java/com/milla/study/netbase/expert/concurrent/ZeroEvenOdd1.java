package com.milla.study.netbase.expert.concurrent;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

/**
 * @Package: com.milla.study.netbase.expert.concurrent
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/6/19 18:48
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/19 18:48
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class ZeroEvenOdd1 {
    private int n;
    private Semaphore z = new Semaphore(1);
    private Semaphore e = new Semaphore(1);
    private Semaphore o = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        ZeroEvenOdd1 zeroEvenOdd1 = new ZeroEvenOdd1(5);
        new Thread(() -> {
            try {
                zeroEvenOdd1.zero((e) -> System.out.print(e));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                zeroEvenOdd1.odd((e) -> System.out.print(e));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                zeroEvenOdd1.even((e) -> System.out.print(e));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }).start();
    }

    public ZeroEvenOdd1(int n) {
        this.n = n;
    }

    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            z.acquire();
            printNumber.accept(0);
            if (i % 2 == 0) {
                o.release();
            } else {
                e.release();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            e.acquire();
            printNumber.accept(i);
            z.release();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {

        for (int i = 1; i <= n; i += 2) {
            o.acquire();
            printNumber.accept(i);
            z.release();
        }
    }

}