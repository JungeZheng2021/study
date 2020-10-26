package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.response.ReturnResponse;
import com.aimsphm.nuclear.core.service.VibrationAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/08/14 14:26
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/14 14:26
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController()
@Api(tags = "旋机-振动分析")
@RequestMapping(value = "analysis", produces = MediaType.APPLICATION_JSON_VALUE)
public class VibrationAnalysisController {
    @Autowired
    private VibrationAnalysisService analysisService;


    @GetMapping("devices/{deviceId}/locations")
    @ApiOperation(value = "获取测点位置-根据设备id")
    public ReturnResponse listSensorLocation(@PathVariable @NotNull Long deviceId) {
        Map<String, String> locations = analysisService.listSensorLocation(deviceId);
        return ResponseUtils.success(locations);
    }

    @GetMapping("{subSystemId}/locations")
    @ApiOperation(value = "获取测点位置-根据子系统id")
    public ReturnResponse listSensorLocationBySubSystemId(@PathVariable @NotNull Long subSystemId) {
        Map<String, String> locations = analysisService.listSensorLocationBySubSystemId(subSystemId);
        return ResponseUtils.success(locations);
    }
}
