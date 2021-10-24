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
import com.aimsphm.nuclear.common.entity.vo.FaultReasoningVO;
import com.aimsphm.nuclear.common.entity.vo.HistoryDataVO;
import com.aimsphm.nuclear.history.service.AlgorithmQueryService;
import com.aimsphm.nuclear.history.service.FaultReasoningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "算法调用接口")
@RequestMapping(value = "algorithm", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmQueryController {
    @Resource
    private AlgorithmQueryService service;
    @Resource
    private FaultReasoningService faultReasoningService;

    @GetMapping("trend")
    @ApiOperation(value = "去躁数据", notes = "多个参数")
    public Map<String, HistoryDataVO> listMovingAverageInfo(HistoryQueryMultiBO multiBo) {
        Map<String, HistoryDataVO> data = service.listMovingAverageInfo(multiBo);
        log.debug("{}", data);
        return data;
    }

    @GetMapping("fault/reasoning")
    @ApiOperation(value = "故障推理")
    public List<FaultReasoningVO> faultReasoning(@RequestParam("pointIds") List<String> pointIds, Long deviceId, Long gmtLastAlarm) {
        return faultReasoningService.faultReasoningVO(pointIds, deviceId, gmtLastAlarm);
    }

    @GetMapping("prediction")
    @ApiOperation(value = "预测数据", notes = "多个参数")
    public Map<String, HistoryDataVO> listPredictionInfo(HistoryQueryMultiBO multiBo) {
        Map<String, HistoryDataVO> data = service.listPredictionInfo(multiBo);
        log.debug("{}", data);
        return data;
    }

    @GetMapping("analysis/vibration")
    @ApiOperation(value = "振动分析绘图", notes = "")
    public Map<String, List<List<List<Object>>>> listVibrationAnalysisData(DataAnalysisQueryMultiBO query) {
        return service.listVibrationAnalysisData(query);
    }

    @GetMapping("analysis/vibration/export")
    @ApiOperation(value = "振动分析绘图数据导出", notes = "")
    public Map<String, HistoryDataVO> listVibrationAnalysisDataExport(HistoryQueryMultiBO queryMultiBO) {
        return null;
    }
}
