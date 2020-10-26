package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.core.entity.bo.StopStartAnalyseBO;
import com.aimsphm.nuclear.core.service.CommonStopStartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Mao
 * @since 2020-05-07
 */
@Slf4j
@RestController
@RequestMapping("stopStart")
@Api(tags = "通用启停分析类")
public class CommonStopStartController {

    @Autowired
    private CommonStopStartService stopStartService;

    @GetMapping("getCSensors")
    @ApiOperation(value = "获取所属设备或者子系统的所有测点（包括虚拟测点）")
    public Object getCSensors(@RequestParam Long subSystemId,@RequestParam(required=false) Long deviceId) {
        return ResponseUtils.success(stopStartService.getAllSubSystemSensors(subSystemId,deviceId));
    }

    @ApiOperation(value = "启停分析时序分析接口")
    @PostMapping("analyseStopStartXTime/{subSystemId}")
    public Object analyseStopStartXTime(@RequestBody StopStartAnalyseBO sb) {
        Map<String, Object> map = stopStartService.analyseStopStartXTime(sb.getYCommonSensorVO(), sb.getOnset(), sb.getOffset(), sb.getRecords(), sb.getBaseRecordId(), sb.getStartFlag());
        return ResponseUtils.success(map);
    }
    @ApiOperation(value = "启停分析相关性分析接口")
    @PostMapping("analyseStopStart/{subSystemId}")
    public Object analyseStopStart(@RequestBody StopStartAnalyseBO sb) {
        Map<String,Map> map = stopStartService.analyseStopStart(sb.getXCommonSensorVO(),sb.getYCommonSensorVO(), sb.getOnset(), sb.getOffset(), sb.getRecords(),  sb.getStartFlag());
        return ResponseUtils.success(map);
    }

}
