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

import com.aimsphm.nuclear.common.entity.bo.DataAnalysisQueryMultiBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.history.service.AlgorithmQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "算法调用接口")
@RequestMapping(value = "algorithm", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmQueryController {
    @Resource
    private AlgorithmQueryService service;

    @GetMapping("trend")
    @ApiOperation(value = "去躁数据", notes = "多个参数")
    public Map<String, HistoryDataVO> listMovingAverageInfo(HistoryQueryMultiBO multiBo) {
        long l = System.currentTimeMillis();
        Map<String, HistoryDataVO> data = service.listMovingAverageInfo(multiBo);
        System.out.println("scan 共计耗时： " + (System.currentTimeMillis() - l));
        return data;
    }

    @GetMapping("prediction")
    @ApiOperation(value = "预测数据", notes = "多个参数")
    public Map<String, HistoryDataVO> listPredictionInfo(HistoryQueryMultiBO multiBo) {
        long l = System.currentTimeMillis();
        Map<String, HistoryDataVO> data = service.listPredictionInfo(multiBo);
        System.out.println("scan 共计耗时： " + (System.currentTimeMillis() - l));
        return data;
    }

    @GetMapping("analysis/vibration")
    @ApiOperation(value = "振动分析绘图", notes = "")
    public Map<String, List<List<List<Object>>>> listVibrationAnalysisData(DataAnalysisQueryMultiBO query) {
//        long l = System.currentTimeMillis();
        return service.listVibrationAnalysisData(query);
//        System.out.println("scan 共计耗时： " + (System.currentTimeMillis() - l));
//        return data;
    }

    @GetMapping("analysis/vibration/export")
    @ApiOperation(value = "振动分析绘图数据导出", notes = "")
    public Map<String, HistoryDataVO> listVibrationAnalysisDataExport(HistoryQueryMultiBO queryMultiBO) {
//        long l = System.currentTimeMillis();
//        Map<String, HistoryDataVO> data = service.listAnalysisDataWithPointList(queryMultiBO);
//        System.out.println("scan 共计耗时： " + (System.currentTimeMillis() - l));
//        return data;
        return null;
    }
}
