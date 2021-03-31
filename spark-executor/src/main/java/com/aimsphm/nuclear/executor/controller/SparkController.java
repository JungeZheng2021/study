package com.aimsphm.nuclear.executor.controller;

import com.aimsphm.nuclear.executor.bo.TestReqParam;
import com.aimsphm.nuclear.executor.constant.TimeUnitConstant;
import com.aimsphm.nuclear.executor.entity.SparkApplicationParam;
import com.aimsphm.nuclear.executor.job.DownSampleScheduleJobManually;
import com.aimsphm.nuclear.executor.service.ISparkSubmitService;
import com.aimsphm.nuclear.executor.util.TimeCalculator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import scala.Tuple2;

import javax.annotation.Resource;
import java.io.IOException;


@Slf4j
@Api(description = "Rest Spark Submit")
@Controller
@RequestMapping("/spark")
public class SparkController {
    @Resource
    private ISparkSubmitService iSparkSubmitService;

    @Resource
    DownSampleScheduleJobManually testService;

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
            e.printStackTrace();
            log.error("执行出错：{}", e.getMessage());
        }
        return null;
    }


    @ResponseBody
    @PostMapping("/doDailyBatch")
    @ApiOperation(value = "batch daily execution", notes = "na")

    public Object testDaily(@RequestBody TestReqParam reqParam) throws Exception {

        long startTs = reqParam.getStartTimestamp() - reqParam.getStartTimestamp() % 3600000;
        long loops = (reqParam.getEndTimestamp() - startTs) / TimeUnitConstant.daysOnMills;
        for (int i = 0; i < loops; i++) {
            long stepStart = startTs + i * TimeUnitConstant.daysOnMills;
            long stepEnd = stepStart + (i + 1) * TimeUnitConstant.daysOnMills;
            Object code = testService.dailyDownSample(stepStart, stepEnd);
            Thread.sleep(30000);
        }


        return null;

    }

    @ResponseBody
    @PostMapping("/doWeeklyBatch")
    @ApiOperation(value = "batch weekly execution", notes = "na")

    public Object testWeekly(@RequestBody TestReqParam reqParam) throws Exception {

        long startTs = reqParam.getStartTimestamp() - reqParam.getStartTimestamp() % 3600000;
        long loops = (reqParam.getEndTimestamp() - startTs) / TimeUnitConstant.weekInMills;
        for (int i = 0; i < loops; i++) {
            long stepStart = startTs + i * TimeUnitConstant.weekInMills;
            long stepEnd = stepStart + (i + 1) * TimeUnitConstant.weekInMills;
            Object code = testService.weeklyDownSample(stepStart, stepEnd);
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
            Thread.sleep(30000);
        }


        return null;

    }

    @ResponseBody
    @PostMapping("/doHourlyBatch")
    @ApiOperation(value = "batch hourly execution", notes = "na")
    public Object downSampleHistoryDailyBatch(@RequestBody TestReqParam reqParam) throws Exception {
        long startTs = reqParam.getStartTimestamp() - reqParam.getStartTimestamp() % TimeUnitConstant.HourInMills;
        long loops = (reqParam.getEndTimestamp() - startTs) / TimeUnitConstant.HourInMills;
        for (int i = 0; i < loops; i++) {
            long stepStart = startTs + i * TimeUnitConstant.HourInMills;
            long stepEnd = stepStart;
            Object code = testService.hourlyDownSample(stepStart, stepEnd);
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
        return null;

    }
}

