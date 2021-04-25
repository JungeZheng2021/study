package com.aimsphm.nuclear.executor.job;

import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.constant.SymbolConstant;
import com.aimsphm.nuclear.common.redis.RedisClient;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.executor.constant.JobFrequencyConstant;
import com.aimsphm.nuclear.executor.entity.SparkApplicationParam;
import com.aimsphm.nuclear.executor.service.ISparkSubmitService;
import com.aimsphm.nuclear.executor.util.DBTableUtil;
import com.aimsphm.nuclear.executor.util.TimeCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import scala.Tuple2;

import java.util.Date;

import static com.aimsphm.nuclear.common.util.DateUtils.YEAR;

/**
 * @Author: xiangfeng
 */
@EnableScheduling
@Component
public class DownSampleScheduleJob {
    private Logger logger = LoggerFactory.getLogger(DownSampleScheduleJob.class);
    @Value("${spark.master}")
    private String sparkMaster;

    @Value("${spark.main.class}")
    private String sparkMainClass;

    @Value("${spark.jar.path}")
    private String jarPath;

    @Value("${initialTimeStampGap}")
    private String initialGap;

    @Value("${tableName}")
    private String tableName;

    @Value("${cf}")
    private String cf;

    @Value("${config.driver.memory.hourly:1g}")
    private String driverMemoryHourly;
    @Value("${config.executor.cores.hourly:1}")
    private String executorCoresHourly;
    @Value("${config.executor.memory.hourly:1g}")
    private String executorMemoryHourly;

    @Value("${config.driver.memory.daily:2g}")
    private String driverMemoryDaily;
    @Value("${config.executor.cores.daily:2}")
    private String executorCoresDaily;
    @Value("${config.executor.memory.daily:2g}")
    private String executorMemoryDaily;

    @Value("${config.driver.memory.weekly:2g}")
    private String driverMemoryWeekly;
    @Value("${config.executor.cores.weekly:2}")
    private String executorCoresWeekly;
    @Value("${config.executor.memory.weekly:2g}")
    private String executorMemoryWeekly;

    @Value("${config.driver.memory.monthly:3g}")
    private String driverMemoryMonthly;
    @Value("${config.executor.cores.monthly:2}")
    private String executorCoresMonthly;
    @Value("${config.executor.memory.monthly:3g}")
    private String executorMemoryMonthly;


    private final String BATCH_SIZE = "30";

    private final String DB_SCHEMA = "nuclear_tw";

    private final String DAILY_TABLE_PREFIX = "spark_down_sample_daily";
    @Autowired
    ISparkSubmitService sparkSubmitService;
    @Autowired
    RedisClient redis;
    @Autowired
    DBTableUtil dbUtil;


    @Scheduled(cron = "${config.cron.hourly:0 13 * * * ? }")
//    @Scheduled(cron = "0 0/3 * * * ?")
    @DistributedLock("downSampleHourly")
    public Object hourlyDownSample() throws Exception {
        //每小时 的第13分开始执行
        Date currentDate = new Date();
        Date executionDate = calculateGapDate(currentDate);
        String currentYear = DateUtils.format(YEAR, executionDate);
        //找缓存中是否有这个表,只有天表比较大，所以分表
        if (redis.get(DAILY_TABLE_PREFIX + currentYear) == null) {
            String tableName = DAILY_TABLE_PREFIX.concat(SymbolConstant.UNDERLINE).concat(currentYear);
            boolean hasTable = dbUtil.createAutoDailyDownSampleTable(DB_SCHEMA, tableName);
            if (!hasTable) {
                return null;
            }
            redis.set(DAILY_TABLE_PREFIX + currentYear, "1");
        }
//       five 执行用1个来做，即生产1条记录
        SparkApplicationParam param = initSparkParam(JobFrequencyConstant.HOURLY, executionDate, "1", "5", "5");
        param.setDriverMemory(driverMemoryHourly);
        param.setExecutorCores(executorCoresHourly);
        param.setExecutorMemory(executorMemoryHourly);
        return sparkSubmitService.submitApplication(param);
    }

    /**
     * 组装参数
     *
     * @param jobFrequencyType 频率
     * @param startDate        开始时间
     * @param five             任务个数
     * @param six              瞬时参数
     * @param seven            瞬时参数
     * @return
     */
    private SparkApplicationParam initSparkParam(String jobFrequencyType, Date startDate, String five, String six, String seven) {
        SparkApplicationParam param = new SparkApplicationParam();
        param.setMaster(sparkMaster);
        param.setMainClass(sparkMainClass);
        param.setJarPath(jarPath);
        String[] args = new String[10];
        Tuple2<String, String> tuple = TimeCalculator.getStartAndEndTimestamp(startDate, jobFrequencyType);
        logger.info("{} ..job.. start={}-------end={}", jobFrequencyType, tuple._1, tuple._2);
        //job频率
        args[0] = jobFrequencyType;
        //开始时间
        args[1] = tuple._1;
        //结束时间
        args[2] = tuple._2;
        //表格名称呢个
        args[3] = tableName;
        //要操作的列族
        args[4] = cf;
        //执行用1个来做，即生产1条记录
        args[5] = five;
        //分段瞬态法的特别参数
        args[6] = six;
        //分段瞬态法的特别参数
        args[7] = seven;
        //数据创建时间
        args[8] = String.valueOf(System.currentTimeMillis());
        //批量大小
        args[9] = BATCH_SIZE;
        param.setArgs(args);
        return param;
    }

    @Async
    @Scheduled(cron = "${config.cron.daily:0 23 1 * * ? }")
//    @Scheduled(cron = "0 0/23 * * * ?")
    @DistributedLock("downSampleDaily")
    public Object dailyDownSample() throws Exception {
        //每天1点23 执行
        Date currentDate = new Date();
        Date executionDate = calculateGapDate(currentDate);
        SparkApplicationParam param = initSparkParam(JobFrequencyConstant.DAILY, executionDate, "3", "5", "5");
        param.setDriverMemory(driverMemoryDaily);
        param.setExecutorCores(executorCoresDaily);
        param.setExecutorMemory(executorMemoryDaily);
        return sparkSubmitService.submitApplication(param);
    }

    @Async
    @Scheduled(cron = "${config.cron.weekly:0 37 2 ? * MON}")
//    @Scheduled(cron = "0 0/37 * * * ?")
    @DistributedLock("downSampleWeekly")
    public Object weeklyDownSample() throws Exception {
        //每周一的2点37分钟执行
        Date currentDate = new Date();
        Date executionDate = calculateGapDate(currentDate);
        SparkApplicationParam param = initSparkParam(JobFrequencyConstant.WEEKLY, executionDate, "12", "5", "5");
        param.setDriverMemory(driverMemoryWeekly);
        param.setExecutorCores(executorCoresWeekly);
        param.setExecutorMemory(executorMemoryWeekly);
        return sparkSubmitService.submitApplication(param);
    }

    /**
     * 每月1号的3点47执行
     *
     * @return
     * @throws Exception
     */
    @Async
    @Scheduled(cron = "${config.cron.monthly:0 47 3 1 * ? }")
//    @Scheduled(cron = "0 0/47 * * * ?")
    @DistributedLock("downSampleMonthly")
    public Object monthlyDownSample() throws Exception {
        //每月1号的3点47分开始执行
        Date currentDate = new Date();
        Date executionDate = calculateGapDate(currentDate);
        SparkApplicationParam param = initSparkParam(JobFrequencyConstant.MONTHLY, executionDate, "32", "5", "5");
        param.setDriverMemory(driverMemoryMonthly);
        param.setExecutorCores(executorCoresMonthly);
        param.setExecutorMemory(executorMemoryMonthly);
        return sparkSubmitService.submitApplication(param);
    }

    public Date calculateGapDate(Date date) {
        long ts = date.getTime() - Long.parseLong(initialGap);
        Date resDate = new Date(ts);
        return resDate;
    }
}
