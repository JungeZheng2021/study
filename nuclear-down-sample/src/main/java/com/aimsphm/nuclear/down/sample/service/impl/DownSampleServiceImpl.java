package com.aimsphm.nuclear.down.sample.service.impl;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.SparkDownSampleConfigDO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQueryFilledBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.enums.FrequencyEnum;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.SparkDownSampleConfigService;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.down.sample.service.DownSampleService;
import com.aimsphm.nuclear.down.sample.service.ExecuteDownSampleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 功能描述:降采样实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/11/01 09:50
 */
@Slf4j
@Service
public class DownSampleServiceImpl implements DownSampleService {

    @Resource
    private Map<String, ExecuteDownSampleService> executeMap;

    @Resource
    private SparkDownSampleConfigService configService;
    @Resource
    private CommonMeasurePointService pointService;

    @Override
    public void executeDownSample(String type, Integer rate, TimeRangeQueryBO rangTime) {
        if (!checkParam(rate, rangTime)) {
            return;
        }
        List<SparkDownSampleConfigDO> spark = listDownSampleConfig(type, rate);
        if (CollectionUtils.isEmpty(spark)) {
            log.warn("down sample config list is null --> frequency:{},rate:{}", type, rate);
            return;
        }
        ExecuteDownSampleService execute = executeMap.get(type);
        if (Objects.isNull(execute)) {
            log.warn("unsupported this frequency --> frequency:{},rate:{}", type, rate);
            return;
        }
        execute.executeDownSample(spark, rangTime);
    }

    private boolean checkParam(Integer rate, TimeRangeQueryBO rangTime) {
        FrequencyEnum frequencyEnum = FrequencyEnum.getByRate(rate);
        if (Objects.isNull(frequencyEnum)) {
            log.error("unsupported this rate : {}", rate);
            return false;
        }
        return checkRangeTime(rangTime);
    }

    private boolean checkRangeTime(TimeRangeQueryBO rangTime) {
        boolean isNull = Objects.isNull(rangTime) || Objects.isNull(rangTime.getStart()) || Objects.isNull(rangTime.getEnd());
        if (isNull || rangTime.getEnd() <= rangTime.getStart()) {
            log.error("end time must be greater than start time:{}", rangTime);
            return false;
        }
        return true;
    }

    @Override
    public List<List<Object>> executeDownSample4HistoryData(HistoryQueryFilledBO bo) {
        if (!checkRangeTime(bo)) {
            return new ArrayList<>();
        }
        FrequencyEnum frequencyEnum = FrequencyEnum.getByTableName(bo.getTableName());
        if (Objects.isNull(frequencyEnum)) {
            log.error("unsupported this tableName：{}", bo.getTableName());
            return new ArrayList<>();
        }
        CommonMeasurePointDO point = pointService.getPointByPointId(bo.getPointId());
        if (Objects.isNull(point)) {
            log.warn("this pointId is not exist -> :{}", bo.getPointId());
            return new ArrayList<>();
        }
        List<SparkDownSampleConfigDO> configList = listDownSampleConfig(frequencyEnum, point.getSensorCode(), point.getFeature());
        if (CollectionUtils.isEmpty(configList)) {
            log.warn("down sample config list is null --> frequency:{},point:{}", frequencyEnum, bo.getPointId());
            return new ArrayList<>();
        }
        ExecuteDownSampleService execute = executeMap.get(frequencyEnum.getFrequency());
        if (Objects.isNull(execute)) {
            log.warn("unsupported this frequency --> frequency ", frequencyEnum);
            return new ArrayList<>();
        }
        return execute.executeDownSample4HistoryData(configList.get(0), bo, frequencyEnum);
    }

    public List<SparkDownSampleConfigDO> listDownSampleConfig(String type, Integer rate) {
        LambdaQueryWrapper<SparkDownSampleConfigDO> wrapper = Wrappers.lambdaQuery(SparkDownSampleConfigDO.class);
        wrapper.eq(SparkDownSampleConfigDO::getFrequency, type);
        if (Objects.nonNull(rate)) {
            wrapper.eq(SparkDownSampleConfigDO::getRate, rate);
        }
        return configService.list(wrapper);
    }

    public List<SparkDownSampleConfigDO> listDownSampleConfig(FrequencyEnum rate, String sensorCode, String feature) {
        LambdaQueryWrapper<SparkDownSampleConfigDO> wrapper = Wrappers.lambdaQuery(SparkDownSampleConfigDO.class);
        wrapper.eq(SparkDownSampleConfigDO::getFrequency, rate.getFrequency());
        wrapper.eq(SparkDownSampleConfigDO::getRate, rate.getRate());
        wrapper.eq(SparkDownSampleConfigDO::getSensorCode, sensorCode);
        if (StringUtils.isNotBlank(feature)) {
            wrapper.eq(SparkDownSampleConfigDO::getFeature, feature);
        }
        return configService.list(wrapper);
    }


    @Override
    public void allData(TimeRangeQueryBO rangeTime) {
        Long gap = 3600_000L;
        if (Objects.isNull(rangeTime.getEnd())) {
            rangeTime.setEnd(System.currentTimeMillis());
        }
        if (Objects.isNull(rangeTime.getStart()) || rangeTime.getStart() >= rangeTime.getEnd()) {
            log.warn("没有执行任何数据");
            return;
        }
        for (Long i = rangeTime.getStart() / gap * gap; i < rangeTime.getEnd(); i = i + gap) {
            hourly(i);
            if (DateUtils.isStartOfDay(i)) {
                daily(i);
            }
            if (DateUtils.isStartOfDay(i) && DateUtils.isStartOfWeek(i)) {
                weekly(i);
            }
            if (DateUtils.isStartOfDay(i) && DateUtils.isStartOfMonth(i)) {
                monthly(i);
            }
        }
    }

    private void hourly(Long start) {
        TimeRangeQueryBO rangTime = new TimeRangeQueryBO(DateUtils.getStartOfHour(start), DateUtils.getEndOfHour(start));
        this.executeDownSample(FrequencyEnum.HOURLY.getFrequency(), FrequencyEnum.HOURLY.getRate(), rangTime);
    }

    private void daily(Long start) {
        TimeRangeQueryBO rangTime = new TimeRangeQueryBO(DateUtils.getStartOfDay(start), DateUtils.getEndOfDay(start));
        this.executeDownSample(FrequencyEnum.DAILY_60.getFrequency(), null, rangTime);
    }

    private void weekly(Long start) {
        TimeRangeQueryBO rangTime = new TimeRangeQueryBO(DateUtils.getStartOfPreviousWeek(start), DateUtils.getEndOfPreviousWeek(start));
        this.executeDownSample(FrequencyEnum.WEEKLY_780.getFrequency(), null, rangTime);
    }

    private void monthly(Long start) {
        TimeRangeQueryBO rangTime = new TimeRangeQueryBO(DateUtils.previousMonthFirstDay(start).getTime(), DateUtils.previousMonthLastDay(start).getTime());
        log.info("{}~{}", new Date(rangTime.getStart()), new Date(rangTime.getEnd()));
        this.executeDownSample(FrequencyEnum.MONTHLY_3155.getFrequency(), null, rangTime);
    }
}
