package com.aimsphm.nuclear.executor.service.impl;

import com.aimsphm.nuclear.executor.entity.SparkApplicationParam;
import com.aimsphm.nuclear.executor.service.ISparkSubmitService;
import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 *
 **/
@Service
public class SparkSubmitServiceImpl implements ISparkSubmitService {

    private static Logger log = LoggerFactory.getLogger(SparkSubmitServiceImpl.class);

    @Value("${driver.name:n151}")
    private String driverName;
    @Value("${spark.home.linuxdir}")
    private String sparkHome;

    @Override
    public int submitApplication(SparkApplicationParam sparkAppParams) throws IOException, InterruptedException {
        log.info("spark任务传入参数：{}", sparkAppParams);
        int returnCode = 1;

        CountDownLatch countDownLatch = new CountDownLatch(1);
        String[] appArgs = sparkAppParams.getArgs();
        SparkLauncher launcher = new SparkLauncher()
                .setSparkHome(sparkHome)
                .setAppResource(sparkAppParams.getJarPath())
                .setMainClass(sparkAppParams.getMainClass())
                .setMaster(sparkAppParams.getMaster())
                .setConf("spark.driver.memory", sparkAppParams.getDriverMemory())
                .setConf("spark.executor.memory", sparkAppParams.getExecutorMemory())
                .setConf("spark.executor.cores", sparkAppParams.getExecutorCores());
        launcher.addAppArgs(appArgs);
        log.info("参数设置完成，开始提交spark任务");
        launcher.setVerbose(false).startApplication(new SparkAppHandle.Listener() {
            @Override
            public void stateChanged(SparkAppHandle sparkAppHandle) {
                if (sparkAppHandle.getState().isFinal()) {
                    countDownLatch.countDown();
                }
                log.info("stateChanged:{}", sparkAppHandle.getState().toString());
            }

            @Override
            public void infoChanged(SparkAppHandle sparkAppHandle) {
                log.info("infoChanged:{}", sparkAppHandle.getState().toString());
            }
        });
        log.info("The task is executing, please wait ....");
        //线程等待任务结束
        countDownLatch.await();
        log.info("The task is finished!");
        returnCode = 0;
        return returnCode;
    }
}
