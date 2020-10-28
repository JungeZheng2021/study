package com.aimsphm.nuclear.data;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author milla
 */
@EnableAsync
@EnableCaching
@EnableFeignClients
@MapperScan({"com.aimsphm.nuclear.*.mapper**"})
@SpringBootApplication(scanBasePackages = "com.aimsphm.nuclear")
@RestController
public class NuclearDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(NuclearDataApplication.class, args);

    }
}
