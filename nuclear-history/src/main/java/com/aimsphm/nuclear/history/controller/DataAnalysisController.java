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
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.history.service.DataAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

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
    public Map<String, HistoryDataVO> listAnalysisDataWithPointListExport(HistoryQueryMultiBO queryMultiBO) {
        long l = System.currentTimeMillis();
        Map<String, HistoryDataVO> data = service.listAnalysisDataWithPointList(queryMultiBO);
        System.out.println("scan 共计耗时： " + (System.currentTimeMillis() - l));
        return data;
    }
}
