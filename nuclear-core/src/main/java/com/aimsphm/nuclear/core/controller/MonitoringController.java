package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.core.service.MonitoringService;
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
@Api(tags = "monitor-系统监测控制类")
@RequestMapping(value = "monitor", produces = MediaType.APPLICATION_JSON_VALUE)
public class MonitoringController {
    @Autowired
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


}
