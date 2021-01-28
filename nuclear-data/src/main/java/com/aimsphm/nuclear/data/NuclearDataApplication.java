package com.aimsphm.nuclear.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Package: com.aimsphm.nuclear.data
 * @Description: <服务入口>
 * @Author: MILLA
 * @CreateDate: 2020/10/23 11:27
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/10/23 11:27
 * @UpdateRemark: <>
 * @Version: 1.0
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
