package com.aimsphm.nuclear.algorithm.controller;

import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.aimsphm.nuclear.algorithm.service.FaultReasoningService;
import com.aimsphm.nuclear.algorithm.service.FeatureExtractionOperationService;
import com.aimsphm.nuclear.common.enums.PointTypeEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.controller
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/18 18:01
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/18 18:01
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "algorithm", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmController {
    @Resource
    private AlgorithmService algorithmService;

    @Resource
    private FeatureExtractionOperationService featureExtractionService;

    @Resource
    private FaultReasoningService faultReasoningService;

    @GetMapping("test/{deviceId}/{p}")
    @ApiOperation(value = "获取某一实体")
    public void getDeviceStateMonitorInfo(@PathVariable Long deviceId, @PathVariable Integer p) throws IOException {
        algorithmService.getDeviceStateMonitorInfo(deviceId, p);
    }

    @GetMapping("test")
    @ApiOperation(value = "计算特征数据")
    public void operationFeatureExtractionData() {
        featureExtractionService.operationFeatureExtractionData(PointTypeEnum.CALCULATE);
    }

    @GetMapping("test/symptom")
    @ApiOperation(value = "征兆判断")
    public void symptomJudgmentData(@RequestParam("pointIds") List<String> pointIds) {
        List<Integer> indexes = featureExtractionService.symptomJudgment(pointIds);
    }

    @GetMapping("test/faultReasoning")
    @ApiOperation(value = "故障推理")
    public void faultReasoning(@RequestParam("pointIds") List<String> pointIds, Long deviceId) {
        faultReasoningService.faultReasoning(pointIds, deviceId);
    }
}
