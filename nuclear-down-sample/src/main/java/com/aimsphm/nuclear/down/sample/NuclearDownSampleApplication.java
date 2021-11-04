package com.aimsphm.nuclear.down.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * 功能描述:定时任务降采样
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/11/01 09:37
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.aimsphm.nuclear"})
public class NuclearDownSampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(NuclearDownSampleApplication.class, args);
    }

}
