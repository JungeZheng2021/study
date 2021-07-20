package com.aimsphm.nuclear.history.controller;

/**
 * @Package: com.aimsphm.nuclear.history.controller
 * @Description: <历史数据查询-相关接口>
 * @Author: MILLA
 * @CreateDate: 2020/11/21 11:39
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/21 11:39
 * @UpdateRemark: <>
 * @Version: 1.0
 */

import com.aimsphm.nuclear.algorithm.util.RawDataThreadLocal;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleWithFeatureBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.entity.vo.EventDataVO;
import com.aimsphm.nuclear.common.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.aimsphm.nuclear.history.service.HistoryQueryService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.date.DateNumberConverter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_TABLE_NPC_PHM_DATA;

@RestController
@Api(tags = "History-历史数据查询-相关接口")
@RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
public class HistoryQueryController {

    private HistoryQueryService service;

    public HistoryQueryController(HistoryQueryService service) {
        this.service = service;
    }

    @Resource
    private CommonMeasurePointService iCommonMeasurePointServiceExt;

    @GetMapping("single")
    @ApiOperation(value = "查询一个测点的历史数据", notes = "pointId是完整测点编号")
    public HistoryDataVO listHistoryWithSinglePoint(HistoryQuerySingleBO singleBO) {
        HistoryDataVO data = service.listHistoryDataWithPointByScan(singleBO);
        return data;
    }

    @GetMapping("single/feature")
    @ApiOperation(value = "查询一个测点的历史数据[需要特征值]", notes = "PI 测点feature不需要传值,自装测点需要传特征值")
    public List<List<Object>> listHistoryWithSingleTagByThreshold(HistoryQuerySingleWithFeatureBO singleBO) {
        return service.listHistoryDataWithPointByScan(singleBO);
    }

    @GetMapping("multiple")
    @ApiOperation(value = "查询多个测点的历史数据", notes = "")
    public Map<String, HistoryDataVO> listHistoryWithPointList(HistoryQueryMultiBO queryMultiBO) {
        Map<String, HistoryDataVO> data = service.listHistoryDataWithPointIdsByScan(queryMultiBO);
        return data;
    }

    @GetMapping("multiple/realtime")
    @ApiOperation(value = "查询多个测点实测值、估计值、报警测点、残差值", notes = "")
    public Map<String, EventDataVO> listDataWithPointList(HistoryQueryMultiBO queryMultiBO) {
        Map<String, EventDataVO> data = service.listDataWithPointList(queryMultiBO);
        return data;
    }

    @GetMapping("multiple/export")
    @ApiOperation(value = "导出多个测点的历史数据", notes = "")
    public void exportHistoryWithPointList(HistoryQueryMultiBO queryMultiBO, HttpServletResponse response) {
        Map<String, HistoryDataVO> data = service.listHistoryDataWithPointIdsByScan(queryMultiBO);
        if (MapUtils.isEmpty(data)) {
            throw new CustomMessageException("没有可以导出的数据");
        }
        writeExcelFile(response, data);
    }

    private void writeExcelFile(HttpServletResponse response, Map<String, HistoryDataVO> data) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=export-" + System.currentTimeMillis() + ".xlsx");
        AtomicInteger counts = new AtomicInteger(1);
        try {
            ExcelWriter writer = EasyExcel.write(response.getOutputStream()).build();
            data.forEach((k, v) -> {
                List<List<Object>> chartData = v.getChartData();
                int andIncrement = counts.getAndIncrement();
                WriteSheet writeSheet = EasyExcel.writerSheet(andIncrement, k).head(HBaseTimeSeriesDataDTO.class).registerConverter(new DateNumberConverter()).build();
                writer.write(chartData, writeSheet);
            });
            writer.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Resource
    CommonMeasurePointService pointService;

    @GetMapping("export/raw-data")
    @ApiOperation(value = "导出原始数据", notes = "接口中不使用")
    public Map<String, HistoryDataVO> listHistoryPointList(HistoryQueryMultiBO queryMultiBO, String sql) {
        operationPoints(queryMultiBO, sql);
        RawDataThreadLocal.INSTANCE.setWhether(true);
        Map<String, HistoryDataVO> data = service.listHistoryDataWithPointIdsByScan(queryMultiBO);
        return data;
    }

    @GetMapping("export/raw-data/excel")
    @ApiOperation(value = "导出原始数据excel", notes = "接口中不使用")
    public void listHistoryPointListExcel(HistoryQueryMultiBO queryMultiBO, HttpServletResponse response, String sql) {
        operationPoints(queryMultiBO, sql);
        RawDataThreadLocal.INSTANCE.setWhether(true);
        Map<String, HistoryDataVO> data = service.listHistoryDataWithPointIdsByScan(queryMultiBO);
        writeExcelFile(response, data);
    }

    private void operationPoints(HistoryQueryMultiBO queryMultiBO, String sql) {
        if (StringUtils.isNotBlank(sql)) {
            LambdaQueryWrapper<CommonMeasurePointDO> wrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
            wrapper.apply(sql);
            List<CommonMeasurePointDO> list = pointService.list(wrapper);
            if (CollectionUtils.isNotEmpty(list)) {
                List<String> collect = list.stream().map(x -> x.getPointId()).collect(Collectors.toList());
                queryMultiBO.setPointIds(collect);
            }
        }
    }

    @Resource
    private HBaseUtil hBase;

    @GetMapping("families/features")
    @ApiOperation(value = "将所有的特征值添加到列族", notes = "增量增加")
    public List<String> addFeatures2Families() {
        Set<String> features = iCommonMeasurePointServiceExt.listFeatures();
        try {
            return hBase.addFamily2TableIncrementalAdd(H_BASE_TABLE_NPC_PHM_DATA, Lists.newArrayList(features), Compression.Algorithm.SNAPPY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("multiple/gets")
    @ApiOperation(value = "查询多个测点的历史数据-备用", notes = "备用")
    public Map<String, HistoryDataVO> listHistoryWithPointListByGetList(HistoryQueryMultiBO queryMultiBO) {
        Map<String, HistoryDataVO> data = service.listHistoryDataWithPointIdsByGetList(queryMultiBO);
        return data;
    }
}
