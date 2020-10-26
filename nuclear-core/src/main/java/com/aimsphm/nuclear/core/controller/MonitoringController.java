package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.vo.MeasurePointTimesScaleVO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.response.ReturnResponse;
import com.aimsphm.nuclear.core.vo.RotaryMonitorVO;
import com.aimsphm.nuclear.pump.service.PumpEquipmentMonitoringService;
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
 * @Package: com.aimsphm.nuclear.pump.controller
 * @Description: <各设备监测控制类>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 9:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 9:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController()
@Api(tags = "系统监测控制类")
@RequestMapping(value = "monitor", produces = MediaType.APPLICATION_JSON_VALUE)
public class MonitoringController {

    @Autowired
    private SystemPanoramaService panoramaService;

    @GetMapping("pump/{deviceId}")
    @ApiOperation(value = "主泵-获取监测信息(包含实时预警信息)")
    public ReturnResponse getMonitorInfoPump(@PathVariable @NotNull Long deviceId) {
        Map<String, MeasurePointVO> points = panoramaService.getMonitorInfo(deviceId);
        return ResponseUtils.success(points);
    }

    @GetMapping("turbine/{deviceId}")
    @ApiOperation(value = "汽机-获取监测信息-设备测点展示(包含实时预警信息)")
    public ReturnResponse getMonitorInfoTurbine(@PathVariable @NotNull Long deviceId) {
        Map<String, MeasurePointVO> points = panoramaService.getMonitorInfo(deviceId);
        return ResponseUtils.success(points);
    }

    @GetMapping("rotary/{deviceId}")
    @ApiOperation(value = "旋转机械-获取监测信息-设备测点展示(包含实时预警信息)")
    public ReturnResponse getMonitorInfoOfRotary(@PathVariable @NotNull Long deviceId) {
        RotaryMonitorVO vo = panoramaService.getMonitorInfoOfRotary(deviceId);
        return ResponseUtils.success(vo);
    }

    @GetMapping("valve/{deviceId}")
    @ApiOperation(value = "阀-获取监测信息-设备测点展示(包含实时预警信息)")
    public ReturnResponse getMonitorInfoValve(@PathVariable @NotNull Long deviceId) {
        Map<String, MeasurePointVO> points = panoramaService.getMonitorInfo(deviceId);
        return ResponseUtils.success(points);
    }

    @GetMapping("exchanger/{deviceId}")
    @ApiOperation(value = "换热器-获取监测信息-设备测点展示(包含实时预警信息)")
    public ReturnResponse getMonitorInfoExchanger(@PathVariable @NotNull Long deviceId) {
        Map<String, MeasurePointVO> points = panoramaService.getMonitorInfo(deviceId);
        return ResponseUtils.success(points);
    }

    @Autowired
    private PumpEquipmentMonitoringService monitoringService;

    //----------------------------------------------非共用----------------------------------------------------
    @GetMapping("status/{deviceId}")
    @ApiOperation(value = "换热器/阀的设备状态")
    public ReturnResponse getExchangerOrValveStatus(@PathVariable Long deviceId) {
        boolean flag = panoramaService.getExchangerOrValveStatus(deviceId);
        return ResponseUtils.success(flag);
    }

    @GetMapping("exchanger/flow/max")
    @ApiOperation(value = "换热器流量最大值接口")
    public ReturnResponse getExchangerFlowMax() {
        Double value = panoramaService.getExchangerFlowMax();
        return ResponseUtils.success(value);
    }

    @GetMapping("rotary/{deviceId}/status")
    @ApiOperation(value = "旋转机械-获取运行状态信息-启停/健康状态/总运行时长...")
    public ReturnResponse getMonitorStatusOfRotary(@PathVariable @NotNull Long deviceId) {
        Object vo = monitoringService.getMonitorStatusOfRotary(deviceId);
        return ResponseUtils.success(vo);
    }

    @GetMapping("rotary/{deviceId}/statistics/running")
    @ApiOperation(value = "旋转机械-运行时常统计")
    public ReturnResponse getMonitorStatisticsOfRotary(@PathVariable @NotNull Long deviceId) {
        MeasurePointTimesScaleVO vos = monitoringService.getMonitorStatisticsOfRotary(deviceId);
        return ResponseUtils.success(vos);
    }


}
