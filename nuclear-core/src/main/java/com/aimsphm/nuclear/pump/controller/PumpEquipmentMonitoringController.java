package com.aimsphm.nuclear.pump.controller;

import com.aimsphm.nuclear.common.entity.TxPumpsnapshot;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointTimesScaleVO;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.response.ReturnResponse;
import com.aimsphm.nuclear.pump.service.PumpEquipmentMonitoringService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.pump.controller
 * @Description: <主泵监测控制类>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 9:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 9:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController()
@Api(tags = "主泵监测控制类")
@RequestMapping(value = "monitor/pump", produces = MediaType.APPLICATION_JSON_VALUE)
public class PumpEquipmentMonitoringController {

    @Autowired
    private PumpEquipmentMonitoringService monitoringService;

    @GetMapping("{deviceId}/status")
    @ApiOperation(value = "获取运行状态信息")
    public ReturnResponse getRunningStatus(@PathVariable @NotNull Long deviceId) {
        TxPumpsnapshot txPumpsnapshot = monitoringService.getRunningStatus(deviceId);
        return ResponseUtils.success(txPumpsnapshot);
    }

    @GetMapping("{deviceId}/statistics/warning")
    @ApiOperation(value = "报警统计", notes = "startTime需计算出来")
    public ReturnResponse<List<List<MeasurePointTimesScaleVO>>> statisticsWarmingPoints(@PathVariable @NotNull Long deviceId, Long startTime, Long endTime) {
        List<List<MeasurePointTimesScaleVO>> voList = monitoringService.statisticsWarmingPoints(deviceId, startTime, endTime);
        return ResponseUtils.success(voList);
    }

    @GetMapping("{deviceId}/statistics/running")
    @ApiOperation(value = "运行统计")
    public ReturnResponse statisticsRunningStatus(@PathVariable @NotNull Long deviceId) {
        List<MeasurePointTimesScaleVO> vos = monitoringService.statisticsRunningStatus(deviceId);
        return ResponseUtils.success(vos);
    }
}
