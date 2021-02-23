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

import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.history.service.DataAnalysisService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.date.DateNumberConverter;
import com.alibaba.excel.write.metadata.WriteSheet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Api(tags = "Data-数据分析-相关接口")
@RequestMapping(value = "analysis", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataAnalysisController {

    @Resource
    private DataAnalysisService service;

    @GetMapping("vibration")
    @ApiOperation(value = "振动分析", notes = "")
    public Map<String, HistoryDataVO> listAnalysisDataWithPointList(HistoryQueryMultiBO queryMultiBO) {
        long l = System.currentTimeMillis();
        Map<String, HistoryDataVO> data = service.listAnalysisDataWithPointList(queryMultiBO);
        System.out.println("scan 共计耗时： " + (System.currentTimeMillis() - l));
        return data;
    }

    @GetMapping("vibration/export")
    @ApiOperation(value = "振动分析-数据导出", notes = "")
    public void listAnalysisDataWithPointListExport(HistoryQueryMultiBO queryMultiBO, HttpServletResponse response) {
        long l = System.currentTimeMillis();
        Map<String, HistoryDataVO> data = service.listAnalysisDataWithPointList(queryMultiBO);
        if (MapUtils.isEmpty(data)) {
            throw new CustomMessageException("没有可以导出的数据");
        }
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
        System.out.println("scan 共计耗时： " + (System.currentTimeMillis() - l));
    }
}
