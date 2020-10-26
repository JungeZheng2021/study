package com.aimsphm.nuclear.turbine.controller;

import com.aimsphm.nuclear.common.entity.MdDeviceDetails;
import com.aimsphm.nuclear.common.entity.TxTurbinesnapshot;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointTimesScaleVO;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.response.ReturnResponse;
import com.aimsphm.nuclear.common.service.MdDeviceDetailsService;
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
 * @Description: <汽机监测控制类>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 9:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 9:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController()
@Api(tags = "汽机监测控制类")
@RequestMapping(value = "monitor/turbine", produces = MediaType.APPLICATION_JSON_VALUE)
public class TurbineEquipmentMonitoringController {

    @Autowired
    private MdDeviceDetailsService detailsService;

    @Autowired
    private PumpEquipmentMonitoringService monitoringService;


    @GetMapping("details/{subSystemId}")
    @ApiOperation(value = "获取设备详细信息-设计信息")
    public ReturnResponse listDeviceInfo(@PathVariable @NotNull Long subSystemId) {
        List<MdDeviceDetails> details = detailsService.listDeviceInfo(subSystemId);
        return ResponseUtils.success(details);
    }

    @GetMapping("{deviceId}/status")
    @ApiOperation(value = "获取运行状态信息-启停/健康状态/总运行时长...")
    public ReturnResponse getRunningStatus(@PathVariable @NotNull Long deviceId) {
        TxTurbinesnapshot snapshot = monitoringService.getRunningStatusTurbine(deviceId);
        return ResponseUtils.success(snapshot);
    }

    @GetMapping("{deviceId}/statistics/warning")
    @ApiOperation(value = "报警统计-测点类型/报警级别/报警类型", notes = "startTime需计算出来")
    public ReturnResponse<List<List<MeasurePointTimesScaleVO>>> statisticsWarmingPoints(@PathVariable @NotNull Long deviceId, Long startTime, Long endTime) {
        List<List<MeasurePointTimesScaleVO>> voList = monitoringService.turbineStatisticsWarmingPoints(deviceId, startTime, endTime);
        return ResponseUtils.success(voList);
    }

    @GetMapping("{deviceId}/statistics/running")
    @ApiOperation(value = "运行统计-/启动/累积/报警趋势")
    public ReturnResponse statisticsRunningStatus(@PathVariable @NotNull Long deviceId) {
        List<MeasurePointTimesScaleVO> vos = monitoringService.turbineStatisticsRunningStatus(deviceId);
        return ResponseUtils.success(vos);
    }
}
