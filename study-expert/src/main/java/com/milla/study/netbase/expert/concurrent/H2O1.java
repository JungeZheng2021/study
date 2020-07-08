package com.milla.study.netbase.expert.concurrent;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class H2O1 {

    public static void main(String[] args) {
        H2O1 h2O = new H2O1();
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

    Semaphore h = new Semaphore(2);
    Semaphore o = new Semaphore(1);

    public H2O1() {


    }

    CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> {
        h.release(2);
        o.release();
    });

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {

        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        h.acquire();
        releaseHydrogen.run();
        try {
            cyclicBarrier.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        // releaseOxygen.run() outputs "O". Do not change or remove this line.
        o.acquire();
        releaseOxygen.run();
        try {
            cyclicBarrier.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
