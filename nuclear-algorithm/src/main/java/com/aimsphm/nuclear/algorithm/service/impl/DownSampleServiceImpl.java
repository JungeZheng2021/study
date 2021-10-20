package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.service.DownSampleService;
import com.aimsphm.nuclear.common.entity.AlgorithmPrognosticFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.JobDownSampleDO;
import com.aimsphm.nuclear.common.enums.TimeUnitEnum;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.service.AlgorithmPrognosticFaultFeatureService;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.JobDownSampleService;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_FAMILY_NPC_PI_REAL_TIME;
import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_TABLE_NPC_PHM_DATA;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.DASH;

/**
 * <p>
 * 功能描述:降采样实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/07/27 14:00
 */
@Slf4j
@Service
public class DownSampleServiceImpl implements DownSampleService {
    @Resource
    private AlgorithmPrognosticFaultFeatureService prognosticFaultFeatureService;
    @Resource
    private CommonMeasurePointService pointService;
    @Resource
    private JobDownSampleService downSampleService;
    @Resource
    private HBaseUtil hBase;

    @Override
    public void execute() {
        executeOnce(new Date(), -1L);
    }

    @Override
    public void executeOnce(Date date, Long previousHours) {
        List<AlgorithmPrognosticFaultFeatureDO> list = prognosticFaultFeatureService.list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //结束时间
        Long end = DateUtils.plusHoursMaxValue(previousHours, date);
        //开始时间
        Long start = DateUtils.plusHoursMinValue(previousHours, date);
        Set<String> distinctPoints = Sets.newHashSet();
        list.forEach(x -> {
            String pointId = x.getSensorDesc();
            String key = x.getComponentId() + pointId;
            if (distinctPoints.contains(key)) {
                return;
            }
            distinctPoints.add(key);
            CommonMeasurePointDO point = pointService.getPointByPointId(pointId);
            String timeRange = x.getTimeRange();
            String timeGap = x.getTimeGap();
            Long timeGapValue = TimeUnitEnum.getGapValue(timeGap);
            Long timeRangeValue = TimeUnitEnum.getGapValue(timeRange);
            if (StringUtils.isBlank(pointId) || Objects.isNull(timeGapValue) || Objects.isNull(timeRangeValue)) {
                log.warn("this config is useless:{}", x);
                return;
            }
            //1个小时内需要取的点数
            long timesInHour = TimeUnitEnum.HOUR.getValue() / timeGapValue;
            List<List<Object>> lists = listHistoryDataByScan(point, start, end, (int) timesInHour);
            log.debug("query result:{}", JSON.toJSONString(lists));
            long arraySize = timeRangeValue / TimeUnitEnum.HOUR.getValue() * timesInHour;
            LambdaQueryWrapper<JobDownSampleDO> w = Wrappers.lambdaQuery(JobDownSampleDO.class);
            w.eq(JobDownSampleDO::getPointId, pointId).eq(JobDownSampleDO::getComponentId, x.getComponentId());
            w.last("limit 1");
            JobDownSampleDO one = downSampleService.getOne(w);
            if (Objects.isNull(one)) {
                one = new JobDownSampleDO();
                one.setPointId(pointId);
                one.setComponentId(x.getComponentId());
            }
            ArrayDeque queue = Objects.isNull(one.getData()) ? new ArrayDeque() : JSON.parseObject(one.getData(), ArrayDeque.class);
            for (List<Object> obj : lists) {
                if (containsItems(queue, obj)) {
                    log.debug("包含指定的数值：{}", obj);
                    continue;
                }
                if (queue.size() < arraySize) {
                    queue.add(obj);
                    continue;
                }
                queue.removeFirst();
                queue.add(obj);
            }
            String dataNew = JSON.toJSONString(queue.toArray());
            LambdaUpdateWrapper<JobDownSampleDO> update = Wrappers.lambdaUpdate(JobDownSampleDO.class);
            update.eq(JobDownSampleDO::getPointId, pointId).eq(JobDownSampleDO::getComponentId, x.getComponentId());
            one.setData(dataNew);
            one.setGmtModified(new Date());
            downSampleService.saveOrUpdate(one, update);
        });
    }

    private boolean containsItems(ArrayDeque queue, List<Object> obj) {
        return JSON.toJSONString(queue.toArray()).contains(JSON.toJSONString(obj));
    }

    public List<List<Object>> listHistoryDataByScan(CommonMeasurePointDO point, Long startTime, Long endTime, Integer timesInHour) {
        log.debug("query params: start:{},end:{},times:{},pointId:{}", DateUtils.format(DateUtils.YEAR_MONTH_DAY_HH_MM_SS_SSS_M, startTime), DateUtils.format(DateUtils.YEAR_MONTH_DAY_HH_MM_SS_SSS_M, endTime), timesInHour, point.getPointId());
        String family = H_BASE_FAMILY_NPC_PI_REAL_TIME;
        if (StringUtils.isNotBlank(point.getFeatureType()) && StringUtils.isNotBlank(point.getFeature())) {
            family = point.getFeatureType() + DASH + point.getFeature();
        }
        try {
            List<List<Object>> lists = hBase.listDataWith3600Columns(H_BASE_TABLE_NPC_PHM_DATA, point.getSensorCode(), startTime, endTime, family);
            if (CollectionUtils.isEmpty(lists)) {
                return IntStream.range(0, timesInHour).mapToObj(x -> Lists.newArrayList(startTime + x * 3600 * 1000 / timesInHour, (Object) null)).collect(Collectors.toList());
            }
            ArrayList<List<Object>> data = Lists.newArrayList();
            for (int i = 0, len = lists.size(); i < len; ) {
                List<Object> objects = lists.get(i);
                data.add(objects);
                i = i + (lists.size() <= timesInHour ? 1 : lists.size() / timesInHour);
            }
            return data;
        } catch (IOException e) {
            throw new CustomMessageException("查询历史数据失败");
        }
    }
}