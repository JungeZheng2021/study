package com.aimsphm.nuclear.report;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Package: com.aimsphm.nuclear.report
 * @Description: <时间操作工具>
 * @Author: MILLA
 * @CreateDate: 2018/5/4 16:02
 * @UpdateUser: MILLA
 * @UpdateDate: 2018/5/4 16:02
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@EnableAsync
@EnableCaching
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.aimsphm.nuclear"})
@MapperScan({"com.aimsphm.nuclear.*.mapper**"})
@EnableTransactionManagement
@EnableFeignClients(basePackages = {"com.aimsphm.nuclear"})
public class ReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportApplication.class, args);
    }
}
