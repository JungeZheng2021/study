package com.aimsphm.nuclear.report.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.aimsphm.nuclear.report.config
 * @Description: <线程池配置>
 * @Author: MILLA
 * @CreateDate: 2020/5/12 12:57
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/12 12:57
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Configuration
public class ThreadPoolConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        //如果线程池没有关闭直接运行任务(性能不高)
        return new ThreadPoolExecutor(15, 50, 10,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
