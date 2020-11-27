package com.milla.study.netbase.expert.io.netty.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.milla.study.netbase.expert.io.netty.demo
 * @Description: <多线程使用同一个channel>
 * @Author: milla
 * @CreateDate: 2020/08/21 18:00
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/21 18:00
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class MulChannel {
    public static void main(String[] args) {
        start();
    }

    private static void start() {

        ByteBuf data = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8).retain();
        ExecutorService service = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        service.execute(runnable);
    }
}

