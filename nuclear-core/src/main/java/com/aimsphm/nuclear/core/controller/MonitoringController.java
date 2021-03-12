package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.entity.vo.DeviceStatusVO;
import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.ext.service.MonitoringService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@Api(tags = "monitor-系统监测控制类")
@RequestMapping(value = "monitor", produces = MediaType.APPLICATION_JSON_VALUE)
public class MonitoringController {
    @Resource
    private MonitoringService monitoringService;

    @GetMapping("device/{deviceId}")
    @ApiOperation(value = "设备检测")
    public Map<String, MeasurePointVO> getDeviceMonitorInfo(@PathVariable @NotNull Long deviceId) {
        return monitoringService.getMonitorInfo(deviceId);
    }

    @GetMapping("point/{deviceId}")
    @ApiOperation(value = "测点监测")
    public Map<String, List<MeasurePointVO>> getPointMonitorInfo(@PathVariable @NotNull Long deviceId) {
        return monitoringService.getPointMonitorInfo(deviceId);
    }

    @GetMapping("transfinite/{deviceId}")
    @ApiOperation(value = "计算超限测点个数")
    public Map<Integer, Long> countTransfinitePoint(@PathVariable @NotNull Long deviceId) {
        return monitoringService.countTransfinitePiPoint(deviceId);
    }

    @GetMapping("{deviceId}/statistics/warning")
    @ApiOperation(value = "报警信息统计")
    public List<List<LabelVO>> listWarningPoint(@PathVariable @NotNull Long deviceId, TimeRangeQueryBO range) {
        return monitoringService.listWarningPoint(deviceId, range);
    }

    @GetMapping("{deviceId}/statistics/duration")
    @ApiOperation(value = "设备运行时长统计")
    public Map<Integer, Long> listRunningDuration(@PathVariable @NotNull Long deviceId, TimeRangeQueryBO range) {
        return monitoringService.listRunningDuration(deviceId, range);
    }

    @GetMapping("{deviceId}/running/status")
    @ApiOperation(value = "设备运行状态")
    public DeviceStatusVO getRunningStatus(@PathVariable @NotNull Long deviceId) {
        return monitoringService.getRunningStatus(deviceId);
    }

    @PutMapping("{deviceId}/running/status")
    @ApiOperation(value = "修改设备运行状态-基础值")
    public boolean modifyDeviceStatus(@RequestBody DeviceStatusVO statusVO, @PathVariable Long deviceId) {
        statusVO.setDeviceId(deviceId);
        return monitoringService.modifyDeviceStatus(statusVO);
    }
}
