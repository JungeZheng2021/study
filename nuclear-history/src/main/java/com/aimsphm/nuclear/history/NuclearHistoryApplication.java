package com.aimsphm.nuclear.history;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Package: com.aimsphm.nuclear.history
 * @Description: <历史查询服务>
 * @Author: MILLA
 * @CreateDate: 2020/6/28 10:54
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/28 10:54
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.aimsphm.nuclear"})
public class NuclearHistoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(NuclearHistoryApplication.class, args);
    }

}
