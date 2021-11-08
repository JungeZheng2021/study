package com.aimsphm.nuclear.down.sample.service;

import com.aimsphm.nuclear.common.config.DynamicTableTreadLocal;
import com.aimsphm.nuclear.common.entity.SparkDownSample;
import com.aimsphm.nuclear.common.entity.SparkDownSampleConfigDO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleWithFeatureBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.enums.FrequencyEnum;
import com.aimsphm.nuclear.common.enums.TableNameEnum;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.down.sample.entity.bo.ThreePointBO;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.SymbolConstant.*;
import static com.aimsphm.nuclear.common.util.DateUtils.YEAR;

/**
 * <p>
 * 功能描述: 执行降采样
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/11/01 18:53
 */
public interface ExecuteDownSampleService {
    /**
     * 执行降采样
     *
     * @param list     需要操作的集合
     * @param rangTime 起止时间
     */
    void executeDownSample(List<SparkDownSampleConfigDO> list, TimeRangeQueryBO rangTime);

    /**
     * 原始数据操作
     *
     * @param single 测点
     * @param data   初始化的数据
     * @param rate   采样率
     */
    default void rawDataOperation(HistoryQuerySingleWithFeatureBO single, Map<TimeRangeQueryBO, ThreePointBO> data, FrequencyEnum rate) {
        List<List<Object>> lists = queryDataByPoint(single);
        String pointId = single.getSensorCode() + (StringUtils.isNotBlank(single.getFeature()) ? DASH + single.getFeature() : EMPTY);
        rawDataOperation(lists, pointId, data, rate);
    }

    /**
     * 原始数据操作
     *
     * @param lists   测点
     * @param data    初始化的数据
     * @param pointId 测点
     * @param rate    采样率
     */
    default void rawDataOperation(List<List<Object>> lists, String pointId, Map<TimeRangeQueryBO, ThreePointBO> data, FrequencyEnum rate) {
        List<List<Object>> result = rawDataOperation(lists, data);
        if (CollectionUtils.isEmpty(result)) {
            return;
        }
        String dataJson = new Gson().toJson(result);
        SparkDownSample sample = new SparkDownSample();
        sample.setPointId(pointId);
        Long timestamp = (Long) result.get(0).get(0);
        sample.setStartTimestamp((Long) result.get(0).get(0));
        sample.setAlgorithmType(1);
        sample.setPoints(dataJson.substring(1, dataJson.length() - 1));
        String tableName = rate.getTableName();
        if (StringUtils.isBlank(rate.getTableName())) {
            tableName = TableNameEnum.DAILY.getValue() + UNDERLINE + DateUtils.format(YEAR, timestamp);
            boolean flag = checkAndCreateTable(tableName);
            if (!flag) {
                return;
            }
        }
        DynamicTableTreadLocal.INSTANCE.tableName(tableName);
        saveDownSampleResult(sample);
    }

    /**
     * 检查并创建表格
     *
     * @param tableName 表格名称
     * @return
     */
    default boolean checkAndCreateTable(String tableName) {
        return true;
    }

    /**
     * 原始数据操作
     *
     * @param lists 测点
     * @param data  初始化的数据
     * @return 降采之后的结果集
     */
    default List<List<Object>> rawDataOperation(List<List<Object>> lists, Map<TimeRangeQueryBO, ThreePointBO> data) {
        List<List<Object>> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(lists)) {
            return result;
        }
        lists.stream().filter(o -> CollectionUtils.isNotEmpty(o) && o.size() == 2).forEach(x -> setThreePointData(data, x));
        data.entrySet().stream().sorted(Comparator.comparing(a -> a.getKey().getStart())).map(x -> x.getValue().sorted()).forEach(x -> result.addAll(x));
        return result;
    }

    /**
     * 查询数据
     *
     * @param pointInfo 测点信息
     * @return 查询数据的结果集
     */
    List<List<Object>> queryDataByPoint(HistoryQuerySingleWithFeatureBO pointInfo);

    /**
     * 保存结果
     *
     * @param sample 降采样的结果
     */
    void saveDownSampleResult(SparkDownSample sample);

    /**
     * 设置三点数据
     *
     * @param data    数据
     * @param element 每一个元素
     */
    default void setThreePointData(Map<TimeRangeQueryBO, ThreePointBO> data, List<Object> element) {
        Long timestamp = (Long) element.get(0);
        Double value = (Double) element.get(1);
        data.entrySet().stream().filter(x -> timestamp >= x.getKey().getStart() && timestamp < x.getKey().getEnd()).forEach(x -> {
            ThreePointBO bo = x.getValue();
            //第一个元素
            if (Objects.isNull(bo.getFirst())) {
                bo.setFirst(element);
                return;
            }
            List<Object> first = bo.getFirst();
            List<Object> max = bo.getMax();
            List<Object> min = bo.getMin();
            Double firstValue = (Double) first.get(1);
            if (Objects.isNull(min) && Objects.isNull(max) && value > firstValue) {
                bo.setMax(element);
                return;

            }
            if (Objects.isNull(min) && Objects.isNull(max) && value < firstValue) {
                bo.setMin(element);
                return;
            }
            if (Objects.nonNull(min) && value < (Double) min.get(1)) {
                bo.setMin(element);
                return;
            }
            if (Objects.nonNull(max) && value > (Double) max.get(1)) {
                bo.setMax(element);
                return;
            }
        });

    }

    /**
     * 初始化 对应的map
     *
     * @param start        开始时间
     * @param end          结束时间
     * @param targetNumber 需要降采样的数量
     * @return map对象
     */
    default Map<TimeRangeQueryBO, ThreePointBO> initKeys(Long start, Long end, Integer targetNumber) {
        targetNumber = targetNumber / 3;
        Map<TimeRangeQueryBO, ThreePointBO> map = new ConcurrentHashMap<>(16);
        long gap = ((end - start) / 1000 / targetNumber);
        long begin = start;
        for (int i = 0; i < targetNumber; i++) {
            TimeRangeQueryBO bo = new TimeRangeQueryBO(begin + i * gap * 1000L, begin + (i + 1) * gap * 1000L);
            map.put(bo, new ThreePointBO());
        }
        long last = begin + gap * targetNumber * 1000L;
        if (last != end) {
            map.put(new TimeRangeQueryBO(last, end), new ThreePointBO());
        }
        return map;
    }

    /**
     * 保存数据到多个数据
     *
     * @param list     数据
     * @param rangTime 起止时间
     */
    default void dataStore2ManyTable(List<SparkDownSampleConfigDO> list, TimeRangeQueryBO rangTime) {
        Map<String, List<SparkDownSampleConfigDO>> collect = list.stream().filter(x -> StringUtils.isNotBlank(x.getSensorCode()))
                .collect(Collectors.groupingBy(x -> x.getSensorCode() + (Objects.isNull(x.getFeature()) ? EMPTY : UNDERLINE + x.getFeature())));
        collect.forEach((pointId, v) -> {
            HistoryQuerySingleWithFeatureBO bo = new HistoryQuerySingleWithFeatureBO();
            if (pointId.contains(UNDERLINE)) {
                String[] split = pointId.split(UNDERLINE);
                bo.setSensorCode(split[0]);
                bo.setFeature(split[1]);
            } else {
                bo.setSensorCode(pointId);
            }
            bo.setStart(rangTime.getStart());
            bo.setEnd(rangTime.getEnd());
            List<List<Object>> lists = queryDataByPoint(bo);
            if (CollectionUtils.isEmpty(lists)) {
                return;
            }
            v.forEach(x -> {
                try {
                    FrequencyEnum rate = FrequencyEnum.getByRate(x.getRate());
                    if (Objects.isNull(rate)) {
                        return;
                    }
                    Map<TimeRangeQueryBO, ThreePointBO> data = initKeys(rangTime.getStart(), rangTime.getEnd(), x.getTargetNum());
                    rawDataOperation(lists, bo.getSensorCode() + (Objects.isNull(bo.getFeature()) ? EMPTY : DASH + bo.getFeature()), data, rate);
                } catch (Exception e) {
//                    log.error("lost point :{} ;cause:{}", x.getSensorCode(), e);
                }
            });
        });
    }

    /**
     * 为历史查询时时降采样
     *
     * @param config        配置信息
     * @param rangTime      起止时间
     * @param frequencyEnum
     * @return 降采样的结果
     */
    default List<List<Object>> executeDownSample4HistoryData(SparkDownSampleConfigDO config, TimeRangeQueryBO rangTime, FrequencyEnum frequencyEnum) {
        return new ArrayList<>();
    }

    /**
     * 为历史查询时时降采样
     *
     * @param single    配置信息
     * @param targetNum 目标数量
     * @return 降采样的结果
     */
    default List<List<Object>> rawDataOperation4History(HistoryQuerySingleWithFeatureBO single, Integer targetNum) {
        List<List<Object>> lists = queryDataByPoint(single);
        Map<TimeRangeQueryBO, ThreePointBO> data = initKeys(single.getStart(), single.getEnd(), targetNum);
        return rawDataOperation(lists, data);
    }
}
