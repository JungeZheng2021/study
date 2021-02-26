package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.algorithm.entity.bo.PointEstimateDataBO;
import com.aimsphm.nuclear.algorithm.util.WhetherTreadLocal;
import com.aimsphm.nuclear.common.config.DynamicTableTreadLocal;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.JobAlarmRealtimeDO;
import com.aimsphm.nuclear.common.entity.SparkDownSample;
import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleWithFeatureBO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.JobAlarmRealtimeService;
import com.aimsphm.nuclear.common.service.SparkDownSampleService;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.aimsphm.nuclear.history.entity.enums.TableNameEnum;
import com.aimsphm.nuclear.history.entity.vo.EventDataVO;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataWithThresholdVO;
import com.aimsphm.nuclear.history.service.HistoryQueryService;
import com.aimsphm.nuclear.history.util.TableNameParser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Get;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.*;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.*;

/**
 * @Package: com.aimsphm.nuclear.history.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/11/21 17:42
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/21 17:42
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service
public class HistoryQueryServiceImpl implements HistoryQueryService {

    @Resource
    private CommonMeasurePointService serviceExt;
    @Resource
    private JobAlarmRealtimeService realtimeService;
    @Resource
    private SparkDownSampleService downSampleServiceExt;

    private HBaseUtil hBase;

    public HistoryQueryServiceImpl(HBaseUtil hBase) {
        this.hBase = hBase;
    }

    @Override
    public List<List<Object>> listHistoryDataWithPointByScan(HistoryQuerySingleWithFeatureBO single) {

        String family = H_BASE_FAMILY_NPC_PI_REAL_TIME;
        if (StringUtils.isNotBlank(single.getFeature())) {
            family = single.getFeature();
        }
        try {
            return hBase.listDataWith3600Columns(H_BASE_TABLE_NPC_PHM_DATA, single.getSensorCode(), single.getStart(), single.getEnd(), family);
        } catch (IOException e) {
            throw new CustomMessageException("查询历史数据失败");
        }
    }

    @Override
    public HistoryDataVO listHistoryDataWithPointByScan(HistoryQuerySingleBO single) {
        if (Objects.isNull(single) || StringUtils.isBlank(single.getPointId())
                || Objects.isNull(single.getEnd()) || Objects.isNull(single.getStart()) || single.getEnd() <= single.getStart()) {
            return null;
        }
        CommonMeasurePointDO point = serviceExt.getPointByPointId(single.getPointId());
        if (Objects.isNull(point)) {
            throw new CustomMessageException("要查询的测点不存在");
        }
        Boolean needDownSample = serviceExt.isNeedDownSample(point);
        String tableName = TableNameParser.getTableName(single.getStart(), single.getEnd());
        //需要降采样 且时间区间大于3个小时
        if (needDownSample && StringUtils.isNotBlank(tableName)) {
            HistoryQueryMultiBO multi = new HistoryQueryMultiBO();
            multi.setPointIds(Lists.newArrayList(single.getPointId()));
            BeanUtils.copyProperties(single, multi);
            Map<String, HistoryDataVO> data = listHistoryDataWithPointIdsFromMysql(multi);
            return data.get(single.getPointId());
        }
        HistoryQuerySingleWithFeatureBO featureBO = new HistoryQuerySingleWithFeatureBO();
        if (point.getPointType() != 1 && StringUtils.isNotBlank(point.getFeature())) {
            featureBO.setFeature(point.getFeatureType().concat(DASH).concat(point.getFeature()));
        }
        featureBO.setSensorCode(point.getSensorCode());
        BeanUtils.copyProperties(single, featureBO);
        List<List<Object>> dataDTOS = listHistoryDataWithPointByScan(featureBO);
        HistoryDataVO vo = new HistoryDataWithThresholdVO();
        if (Objects.isNull(WhetherTreadLocal.INSTANCE.getWhether()) || WhetherTreadLocal.INSTANCE.getWhether()) {
            BeanUtils.copyProperties(point, vo);
            WhetherTreadLocal.INSTANCE.remove();
        }
        vo.setChartData(dataDTOS);
        return vo;
    }

    @Override
    public Map<String, HistoryDataVO> listHistoryDataWithPointIdsByScan(HistoryQueryMultiBO multi) {
        Map<String, HistoryDataVO> data = new HashMap<>(16);
        checkParam(multi);
        //如果表格是空说明是三个小时以内，要查询原始数据
        Long end = multi.getEnd();
        Long start = multi.getStart();
        String tableName = TableNameParser.getTableName(start, end);
        List<String> pointIds = multi.getPointIds();
        //全部非降采样
        if (StringUtils.isBlank(tableName)) {
            multiPointHBaseData(data, pointIds, start, end);
            return data;
        }
        List<String> downSamplePoints = pointIds.stream().filter(pointId -> serviceExt.isNeedDownSample(pointId)).collect(Collectors.toList());
        List<String> noDownSamplePoints = pointIds.stream().filter(pointId -> !serviceExt.isNeedDownSample(pointId)).collect(Collectors.toList());
        multi.setPointIds(downSamplePoints);
        Map<String, HistoryDataVO> map = listHistoryDataWithPointIdsFromMysql(multi);
        multiPointHBaseData(data, noDownSamplePoints, start, end);
        data.putAll(map);
        return data;
    }

    private void multiPointHBaseData(Map<String, HistoryDataVO> data, List<String> pointIds, Long start, Long end) {
        for (String pointId : pointIds) {
            HistoryQuerySingleBO bo = new HistoryQuerySingleBO();
            bo.setPointId(pointId);
            bo.setEnd(end);
            bo.setStart(start);
            data.put(pointId, listHistoryDataWithPointByScan(bo));
        }
    }


    private Map<String, HistoryDataVO> listHistoryDataWithPointIdsFromMysql(HistoryQueryMultiBO multi) {
        Map<String, HistoryDataVO> result = Maps.newHashMap();
        List<SparkDownSample> list = listSparkDownSampleByConditions(multi);
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> collect = list.stream().filter(item -> StringUtils.isNotBlank(item.getPoints()) && StringUtils.isNotBlank(item.getPointId()))
                .collect(Collectors.groupingBy(item -> item.getPointId().trim(), Collectors.mapping(item -> item.getPoints(), Collectors.toList())));
        for (String pointId : multi.getPointIds()) {
            CommonMeasurePointDO point = serviceExt.getPointByPointId(pointId);
            if (Objects.isNull(point) || CollectionUtils.isEmpty(collect.get(pointId))) {
                continue;
            }
            List<String> points = collect.get(pointId);
            String collect1 = points.stream().collect(Collectors.joining(COMMA));
            HistoryDataVO vo = new HistoryDataWithThresholdVO();
            if (Objects.isNull(WhetherTreadLocal.INSTANCE.getWhether()) || WhetherTreadLocal.INSTANCE.getWhether()) {
                BeanUtils.copyProperties(point, vo);
                WhetherTreadLocal.INSTANCE.remove();
            }
            try {
                List<List<Object>> charData = mapper.readValue(LEFT_SQ_BRACKET + collect1 + RIGHT_SQ_BRACKET, List.class);
                List<List<Object>> collect2 = charData.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(x -> (Long) x.get(0)))), ArrayList::new));
                vo.setChartData(collect2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            result.put(pointId, vo);
        }
        return result;
    }

    private List<SparkDownSample> listSparkDownSampleByConditions(HistoryQueryMultiBO multi) {
        Long end = multi.getEnd();
        Long start = multi.getStart();
        String tableName = TableNameParser.getTableName(start, end);
        if (StringUtils.isBlank(tableName) || CollectionUtils.isEmpty(multi.getPointIds())) {
            return null;
        }
        //如果是天表[天表根据年份分表-需要将跨跃2年的部分拼接起来]
        if (TableNameEnum.DAILY.getValue().equals(tableName)) {
            int endYear = DateUtils.transition(new Date(end)).getYear();
            int startYear = DateUtils.transition(new Date(start)).getYear();
            List<SparkDownSample> list = Lists.newArrayList();
            for (int year = startYear; year <= endYear; year++) {
                resetTableName(tableName, year);
                list.addAll(listSparkDownSampleByPointIdsAndRangeTime(multi.getPointIds(), start, end));
            }
            return list;
        }
        resetTableName(tableName, null);
        return listSparkDownSampleByPointIdsAndRangeTime(multi.getPointIds(), start, end);
    }

    private List<SparkDownSample> listSparkDownSampleByPointIdsAndRangeTime(List<String> pointIds, Long start, Long end) {
        LambdaQueryWrapper<SparkDownSample> wrapper = Wrappers.lambdaQuery(SparkDownSample.class);
        wrapper.in(SparkDownSample::getPointId, pointIds).ge(SparkDownSample::getStartTimestamp, start)
                .le(SparkDownSample::getStartTimestamp, end).orderByAsc(SparkDownSample::getStartTimestamp);
        return downSampleServiceExt.list(wrapper);
    }

    private void resetTableName(String tableName, Integer year) {
        if (Objects.isNull(year)) {
            DynamicTableTreadLocal.INSTANCE.setTableName(tableName);
            return;
        }
        DynamicTableTreadLocal.INSTANCE.setTableName(tableName + UNDERLINE + year);
    }

    @Override
    public Map<String, HistoryDataVO> listHistoryDataWithPointIdsByGetList(HistoryQueryMultiBO multi) {
        Map<String, HistoryDataVO> result = new HashMap<>(16);
        if (Objects.isNull(multi) || CollectionUtils.isEmpty(multi.getPointIds())
                || Objects.isNull(multi.getEnd()) || Objects.isNull(multi.getStart()) || multi.getEnd() <= multi.getStart()) {
            return result;
        }
        List<Get> getList = initGetListByConditions(multi);
        try {
//            Map<String, List<HBaseTimeSeriesDataDTO>> data = hBase.selectDataList(H_BASE_TABLE_NPC_PHM_DATA, getList);
            multi.getPointIds().stream().forEach(item -> {
                CommonMeasurePointDO point = serviceExt.getPointByPointId(item);
                HistoryDataVO vo = new HistoryDataWithThresholdVO();
                BeanUtils.copyProperties(point, vo);
                List<Object> list = Lists.newArrayList(System.currentTimeMillis(), new Random().nextDouble());
                List<List<Object>> data = new ArrayList<>();
                data.add(list);
                vo.setChartData(data);
                ArrayList<Long> list1 = Lists.newArrayList(1L, 3L, 4L);
                result.putIfAbsent(item, vo);
            });
        } catch (Exception e) {
            throw new CustomMessageException("查询历史数据失败");
        }
        return result;
    }

    @Override
    public Map<String, EventDataVO> listDataWithPointList(HistoryQueryMultiBO multi) {
        checkParam(multi);
        HashMap<String, EventDataVO> data = Maps.newHashMap();
        List<String> pointIds = multi.getPointIds();
        try {
            List<PointEstimateDataBO> collect = hBase.selectModelDataList(H_BASE_TABLE_NPC_PHM_DATA, multi.getStart(), multi.getEnd(), H_BASE_FAMILY_NPC_ESTIMATE, pointIds, multi.getModelId());
            if (CollectionUtils.isEmpty(collect)) {
                return null;
            }
            Map<String, List<PointEstimateDataBO>> collect1 = collect.stream().collect(Collectors.groupingBy(x -> x.getPointId()));
            pointIds.stream().forEach(pointId -> {
                EventDataVO vo = new EventDataVO();
                CommonMeasurePointDO point = serviceExt.getPointByPointId(pointId);
                if (Objects.isNull(point)) {
                    return;
                }
                BeanUtils.copyProperties(point, vo);
                List<PointEstimateDataBO> v = collect1.get(pointId);
                List actualData = v.stream().map(item -> Lists.newArrayList(item.getTimestamp(), item.getActual())).collect(Collectors.toList());
                List estimatedData = v.stream().map(item -> Lists.newArrayList(item.getTimestamp(), item.getEstimate())).collect(Collectors.toList());
                List residualData = v.stream().map(item -> Lists.newArrayList(item.getTimestamp(), item.getResidual())).collect(Collectors.toList());
                vo.setActualData(actualData);
                vo.setEstimatedData(estimatedData);
                vo.setResidualData(residualData);
                List<JobAlarmRealtimeDO> realtimeList = realtimeService.listRealTime(pointId, multi.getStart(), multi.getEnd(), multi.getModelId());
                if (CollectionUtils.isNotEmpty(realtimeList)) {
                    vo.setAlarmData(realtimeList.stream().map(x -> x.getGmtAlarmTime().getTime()).collect(Collectors.toList()));
                }
                data.put(pointId, vo);
            });
        } catch (IOException e) {
            log.error("select Estimate data failed..{}", e);
        }
        return data;
    }

    private void checkParam(HistoryQueryMultiBO multi) {
        if (Objects.isNull(multi) || CollectionUtils.isEmpty(multi.getPointIds())
                || Objects.isNull(multi.getEnd()) || Objects.isNull(multi.getStart()) || multi.getEnd() <= multi.getStart()) {
            throw new CustomMessageException("some parameter is missing");
        }
    }

    private List<Get> initGetListByConditions(HistoryQueryMultiBO multi) {
        List<String> pointIds = multi.getPointIds();
        Long end = multi.getEnd();
        Long start = multi.getStart();

        return null;
    }
}
