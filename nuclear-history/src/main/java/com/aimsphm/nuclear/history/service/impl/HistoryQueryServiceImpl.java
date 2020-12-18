package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.common.config.DynamicTableTreadLocal;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.SparkDownSample;
import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleWithFeatureBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.SparkDownSampleService;
import com.aimsphm.nuclear.history.entity.enums.TableNameEnum;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataWithThresholdVO;
import com.aimsphm.nuclear.history.service.HistoryQueryService;
import com.aimsphm.nuclear.history.util.TableNameParser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Get;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class HistoryQueryServiceImpl implements HistoryQueryService {

    @Autowired
    private CommonMeasurePointService serviceExt;
    @Autowired
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
        CommonMeasurePointDO point = getPoint(single.getPointId());
        if (Objects.isNull(point)) {
            throw new CustomMessageException("要查询的测点不存在");
        }
        HistoryQueryMultiBO multi = new HistoryQueryMultiBO();
        multi.setPointIds(Lists.newArrayList(single.getPointId()));
        BeanUtils.copyProperties(single, multi);
        listHistoryDataWithPointIdsFromMysql(multi);
        Map<String, HistoryDataVO> data = listHistoryDataWithPointIdsFromMysql(multi);
        if (Objects.nonNull(data)) {
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
        BeanUtils.copyProperties(point, vo);
        vo.setActualData(dataDTOS);
        return vo;
    }

    private CommonMeasurePointDO getPoint(String pointId) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonMeasurePointDO::getPointId, pointId);
        CommonMeasurePointDO point = serviceExt.getOne(wrapper);
        return point;
    }

    @Override
    public Map<String, HistoryDataVO> listHistoryDataWithPointIdsByScan(HistoryQueryMultiBO multi) {
        HashMap<String, HistoryDataVO> result = new HashMap<>(16);
        if (Objects.isNull(multi) || CollectionUtils.isEmpty(multi.getPointIds())
                || Objects.isNull(multi.getEnd()) || Objects.isNull(multi.getStart()) || multi.getEnd() <= multi.getStart()) {
            return result;
        }
        Map<String, HistoryDataVO> data = listHistoryDataWithPointIdsFromMysql(multi);
        if (Objects.nonNull(data)) {
            return data;
        }
        Long end = multi.getEnd();
        Long start = multi.getStart();
        for (String pointId : multi.getPointIds()) {
            HistoryQuerySingleBO bo = new HistoryQuerySingleBO();
            bo.setPointId(pointId);
            bo.setEnd(end);
            bo.setStart(start);
            result.put(pointId, listHistoryDataWithPointByScan(bo));
        }
        return result;
    }

    private Map<String, HistoryDataVO> listHistoryDataWithPointIdsFromMysql(HistoryQueryMultiBO multi) {
        List<SparkDownSample> list = listSparkDownSampleByConditions(multi);
        if (Objects.isNull(list)) {
            return null;
        }
        Map<String, HistoryDataVO> result = Maps.newHashMap();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> collect = list.stream().filter(item -> StringUtils.isNotBlank(item.getPoints()) && StringUtils.isNotBlank(item.getPointId()))
                .collect(Collectors.groupingBy(item -> item.getPointId().trim(), Collectors.mapping(item -> item.getPoints(), Collectors.toList())));
        for (String pointId : multi.getPointIds()) {
            CommonMeasurePointDO point = getPoint(pointId);
            if (Objects.isNull(point)) {
                continue;
            }
            List<String> points = collect.get(pointId);
            String collect1 = points.stream().collect(Collectors.joining(COMMA));
            HistoryDataVO vo = new HistoryDataWithThresholdVO();
            BeanUtils.copyProperties(point, vo);
            try {
                vo.setActualData(mapper.readValue(LEFT_SQ_BRACKET + collect1 + RIGHT_SQ_BRACKET, List.class));
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
        if (StringUtils.isBlank(tableName)) {
            return null;
        }
        int endYear = DateUtils.transition(new Date(end)).getYear();
        int startYear = DateUtils.transition(new Date(start)).getYear();
        List<SparkDownSample> list = Lists.newArrayList();
        for (int year = startYear; year <= endYear; year++) {
            resetTableName(tableName, year);
            list.addAll(listSparkDownSampleByPointIdsAndRangeTime(multi.getPointIds(), start, end));
        }
        return list;
    }

    private List<SparkDownSample> listSparkDownSampleByPointIdsAndRangeTime(List<String> pointIds, Long start, Long end) {
        LambdaQueryWrapper<SparkDownSample> wrapper = Wrappers.lambdaQuery(SparkDownSample.class);
        wrapper.in(SparkDownSample::getPointId, pointIds).ge(SparkDownSample::getStartTimestamp, start)
                .le(SparkDownSample::getStartTimestamp, end).orderByAsc(SparkDownSample::getStartTimestamp);
        return downSampleServiceExt.list(wrapper);
    }

    private void resetTableName(String tableName, int year) {
        if (tableName.equals(TableNameEnum.DAILY.getValue())) {
            DynamicTableTreadLocal.INSTANCE.setTableName(tableName + UNDERLINE + year);
            return;
        }
        DynamicTableTreadLocal.INSTANCE.setTableName(tableName);
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
            Map<String, List<HBaseTimeSeriesDataDTO>> data = hBase.selectDataList(H_BASE_TABLE_NPC_PHM_DATA, getList);
            multi.getPointIds().stream().forEach(item -> {
                CommonMeasurePointDO point = getPoint(item);
                HistoryDataVO vo = new HistoryDataWithThresholdVO();
                BeanUtils.copyProperties(point, vo);
//                vo.setActualData(data.get(point.getSensorCode()));
                result.putIfAbsent(item, vo);
            });
        } catch (IOException e) {
            throw new CustomMessageException("查询历史数据失败");
        }
        return result;
    }

    private List<Get> initGetListByConditions(HistoryQueryMultiBO multi) {
        List<String> pointIds = multi.getPointIds();
        Long end = multi.getEnd();
        Long start = multi.getStart();

        return null;
    }
}
