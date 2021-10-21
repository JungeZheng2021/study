package com.aimsphm.nuclear.algorithm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 * 功能描述:算法调用服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/6/28 10:54
 */
@EnableAsync
@EnableEurekaClient
@EnableFeignClients
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.aimsphm.nuclear"})
public class AlgorithmApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlgorithmApplication.class, args);
    }
}
