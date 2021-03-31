package com.aimsphm.nuclear.executor.job;

import com.aimsphm.nuclear.executor.constant.JobFrequencyConstant;
import com.aimsphm.nuclear.executor.entity.SparkApplicationParam;
import com.aimsphm.nuclear.executor.service.ISparkSubmitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: xiangfeng
 */
//@EnableScheduling
@Component
public class DownSampleScheduleJobManually {
    private Logger logger = LoggerFactory.getLogger(DownSampleScheduleJobManually.class);
    @Value("${spark.master}")
    private String sparkMaster;

    @Value("${spark.main.class}")
    private String sparkMainClass;

    @Value("${spark.jar.path}")
    private String jarPath;

    @Value("${initialTimeStampGap}")
    private String initialGap;
    @Value("${tableName}")
    private String HABSE_TABLE_NAME;
    private final String CF = "pRaw";
    private final String BATCHSIZE = "30";
    @Autowired
    ISparkSubmitService sparkSubmitService;

    SparkApplicationParam param = new SparkApplicationParam();


    /* @Async
     @Scheduled(cron = "0 0 * * * ? ") //每小时*/
    // @DistributedLock("testDistributeLock") //add a ditributeLock
    public Object hourlyDownSample(long start, long end) throws Exception {

        Date currentDate = new Date();
        //Date executionDate = calculateGapDate(currentDate);

        SparkApplicationParam param = new SparkApplicationParam();
        param.setMaster(sparkMaster);
        param.setMainClass(sparkMainClass);
        param.setJarPath(jarPath);
        param.setDriverMemory("4g");
        param.setExecutorCores("10");
        param.setExecutorMemory("6g");
        String[] appParam = new String[10];
        appParam[0] = JobFrequencyConstant.HOURLY;
        //Tuple2<String,String> startAndEndTimestamp = TimeCalculator.getStartAndEndTimestamp(executionDate,JobFrequencyConstant.HOURLY);
        appParam[1] = start + "";
        appParam[2] = end + "";
        appParam[3] = HABSE_TABLE_NAME;
        appParam[4] = CF;
        appParam[5] = "1";//执行用1个来做，即生产1条记录
        appParam[6] = "5";//分段瞬态法的特别参数
        appParam[7] = "5";//分段瞬态法的特别参数
        appParam[8] = currentDate.getTime() + "";//creat on的时间
        appParam[9] = BATCHSIZE;
        param.setArgs(appParam);
        return sparkSubmitService.submitApplication(param);
    }

    /* @Async
     @Scheduled(cron = "0 0 0 * * ? ") //每天*/
    // @DistributedLock("testDistributeLock") //add a ditributeLock
    public Object dailyDownSample(long start, long end) throws Exception {
        Date currentDate = new Date();
        //Date executionDate = calculateGapDate(currentDate);
        SparkApplicationParam param = new SparkApplicationParam();
        param.setMaster(sparkMaster);
        param.setMainClass(sparkMainClass);
        param.setJarPath(jarPath);
        param.setDriverMemory("4g");
        param.setExecutorCores("10");
        param.setExecutorMemory("6g");
        String[] appParam = new String[10];
        appParam[0] = JobFrequencyConstant.DAILY;
        //Tuple2<String,String> startAndEndTimestamp = TimeCalculator.getStartAndEndTimestamp(executionDate,JobFrequencyConstant.DAILY);
        appParam[1] = start + "";
        appParam[2] = end + "";
        appParam[3] = HABSE_TABLE_NAME;
        appParam[4] = CF;
        appParam[5] = "6";//执行用6个task来做，即生产6条记录
        appParam[6] = "5";//分段瞬态法的特别参数
        appParam[7] = "5";//分段瞬态法的特别参数
        appParam[8] = currentDate.getTime() + "";//creat on的时间
        appParam[9] = BATCHSIZE;
        param.setArgs(appParam);
        return sparkSubmitService.submitApplication(param);
    }

    /* @Async
     @Scheduled(cron = "0 0 0 ? * MON") //每周*/
    // @DistributedLock("testDistributeLock") //add a ditributeLock
    public Object weeklyDownSample(long start, long end) throws Exception {
        Date currentDate = new Date();
        //Date executionDate = calculateGapDate(currentDate);
        SparkApplicationParam param = new SparkApplicationParam();
        param.setMaster(sparkMaster);
        param.setMainClass(sparkMainClass);
        param.setJarPath(jarPath);
        param.setDriverMemory("4g");
        param.setExecutorCores("10");
        param.setExecutorMemory("6g");
        String[] appParam = new String[10];
        appParam[0] = JobFrequencyConstant.WEEKLY;
        //Tuple2<String,String> startAndEndTimestamp = TimeCalculator.getStartAndEndTimestamp(executionDate,JobFrequencyConstant.WEEKLY);
        appParam[1] = start + "";
        appParam[2] = end + "";
        appParam[3] = HABSE_TABLE_NAME;
        appParam[4] = CF;
        appParam[5] = "15";//执行用6个task来做，即生产6条记录
        appParam[6] = "5";//分段瞬态法的特别参数
        appParam[7] = "5";//分段瞬态法的特别参数
        appParam[8] = currentDate.getTime() + "";//creat on的时间
        appParam[9] = BATCHSIZE;
        param.setArgs(appParam);
        return sparkSubmitService.submitApplication(param);
    }

    /* @Async
     @Scheduled(cron = "0 0 0 1 * ? ") //每月*/
    // @DistributedLock("testDistributeLock") //add a ditributeLock
    public Object monthlyDownSample(long start, long end) throws Exception {
        Date currentDate = new Date();
        // Date executionDate = calculateGapDate(currentDate);
        SparkApplicationParam param = new SparkApplicationParam();
        param.setMaster(sparkMaster);
        param.setMainClass(sparkMainClass);
        param.setJarPath(jarPath);
        param.setDriverMemory("5g");
        param.setExecutorCores("10");
        param.setExecutorMemory("6g");
        String[] appParam = new String[10];
        appParam[0] = JobFrequencyConstant.MONTHLY;
        // Tuple2<String,String> startAndEndTimestamp = TimeCalculator.getStartAndEndTimestamp(executionDate,JobFrequencyConstant.MONTHLY);
        appParam[1] = start + "";
        appParam[2] = end + "";
        appParam[3] = HABSE_TABLE_NAME;
        appParam[4] = CF;
        appParam[5] = "30";//执行用32个task来做，即生产30条记录
        appParam[6] = "5";//分段瞬态法的特别参数
        appParam[7] = "5";//分段瞬态法的特别参数
        appParam[8] = currentDate.getTime() + "";//creat on的时间
        appParam[9] = BATCHSIZE;
        param.setArgs(appParam);
        return sparkSubmitService.submitApplication(param);
    }


    public Object downSampleSingleJob(String freq, long start, long end, int partiotion) throws Exception {
        Date currentDate = new Date();
        //Date executionDate = calculateGapDate(currentDate);
        SparkApplicationParam param = new SparkApplicationParam();
        param.setMaster(sparkMaster);
        param.setMainClass(sparkMainClass);
        param.setJarPath(jarPath);
        param.setDriverMemory("2g");
        param.setExecutorCores("10");
        param.setExecutorMemory("5g");
        String[] appParam = new String[10];
        appParam[0] = freq;
        //Tuple2<String,String> startAndEndTimestamp = TimeCalculator.getStartAndEndTimestamp(executionDate,JobFrequencyConstant.WEEKLY);
        appParam[1] = start + "";
        appParam[2] = end + "";
        appParam[3] = HABSE_TABLE_NAME;
        appParam[4] = CF;
        appParam[5] = partiotion + "";//执行用6个task来做，即生产6条记录
        appParam[6] = "5";//分段瞬态法的特别参数
        appParam[7] = "5";//分段瞬态法的特别参数
        appParam[8] = currentDate.getTime() + "";//creat on的时间
        appParam[9] = BATCHSIZE;
        param.setArgs(appParam);
        return sparkSubmitService.submitApplication(param);
    }

    public Date calculateGapDate(Date date) {
        long ts = date.getTime() - Long.parseLong(initialGap);
        Date resDate = new Date(ts);
        return resDate;
    }
}
