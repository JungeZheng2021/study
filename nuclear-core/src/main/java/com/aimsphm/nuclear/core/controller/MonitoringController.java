package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.vo.MeasurePointTimesScaleVO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.core.service.EquipmentMonitoringService;
import com.aimsphm.nuclear.pump.service.SystemPanoramaService;
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
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <设备监测控制类>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 9:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 9:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController()
@Api(tags = "系统监测")
@RequestMapping(value = "monitor", produces = MediaType.APPLICATION_JSON_VALUE)
public class MonitoringController {

    @Autowired
    private SystemPanoramaService panoramaService;
    @Autowired
    private EquipmentMonitoringService monitoringService;

    @GetMapping("{deviceId}")
    @ApiOperation(value = "设备监测信息(包含实时预警信息)")
    public Map<String, MeasurePointVO> getMonitorInfo(@PathVariable @NotNull Long deviceId) {
        return panoramaService.getMonitorInfo(deviceId);
    }

    @GetMapping("sensor/statics/{subSystemId}")
    @ApiOperation(value = "传感器统计信息)")
    public Map<String, MeasurePointVO> getSensorStaticsInfo(@PathVariable @NotNull Long subSystemId) {
        return panoramaService.getMonitorInfo(subSystemId);
    }

    @GetMapping("{deviceId}/statistics/warning")
    @ApiOperation(value = "报警统计", notes = "startTime需计算出来")
    public List<List<MeasurePointTimesScaleVO>> statisticsWarmingPoints(@PathVariable @NotNull Long deviceId, Long startTime, Long endTime) {
        return monitoringService.statisticsWarmingPoints(deviceId, startTime, endTime);
    }
}
