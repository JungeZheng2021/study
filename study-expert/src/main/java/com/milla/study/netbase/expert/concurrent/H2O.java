package com.milla.study.netbase.expert.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class H2O {
    //用于标识产生氧原子个数
    private volatile int oxygen = 0;
    //用于标识产生的氢原子个数
    private volatile int hydrogen = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public static void main(String[] args) {
        H2O h2O = new H2O();
        for (int i = 0; i < 4 * 3; i++) {

            new Thread(() -> {
                try {
                    h2O.hydrogen(() -> {
                        System.out.print("H");
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            new Thread(() -> {
                try {
                    h2O.oxygen(() -> {
                        System.out.print("O");
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    public H2O() {
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        try {
            lock.lock();
            //产生了两个H原子，就需要等O原子产生
            while (hydrogen == 2) {
                condition.await();
            }
            // releaseHydrogen.run() outputs "H". Do not change or remove this line.
            releaseHydrogen.run();
            hydrogen++;
            if (hydrogen == 2 && oxygen == 1) {
                hydrogen = 0;
                oxygen = 0;
            }
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        try {
            lock.lock();
            //产生了一个O原子，就需要等产生2个H原子
            while (oxygen == 1) {
                condition.await();
            }
            // releaseOxygen.run() outputs "O". Do not change or remove this line.
            releaseOxygen.run();
            oxygen++;
            if (hydrogen == 2 && oxygen == 1) {
                hydrogen = 0;
                oxygen = 0;
            }
            condition.signalAll();
        } finally {
            lock.unlock();
        }

    }
}
