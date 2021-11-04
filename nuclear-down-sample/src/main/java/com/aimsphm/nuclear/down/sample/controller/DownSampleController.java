package com.aimsphm.nuclear.down.sample.controller;

import com.aimsphm.nuclear.common.entity.bo.HistoryQueryFilledBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.enums.FrequencyEnum;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.down.sample.service.DownSampleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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
@RestController("sample")
public class DownSampleController {

    @Resource
    private DownSampleService downSampleService;


    @GetMapping(value = "hourly")
    @ApiOperation(value = "每小时执行")
    public void hourly(TimeRangeQueryBO rangTime) {
        if (Objects.isNull(rangTime) || Objects.isNull(rangTime.getStart())) {
            rangTime = new TimeRangeQueryBO(DateUtils.previousHourMinValue(), DateUtils.previousHourMaxValue());
        }
        downSampleService.executeDownSample(FrequencyEnum.HOURLY.getFrequency(), FrequencyEnum.HOURLY.getRate(), rangTime);
    }

    @GetMapping(value = "history/filled")
    @ApiOperation(value = "为历史查询进行补点操作")
    public List<List<Object>> hourly(HistoryQueryFilledBO bo) {
        return downSampleService.executeDownSample4HistoryData(bo);
    }

    @GetMapping(value = "daily")
    @ApiOperation(value = "每天执行", notes = "存储到周表，半月表，月表")
    public void daily(TimeRangeQueryBO rangTime) {
        if (Objects.isNull(rangTime) || Objects.isNull(rangTime.getStart())) {
            rangTime = new TimeRangeQueryBO(DateUtils.getStartOfPreviousDay(), DateUtils.getEndOfPreviousDay());
        }
        downSampleService.executeDownSample(FrequencyEnum.DAILY_60.getFrequency(), null, rangTime);
    }

    @GetMapping(value = "weekly")
    @ApiOperation(value = "每周执行", notes = "存储到季度表")
    public void weekly(TimeRangeQueryBO rangTime) {
        if (Objects.isNull(rangTime) || Objects.isNull(rangTime.getStart())) {
            rangTime = new TimeRangeQueryBO(DateUtils.getStartOfPreviousWeek(), DateUtils.getEndOfPreviousWeek());
        }
        downSampleService.executeDownSample(FrequencyEnum.WEEKLY_780.getFrequency(), null, rangTime);
    }

    @GetMapping(value = "monthly")
    @ApiOperation(value = "每月执行", notes = "存储到半年表，1年表，2年表，3年表")
    public void monthly(TimeRangeQueryBO rangTime) {
        if (Objects.isNull(rangTime) || Objects.isNull(rangTime.getStart())) {
            rangTime = new TimeRangeQueryBO(DateUtils.previousMonthFirstDay().getTime(), DateUtils.previousMonthLastDay().getTime());
        }
        downSampleService.executeDownSample(FrequencyEnum.MONTHLY_3155.getFrequency(), null, rangTime);
    }

    @GetMapping(value = "range/data")
    @ApiOperation(value = "从某个时间到某个时间全部执行", notes = "[时间范围不要太大]")
    public void allData(TimeRangeQueryBO rangeTime) {
        downSampleService.allData(rangeTime);
    }
}
