package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.config.DynamicTableTreadLocal;
import com.aimsphm.nuclear.common.entity.SparkDownSample;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleWithFeatureBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.enums.TableNameEnum;
import com.aimsphm.nuclear.common.mapper.SparkDownSampleMapper;
import com.aimsphm.nuclear.common.service.SparkDownSampleService;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.common.util.TableNameParser;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.SymbolConstant.*;

/**
 * <p>
 * 功能描述:降采样扩展服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-12-14 14:30
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class SparkDownSampleServiceImpl extends ServiceImpl<SparkDownSampleMapper, SparkDownSample> implements SparkDownSampleService {


    @Override
    public Map<String, List<SparkDownSample>> listDataFromDailyTable(List<String> pointIds, Long start, Long end) {
        int endYear = DateUtils.transition(new Date(end)).getYear();
        int startYear = DateUtils.transition(new Date(start)).getYear();
        Map<String, List<SparkDownSample>> result = Maps.newHashMap();
        for (int year = startYear; year <= endYear; year++) {
            DynamicTableTreadLocal.INSTANCE.tableName(TableNameEnum.DAILY.getValue() + UNDERLINE + year);
            result.putAll(listDataByRangeTime(pointIds, start, end));
        }
        return result;
    }

    @Override
    public List<List<Object>> listDataFromDailyData(HistoryQuerySingleWithFeatureBO pointInfo) {
        ObjectMapper mapper = new ObjectMapper();
        String pointId = StringUtils.isNotBlank(pointInfo.getFeature()) ? pointInfo.getSensorCode() + DASH + pointInfo.getFeature() : pointInfo.getSensorCode();
        List<String> list = Lists.newArrayList(StringUtils.isNotBlank(pointInfo.getFeature()) ? pointInfo.getSensorCode() + DASH + pointInfo.getFeature() : pointInfo.getSensorCode());
        Map<String, List<SparkDownSample>> data = this.listDataFromDailyTable(list, pointInfo.getStart(), pointInfo.getEnd());
        List<SparkDownSample> sparkDownSamples = data.get(pointId);
        if (CollectionUtils.isEmpty(sparkDownSamples)) {
            log.warn("daily data is null :{},start:{} end:{}", pointInfo, DateUtils.format(pointInfo.getStart()), DateUtils.format(pointInfo.getEnd()));
            return null;
        }
        List<String> points = sparkDownSamples.stream().filter(x -> StringUtils.isNotBlank(x.getPoints())).map(SparkDownSample::getPoints).collect(Collectors.toList());
        try {
            String collect1 = points.stream().collect(Collectors.joining(COMMA));
            List<List<Object>> charData = mapper.readValue(LEFT_SQ_BRACKET + collect1 + RIGHT_SQ_BRACKET, List.class);
            return charData.stream().filter(x -> Objects.nonNull(x.get(0))).collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(x -> (Long) x.get(0)))), ArrayList::new));
        } catch (IOException e) {
            log.error("get failed:{}", e);
        }
        return null;
    }

    @Override
    public Map<String, List<SparkDownSample>> listDataByRangeTime(List<String> pointIds, Long start, Long end) {
        Map<String, List<SparkDownSample>> result = Maps.newHashMap();
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return result;
        }
        TimeRangeQueryBO rangeTime = TableNameParser.getRangeTime(start, end);
        //开始时间计算成当前时间的小时值
        for (String pointId : pointIds) {
            LambdaQueryWrapper<SparkDownSample> wrapper = Wrappers.lambdaQuery(SparkDownSample.class);
            wrapper.eq(SparkDownSample::getPointId, pointId).ge(SparkDownSample::getStartTimestamp, rangeTime.getStart())
                    .le(SparkDownSample::getStartTimestamp, end).orderByAsc(SparkDownSample::getStartTimestamp);
            List<SparkDownSample> list = this.list(wrapper);
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }
            SparkDownSample first = list.get(0);
            //截取开头数据
            subPointsByStartAndEnd(start, end, first);
            if (list.size() > 1) {
                SparkDownSample last = list.get(list.size() - 1);
                //截取结束数据
                subPointsByStartAndEnd(start, end, last);
            }
            result.put(pointId, list);
        }
        return result;
    }

    /**
     * 根据时间截取开头和结尾数据
     *
     * @param start  开始时间
     * @param end    结束时间
     * @param sample 将采样对象
     */
    private void subPointsByStartAndEnd(Long start, Long end, SparkDownSample sample) {
        try {
            String points = sample.getPoints();
            if (StringUtils.isEmpty(points)) {
                sample.setPoints(LEFT_SQ_BRACKET + RIGHT_SQ_BRACKET);
                return;
            }
            List<List> lists = JSON.parseArray(LEFT_SQ_BRACKET + points + RIGHT_SQ_BRACKET, List.class);
            String collect = lists.stream().filter(x -> peakItem(x, start, end)).map(d -> Arrays.toString(d.toArray())).collect(Collectors.joining(COMMA));
            sample.setPoints(collect);
        } catch (Exception e) {
            log.error("down sample query error,{}", e);
            sample.setPoints(LEFT_SQ_BRACKET + RIGHT_SQ_BRACKET);
        }
    }

    private boolean peakItem(List x, Long start, Long end) {
        if (Objects.nonNull(start) && Objects.nonNull(end)) {
            return (Long) x.get(0) >= start && (Long) x.get(0) <= end;
        }
        if (Objects.nonNull(start)) {
            return (Long) x.get(0) >= start;
        }
        if (Objects.nonNull(end)) {
            return (Long) x.get(0) <= end;
        }
        return false;
    }
}
