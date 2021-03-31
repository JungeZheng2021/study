package com.aimsphm.nuclear.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Package: com.aimsphm.nuclear.executor
 * @Description: <spark任务执行入口>
 * @Author: MILLA
 * @CreateDate: 2020/11/12 17:29
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/12 17:29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.aimsphm.nuclear"})
public class ExecutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExecutorApplication.class, args);
    }

}
