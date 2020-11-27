package com.example.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @Package: com.example.mq
 * @Description: <对象存活在1秒左右的场景，远远超过平时接口的响应时间要求，场景应该为吞吐量优先>
 * @Author: milla
 * @CreateDate: 2020/07/31 18:45
 * @UpdateUser: milla
 * @UpdateDate: 2020/07/31 18:45
 * @UpdateRemark: <打包之后在服务器上运行>
 * @Version: 1.0
 */
@SpringBootApplication
@EnableAsync
public class RabbitTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitTestApplication.class, args);
    }
//
//    private static void testMemory() {
////        每隔100毫秒执行一次
////         启动程序，模拟用户请求
////         每100毫秒钟创建150线程，每个线程创建一个512kb的对象，最多一秒同时存在1500线程，占用内存750m（75%），查看GC的情况
//        new ScheduledThreadPoolExecutor(1).scheduleAtFixedRate(() -> {
//            new Thread(() -> {
//                for (int i = 0; i < 150; i++) {
//                    //开辟512k的内存空间
//                    byte[] bytes = new byte[1024 * 512];
//                    try {
//                        //随机休眠1s以内的时间
//                        Thread.sleep(new Random().nextInt(1000));
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }, 100, 100, TimeUnit.MILLISECONDS);
//    }
//
//    private static void test() throws InterruptedException {
//        new Thread(() -> {
//            while (true) {
//                new Random().nextInt(100);
//            }
//        }, "CPU-high").start();
//        for (int i = 0; i < 1000; i++) {
//            new Thread(() -> {
//                try {
//                    int x = 0;
//                    for (int j = 0; j < 10000; j++) {
//                        x = x + 1;
//                        long random = new Random().nextInt(100);
//                        // 模拟c处理耗时
//                        Thread.sleep(random);
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//            long random = new Random().nextInt(500);
//            // 模拟接口调用
//            Thread.sleep(random);
//        }
//}
}
