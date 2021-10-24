package com.aimsphm.nuclear.executor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparkApplicationParam {
    /**
     * 任务的主类
     */
    private String mainClass;
    /**
     * jar包路径
     */
    private String jarPath;
    @Value("${spark.master:yarn}")
    private String master;
    @Value("${spark.driver.memory:1g}")
    private String driverMemory;
    @Value("${spark.executor.memory:1g}")
    private String executorMemory;
    @Value("${spark.executor.cores:1}")
    private String executorCores;
    /**
     * 其他配置：传递给spark job的参数
     */
    private String[] args;


    /**
     * 调用该方法可获取spark任务的设置参数
     *
     * @return SparkApplicationParam
     */
    public SparkApplicationParam getSparkApplicationParam() {
        return new SparkApplicationParam(mainClass, jarPath, master, driverMemory, executorMemory, executorCores, args);
    }
}
