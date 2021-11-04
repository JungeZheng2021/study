package com.aimsphm.nuclear.down.sample.job;

import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.enums.FrequencyEnum;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.down.sample.service.DownSampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * 功能描述: 定时任务
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/11/01 15:13
 */
@Slf4j
@Component
public class DownSampleJob {

    @Resource
    private DownSampleService downSampleService;

    /**
     * 每小时执行,notes = "存储到天表"
     */
    @Scheduled(cron = "${config.cron.hourly:0 13 * * * ? }")
    @DistributedLock("DownSampleJob-hourly")
    public void hourly() {
        long start = System.currentTimeMillis();
        TimeRangeQueryBO rangTime = new TimeRangeQueryBO(DateUtils.previousHourMinValue(), DateUtils.previousHourMaxValue());
        downSampleService.executeDownSample(FrequencyEnum.HOURLY.getFrequency(), FrequencyEnum.HOURLY.getRate(), rangTime);
        log.info("...hourly...total cost：{}s", (System.currentTimeMillis() - start) / 1000);
    }

    /**
     * "每天执行", notes = "存储到周表，半月表，月表"
     */
    @Scheduled(cron = "${config.cron.daily:0 23 1 * * ? }")
    @DistributedLock("DownSampleJob-daily")
    public void daily() {
        long start = System.currentTimeMillis();
        TimeRangeQueryBO rangTime = new TimeRangeQueryBO(DateUtils.getStartOfPreviousDay(), DateUtils.getEndOfPreviousDay());
        downSampleService.executeDownSample(FrequencyEnum.DAILY_60.getFrequency(), null, rangTime);
        log.info("...daily...total cost：{}s", (System.currentTimeMillis() - start) / 1000);
    }

    /**
     * 每周执行, notes = "存储到季度表，半年表"
     */
    @Scheduled(cron = "${config.cron.daily:0 23 1 * * ? }")
    @DistributedLock("DownSampleJob-weekly")
    public void weekly() {
        long start = System.currentTimeMillis();
        TimeRangeQueryBO rangTime = new TimeRangeQueryBO(DateUtils.getStartOfPreviousWeek(), DateUtils.getEndOfPreviousWeek());
        downSampleService.executeDownSample(FrequencyEnum.WEEKLY_780.getFrequency(), null, rangTime);
        log.info("...weekly...total cost：{}s", (System.currentTimeMillis() - start) / 1000);
    }

    /**
     * 每月执行, notes = "存储到1年表，2年表，3年表
     */
    @Scheduled(cron = "${config.cron.daily:0 23 1 * * ? }")
    @DistributedLock("DownSampleJob-monthly")
    public void monthly() {
        long start = System.currentTimeMillis();
        TimeRangeQueryBO rangTime = new TimeRangeQueryBO(DateUtils.previousMonthFirstDay().getTime(), DateUtils.previousMonthLastDay().getTime());
        downSampleService.executeDownSample(FrequencyEnum.MONTHLY_3155.getFrequency(), null, rangTime);
        log.info("...monthly...total cost：{}s", (System.currentTimeMillis() - start) / 1000);
    }
}
