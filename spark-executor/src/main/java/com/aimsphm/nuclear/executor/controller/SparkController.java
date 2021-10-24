package com.aimsphm.nuclear.executor.controller;

import com.aimsphm.nuclear.executor.bo.TestReqParam;
import com.aimsphm.nuclear.executor.constant.TimeUnitConstant;
import com.aimsphm.nuclear.executor.entity.SparkApplicationParam;
import com.aimsphm.nuclear.executor.job.DownSampleScheduleJob;
import com.aimsphm.nuclear.executor.job.DownSampleScheduleJobManually;
import com.aimsphm.nuclear.executor.service.ISparkSubmitService;
import com.aimsphm.nuclear.executor.util.TimeCalculator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import scala.Tuple2;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * @author MILLA
 */
@Slf4j
@Api(tags = "Rest Spark Submit")
@Controller
@RequestMapping("/spark")
public class SparkController {
    @Resource
    private ISparkSubmitService iSparkSubmitService;

    @Resource
    DownSampleScheduleJobManually testService;
    @Resource
    private DownSampleScheduleJob job;

    @GetMapping("/exec/{type}")
    public Object exec(@PathVariable Integer type) {
        try {
            if (type == 1) {
                return job.hourlyDownSample();
            }
            if (type == 2) {
                return job.dailyDownSample();
            }
            if (type == 3) {
                return job.weeklyDownSample();
            }
            if (type == 4) {
                return job.monthlyDownSample();
            }
        } catch (Exception e) {
            log.error("get failed:{}", e);
        }

        return null;
    }

    /**
     * 调用service进行远程提交spark任务
     *
     * @return 执行结果
     */
    @ResponseBody
    @PostMapping("/submit")
    @ApiOperation(value = "submit the task", notes = "this will submit the job into spark standalone cluster")

    public Object downSampleSubmit(@RequestBody SparkApplicationParam param) {
        try {
            return iSparkSubmitService.submitApplication(param.getSparkApplicationParam());
        } catch (IOException | InterruptedException e) {
            log.error("执行出错：{}", e.getMessage());
        }
        return null;
    }


    @ResponseBody
    @PostMapping("/doDailyBatch")
    @ApiOperation(value = "batch daily execution", notes = "na")

    public Object testDaily(@RequestBody TestReqParam reqParam) throws Exception {

        long startTs = reqParam.getStartTimestamp() - reqParam.getStartTimestamp() % 3600000;
        long loops = (reqParam.getEndTimestamp() - startTs) / TimeUnitConstant.DAYS_ON_MILLS;
        for (int i = 0; i < loops; i++) {
            long stepStart = startTs + i * TimeUnitConstant.DAYS_ON_MILLS;
            long stepEnd = stepStart + (i + 1) * TimeUnitConstant.DAYS_ON_MILLS;
            Object code = testService.dailyDownSample(stepStart, stepEnd);
            log.debug("{}", code);
            Thread.sleep(30000);
        }

        return null;

    }

    @ResponseBody
    @PostMapping("/doWeeklyBatch")
    @ApiOperation(value = "batch weekly execution", notes = "na")

    public Object testWeekly(@RequestBody TestReqParam reqParam) throws Exception {

        long startTs = reqParam.getStartTimestamp() - reqParam.getStartTimestamp() % 3600000;
        long loops = (reqParam.getEndTimestamp() - startTs) / TimeUnitConstant.WEEK_IN_MILLS;
        for (int i = 0; i < loops; i++) {
            long stepStart = startTs + i * TimeUnitConstant.WEEK_IN_MILLS;
            long stepEnd = stepStart + (i + 1) * TimeUnitConstant.WEEK_IN_MILLS;
            Object code = testService.weeklyDownSample(stepStart, stepEnd);
            log.debug("{}", code);
            Thread.sleep(30000);
        }

        return null;

    }


    @ResponseBody
    @PostMapping("/doMonthlyBatch")
    @ApiOperation(value = "batcth monthly execution", notes = "na")

    public Object testMonthly(@RequestBody TestReqParam reqParam) throws Exception {

        long startTs = reqParam.getStartTimestamp() - reqParam.getStartTimestamp() % 3600000;
        long loops = reqParam.getLoop();
        long tempTs = startTs;
        for (int i = 0; i < loops; i++) {
            Tuple2<String, String> pair = null;

            pair = TimeCalculator.getStartAndEndTimestampForMonth(tempTs);

            long stepStart = Long.parseLong(pair._1);
            long stepEnd = Long.parseLong(pair._2);
            tempTs = stepEnd;
            Object code = testService.monthlyDownSample(stepStart, stepEnd);
            log.debug("{}", code);
            Thread.sleep(30000);
        }


        return null;

    }

    @ResponseBody
    @PostMapping("/doHourlyBatch")
    @ApiOperation(value = "batch hourly execution", notes = "na")
    public Object downSampleHistoryDailyBatch(@RequestBody TestReqParam reqParam) throws Exception {
        long startTs = reqParam.getStartTimestamp() - reqParam.getStartTimestamp() % TimeUnitConstant.HOUR_IN_MILLS;
        long loops = (reqParam.getEndTimestamp() - startTs) / TimeUnitConstant.HOUR_IN_MILLS;
        for (int i = 0; i < loops; i++) {
            long stepStart = startTs + i * TimeUnitConstant.HOUR_IN_MILLS;
            long stepEnd = stepStart;
            Object code = testService.hourlyDownSample(stepStart, stepEnd);
            log.debug("{}", code);
            Thread.sleep(30000);
        }
        return null;

    }


    @ResponseBody
    @PostMapping("/doBatchSingleJob")
    @ApiOperation(value = "do batch donwsample with single job ", notes = "na")
    public Object testSingleJOb(@RequestBody TestReqParam reqParam) throws Exception {

        long startTs = reqParam.getStartTimestamp();
        long endTs = reqParam.getEndTimestamp();
        String freq = reqParam.getFreq();
        int partition = reqParam.getPartitions();
        Object code = testService.downSampleSingleJob(freq, startTs, endTs, partition);
        log.debug("{}", code);
        return code;

    }
}

