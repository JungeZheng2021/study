package com.milla.study.netbase.expert.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * @Package: com.milla.study.netbase.expert.concurrent
 * @Description: <线程本地变量>
 * @Author: MILLA
 * @CreateDate: 2020/4/21 18:34
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/21 18:34
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class ThreadPoolTests {
    public static void main(String[] args) throws Exception {
//        testThreadPoolExecutor();
        testCashThreadPoolExecutor();
//        testScheduledThreadPoolExecutor();
//        testScheduledThreadPoolExecutorCycle();
//        testScheduledThreadPoolExecutorCycle1();
//
//        testScheduledThreadPoolExecutorCallBack();

    }

    private static void testCashThreadPoolExecutor() throws Exception {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        testCommon((ThreadPoolExecutor) cachedThreadPool);

    }


    //线程池信息：核心线程数5，最大线程池数10，超出核心线程数存活时间5秒，
    //预期：每次查看是否已经超过核心线程数，没有超过的话就创建线程，超过的话，就判断工作队列是否已满，不满的话直接添加到队列，如果已满，则执行决绝策略
    private static void testThreadPoolExecutor() throws Exception {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 5,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), (a, b) -> System.out.println("自定义"));//自定义拒绝策略
//                TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), new ThreadPoolExecutor.DiscardOldestPolicy());//删除队列头部的任务，重试执行(可能再次失败，导致重复)
//                TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), new ThreadPoolExecutor.DiscardPolicy());//直接丢弃需要执行的任务
//                TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), new ThreadPoolExecutor.CallerRunsPolicy());//如果线程池没有关闭直接运行任务(性能不高)
//                TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), new ThreadPoolExecutor.AbortPolicy());//直接抛出异常
        testCommon(executor);
    }

    private static void testCommon(ThreadPoolExecutor executor) throws Exception {
        //提交15个执行时间为3s的任务
        for (int i = 0; i < 15; i++) {
            executor.submit(() -> {
                try {
//                    Thread.sleep(500L);
                    log.info("开始执行....");
                    Thread.sleep(3000L);
                    log.info("执行结束....name:{}", Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            log.info("任务[{}]提交成功", i);
        }
        Thread.sleep(500L);
        log.info("当前线程池线程数量：{}; 当前线程池等待的数量:{} ;核心线程数量：{};最大线程数量：{}", executor.getPoolSize(), executor.getQueue().size(), executor.getCorePoolSize(), executor.getMaximumPoolSize());
        Thread.sleep(60000L);//所有的都执行结束
        log.info("当前线程池线程数量：{}; 当前线程池等待的数量:{} ;核心线程数量：{};最大线程数量：{}", executor.getPoolSize(), executor.getQueue().size(), executor.getCorePoolSize(), executor.getMaximumPoolSize());
        executor.shutdown();
    }

    //周期性执行，在当前任务执行结束固定时间后继续执行
    //预期： 开始任务延迟2秒执行，任务执行了5秒，然后等待3秒周继续执行，依次类推
    private static void testScheduledThreadPoolExecutorCycle1() {
        ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(5);
        scheduledExecutor.scheduleWithFixedDelay(() -> {
            log.info("周期执行开始,当前时间:{},当前线程：{}", System.currentTimeMillis(), Thread.currentThread().getName());
            try {
                Thread.sleep(5000);//模拟任务执行周期是5秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println();
            log.info("周期执行结束,当前时间:{},当前线程：{}", System.currentTimeMillis(), Thread.currentThread().getName());
//            初始延迟2秒       周期3秒
        }, 2000, 3000, TimeUnit.MILLISECONDS);
        log.info("任务提交时间为：{} ,当前线程池中的线程数量为：{}", System.currentTimeMillis(), scheduledExecutor.getCorePoolSize());
    }

    //周期性执行，有固定执行周期
    //如果任务执行时长超过周期的话，不会并行执行，会自动延期
    //预期： 开始任务延迟2秒执行，后过5秒 继续执行
    private static void testScheduledThreadPoolExecutorCycle() {
        ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(5);
        scheduledExecutor.scheduleAtFixedRate(() -> {
            System.out.println();
            log.info("周期执行开始,当前时间:{},当前线程：{}", System.currentTimeMillis(), Thread.currentThread().getName());
            try {
                Thread.sleep(3000);//模拟任务执行周期是3秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("周期执行结束,当前时间:{},当前线程：{}", System.currentTimeMillis(), Thread.currentThread().getName());
//            初始延时2秒          固定周期1秒
        }, 2000, 1000, TimeUnit.MILLISECONDS);
        log.info("任务提交时间为：{} ,当前线程池中的线程数量为：{}", System.currentTimeMillis(), scheduledExecutor.getCorePoolSize());
    }

    //没有返回值 仅执行一次
    private static void testScheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(5);
        scheduledExecutor.schedule(() -> log.info("任务被执行,当前时间:{}", System.currentTimeMillis()),
                //延时30毫秒      单位：毫秒
                30, TimeUnit.MILLISECONDS);
    }

    //有返回值 延迟执行，仅执行一次
    private static void testScheduledThreadPoolExecutorCallBack() throws ExecutionException, InterruptedException {
        ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(5);
        ScheduledFuture<?> future = scheduledExecutor.schedule(() -> {
            return UUID.randomUUID().toString();
//            延时1000毫秒   单位毫秒
        }, 1000, TimeUnit.MILLISECONDS);
        //获取返回值
        Object o = future.get();
        log.info("返回值为:{}", o);
    }
}
