package com.aimsphm.nuclear.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <p>
 * 功能描述:服务入口
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/10/23 11:27
 */
@EnableAsync
@EnableCaching
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.aimsphm.nuclear")
public class NuclearDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(NuclearDataApplication.class, args);

    }
}
