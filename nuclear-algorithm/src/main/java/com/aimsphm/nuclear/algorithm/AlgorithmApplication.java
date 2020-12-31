package com.aimsphm.nuclear.algorithm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @Package: com.aimsphm.nuclear.history
 * @Description: <算法调用服务类>
 * @Author: MILLA
 * @CreateDate: 2020/6/28 10:54
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/28 10:54
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@EnableAsync
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"com.aimsphm.nuclear"})
public class AlgorithmApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlgorithmApplication.class, args);
    }

}
