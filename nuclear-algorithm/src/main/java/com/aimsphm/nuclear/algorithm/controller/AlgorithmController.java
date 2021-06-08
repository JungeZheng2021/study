package com.aimsphm.nuclear.algorithm.controller;

import com.aimsphm.nuclear.algorithm.entity.dto.FaultReasoningResponseDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.SymptomResponseDTO;
import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
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
import java.util.Objects;

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

    @GetMapping("test/{deviceId}/{type}")
    @ApiOperation(value = "状态监测算法")
    public String getDeviceStateMonitorInfo(@PathVariable Long deviceId, @PathVariable String type) throws IOException {
        AlgorithmTypeEnum byValue = AlgorithmTypeEnum.getByValue(type);
        if (Objects.isNull(byValue)) {
            return null;
        }
        if (byValue.equals(AlgorithmTypeEnum.THRESHOLD_MONITOR)) {
            algorithmService.deviceThresholdMonitorInfo(byValue, deviceId, 1 * 60);
        }
        if (byValue.equals(AlgorithmTypeEnum.STATE_MONITOR)) {
            algorithmService.deviceStateMonitorInfo(byValue, deviceId, 10 * 60);
        }
        return String.format("%s 算法运行成功", byValue.getDesc());
    }

    @GetMapping("test")
    @ApiOperation(value = "计算特征数据")
    public void operationFeatureExtractionData() {
        featureExtractionService.operationFeatureExtractionData(PointTypeEnum.CALCULATE);
    }

    @GetMapping("test/symptom")
    @ApiOperation(value = "征兆判断")
    public SymptomResponseDTO symptomJudgmentData(@RequestParam("pointIds") List<String> pointIds) {
        return featureExtractionService.symptomJudgment(pointIds);
    }

    @GetMapping("test/faultReasoning")
    @ApiOperation(value = "故障推理")
    public FaultReasoningResponseDTO faultReasoning(@RequestParam("pointIds") List<String> pointIds, Long deviceId) {
        return faultReasoningService.faultReasoning(pointIds, deviceId);
    }
}
