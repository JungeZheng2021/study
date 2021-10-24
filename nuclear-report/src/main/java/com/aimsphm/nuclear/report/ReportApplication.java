package com.aimsphm.nuclear.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 功能描述:自动报告主程序入口
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/5/12 18:58
 */
@RestController
@EnableAsync
@EnableCaching
//@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.aimsphm.nuclear"})
@EnableTransactionManagement
@EnableFeignClients(basePackages = {"com.aimsphm.nuclear"})
public class ReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportApplication.class, args);
    }
}
