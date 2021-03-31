package com.aimsphm.nuclear.executor.service;


import com.aimsphm.nuclear.executor.entity.SparkApplicationParam;

import java.io.IOException;

/**
 * @description spark任务提交service
 **/
public interface ISparkSubmitService {
    /**
     * 提交spark任务入口
     *
     * @param sparkAppParams spark任务运行所需参数
     * @return 结果
     * @throws IOException          io
     * @throws InterruptedException 线程等待中断异常
     */
    int submitApplication(SparkApplicationParam sparkAppParams) throws IOException, InterruptedException;
}
