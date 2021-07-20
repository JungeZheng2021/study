package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.algorithm.entity.bo.PointEstimateDataBO;
import com.aimsphm.nuclear.algorithm.util.RawDataThreadLocal;
import com.aimsphm.nuclear.common.config.DynamicTableTreadLocal;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.SparkDownSample;
import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleWithFeatureBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.entity.vo.EventDataVO;
import com.aimsphm.nuclear.common.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.common.entity.vo.HistoryDataWithThresholdVO;
import com.aimsphm.nuclear.common.enums.PointTypeEnum;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.SparkDownSampleService;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.aimsphm.nuclear.history.entity.enums.TableNameEnum;
import com.aimsphm.nuclear.history.service.HistoryQueryService;
import com.aimsphm.nuclear.history.util.TableNameParser;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Get;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
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
    private SparkDownSampleService downSampleServiceExt;

    private HBaseUtil hBase;

    public HistoryQueryServiceImpl(HBaseUtil hBase) {
        this.hBase = hBase;
    }

    /**
     * 默认超过2分钟需要补点
     */
    @Value("${time.interval:120}")
    private Long timeInterval;

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
        CommonMeasurePointDO point = checkParam(single);
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
        return listHistoryDataFromHBaseByPoint(single, point);
    }

    public HistoryDataVO listHistoryDataFromHBase(HistoryQuerySingleBO single) {
        try {
            CommonMeasurePointDO point = checkParam(single);
            return listHistoryDataFromHBaseByPoint(single, point);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private HistoryDataVO listHistoryDataFromHBaseByPoint(HistoryQuerySingleBO single, CommonMeasurePointDO point) {
        HistoryDataVO vo = new HistoryDataWithThresholdVO();
        HistoryQuerySingleWithFeatureBO featureBO = new HistoryQuerySingleWithFeatureBO();
        //是否为PI测点 pi测点设置PointId
        boolean notPIPoint = !PointTypeEnum.PI.getValue().equals(point.getPointType()) && StringUtils.isNotBlank(point.getFeature());
        featureBO.setFeature(notPIPoint ? point.getFeatureType().concat(DASH).concat(point.getFeature()) : null);
        featureBO.setSensorCode(notPIPoint ? point.getSensorCode() : point.getPointId());
        BeanUtils.copyProperties(single, featureBO);
        List<List<Object>> dataDTOS = listHistoryDataWithPointByScan(featureBO);
//        if (Objects.isNull(WhetherThreadLocal.INSTANCE.getWhether()) || WhetherThreadLocal.INSTANCE.getWhether()) {
        BeanUtils.copyProperties(point, vo);
//            WhetherThreadLocal.INSTANCE.remove();
//        }
        vo.setChartData(dataDTOS);
        return vo;
    }

    private CommonMeasurePointDO checkParam(HistoryQuerySingleBO single) {
        if (Objects.isNull(single) || StringUtils.isBlank(single.getPointId())
                || Objects.isNull(single.getEnd()) || Objects.isNull(single.getStart()) || single.getEnd() <= single.getStart()) {
            throw new CustomMessageException("参数异常");
        }
        CommonMeasurePointDO point = serviceExt.getPointByPointId(single.getPointId());
        if (Objects.isNull(point)) {
            throw new CustomMessageException("要查询的测点不存在");
        }
        return point;
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
        Boolean whether = RawDataThreadLocal.INSTANCE.getWhether();
        //全部非降采样 表格为空或者是需要查询所有额原始数据
        boolean needlessDownSample = StringUtils.isBlank(tableName) || (Objects.nonNull(whether) && whether);
        if (needlessDownSample) {
            multiPointHBaseData(data, pointIds, start, end);
            RawDataThreadLocal.INSTANCE.remove();
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
            data.put(pointId, listHistoryDataFromHBase(bo));
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
//            if (Objects.isNull(WhetherThreadLocal.INSTANCE.getWhether()) || WhetherThreadLocal.INSTANCE.getWhether()) {
            BeanUtils.copyProperties(point, vo);
//                WhetherThreadLocal.INSTANCE.remove();
//            }
            try {
                List<List<Object>> charData = mapper.readValue(LEFT_SQ_BRACKET + collect1 + RIGHT_SQ_BRACKET, List.class);
                List<List<Object>> collect2 = charData.stream().filter(x -> Objects.nonNull(x.get(0))).collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(x -> (Long) x.get(0)))), ArrayList::new));
                fillPoint(collect2, point);
                vo.setChartData(collect2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            result.put(pointId, vo);
        }
        return result;
    }

    /**
     * 判断是否需要补点
     *
     * @param points
     * @param point
     */
    private void fillPoint(List<List<Object>> points, CommonMeasurePointDO point) {
        if (CollectionUtils.isEmpty(points)) {
            return;
        }
        List<Object> pointValue = points.get(points.size() - 1);
        Long start = (Long) pointValue.get(0);
        long end = System.currentTimeMillis();
        if (end - start <= timeInterval * 1000) {
            return;
        }
        HistoryQuerySingleWithFeatureBO single = new HistoryQuerySingleWithFeatureBO();
        single.setEnd(end);
        single.setStart(start + 1);
        single.setSensorCode(point.getSensorCode());
        if (StringUtils.isNotBlank(point.getFeature()) && StringUtils.isNotBlank(point.getFeatureType())) {
            single.setFeature(point.getFeatureType().concat(DASH).concat(point.getFeature()));
        }
        List<List<Object>> lists = listHistoryDataWithPointByScan(single);
        points.addAll(lists);
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
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return Lists.newArrayList();
        }
        //开始时间计算成当前时间的小时值
        TimeRangeQueryBO rangeTime = TableNameParser.getRangeTime(start, end);
        LambdaQueryWrapper<SparkDownSample> wrapper = Wrappers.lambdaQuery(SparkDownSample.class);
        wrapper.in(SparkDownSample::getPointId, pointIds).ge(SparkDownSample::getStartTimestamp, rangeTime.getStart())
                .le(SparkDownSample::getStartTimestamp, rangeTime.getEnd()).orderByAsc(SparkDownSample::getStartTimestamp);
        List<SparkDownSample> list = downSampleServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        SparkDownSample first = list.get(0);
        //截取开头数据
        subPointsByStartAndEnd(start, end, first);
        if (list.size() > 1) {
            SparkDownSample last = list.get(list.size() - 1);
            //截取结束数据
            subPointsByStartAndEnd(start, end, last);
        }
        return list;
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
        Map<String, List<PointEstimateDataBO>> hBaseData = selectModelDataList(multi);
        if (MapUtils.isEmpty(hBaseData)) {
            return null;
        }
        pointIds.stream().forEach(pointId -> {
            EventDataVO vo = new EventDataVO();
            CommonMeasurePointDO point = serviceExt.getPointByPointId(pointId);
            if (Objects.isNull(point)) {
                return;
            }
            BeanUtils.copyProperties(point, vo);
            operationHBaseData(pointId, vo, hBaseData);
            data.put(pointId, vo);
        });
        return data;
    }

    private Map<String, List<PointEstimateDataBO>> selectModelDataList(HistoryQueryMultiBO multi) {
        Map<String, Long> needsQueryIds = serviceExt.listPointByDeviceIdInModel(multi.getPointIds());
        if (MapUtils.isEmpty(needsQueryIds)) {
            return null;
        }
        List<String> queryPointIds = needsQueryIds.entrySet().stream().filter(x -> x.getValue() > 0).map(x -> x.getKey()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(queryPointIds)) {
            return null;
        }
        multi.setPointIds(queryPointIds);
        Map<Long, List<String>> queryMap = needsQueryIds.entrySet().stream().filter(x -> x.getValue() > 0).collect(Collectors.groupingBy(x -> x.getValue(), Collectors.mapping(x -> x.getKey(), Collectors.toList())));
        final Map<String, List<PointEstimateDataBO>> hBaseData = Maps.newHashMap();
        queryMap.entrySet().stream().forEach(x -> {
            Long modelId = x.getKey();
            List<String> pointIds = x.getValue();
            try {
                List<PointEstimateDataBO> collect = hBase.selectModelDataList(H_BASE_TABLE_NPC_PHM_DATA, multi.getStart(), multi.getEnd(), H_BASE_FAMILY_NPC_ESTIMATE, pointIds, modelId);
                if (CollectionUtils.isNotEmpty(collect)) {
                    Map<String, List<PointEstimateDataBO>> map = collect.stream().collect(Collectors.groupingBy(point -> point.getPointId()));
                    hBaseData.putAll(map);
                }
            } catch (IOException e) {
                log.error("select estimate data failed....");
            }
        });
        return hBaseData;
    }

    private void operationHBaseData(String pointId, EventDataVO vo, Map<String, List<PointEstimateDataBO>> hBaseData) {
        if (MapUtils.isEmpty(hBaseData)) {
            return;
        }
        List<PointEstimateDataBO> v = hBaseData.get(pointId);
        if (CollectionUtils.isEmpty(v)) {
            return;
        }
        List actualData = v.stream().map(item -> Lists.newArrayList(item.getTimestamp(), item.getActual())).collect(Collectors.toList());
        List estimatedData = v.stream().map(item -> Lists.newArrayList(item.getTimestamp(), item.getEstimate())).collect(Collectors.toList());
        List residualData = v.stream().map(item -> Lists.newArrayList(item.getTimestamp(), item.getResidual())).collect(Collectors.toList());
        vo.setActualData(actualData);
        vo.setEstimatedData(estimatedData);
        vo.setResidualData(residualData);
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
