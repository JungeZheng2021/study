package com.milla.study.netbase.expert.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

public class ZeroEvenOdd {
    private int n;

    public ZeroEvenOdd(int n) {
        this.n = n;
    }


    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Condition odd = lock.newCondition();
    private Condition even = lock.newCondition();
    private volatile char isChange = '0';

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        for (int i = 1; i <= n; i++) {

            if (isChange != '0') {
                condition.await();
            }
            printNumber.accept(0);

            if (i % 2 == 1) {//唤醒奇数
                odd.signal();
                isChange = '1';
            } else {//唤醒偶数
                even.signal();
                isChange = '2';
            }
        }
        lock.unlock();
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        for (int i = 2; i <= n; i += 2) {
            if (isChange != '2') {
                even.await();
            }
            printNumber.accept(i);
            condition.signal();//唤醒0操作
            isChange = '0';
        }
        lock.unlock();
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        for (int i = 1; i <= n; i += 2) {
            if (isChange != '1') {
                odd.await();
            }
            printNumber.accept(i);
            condition.signal();//唤醒0操作
            isChange = '0';
        }

        lock.unlock();
    }

    public static void main(String[] args) {
        ZeroEvenOdd zeroEvenOdd = new ZeroEvenOdd(2);
        new Thread(() -> {
            try {
                zeroEvenOdd.zero((e) -> System.out.print(e));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                zeroEvenOdd.odd((e) -> System.out.print(e));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                zeroEvenOdd.even((e) -> System.out.print(e));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }).start();
    }
}