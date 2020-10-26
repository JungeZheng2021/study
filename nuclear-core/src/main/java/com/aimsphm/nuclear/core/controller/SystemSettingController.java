package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.bo.SysLogQueryBo;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.response.ReturnResponse;
import com.aimsphm.nuclear.core.entity.bo.MdSensorDaqConfigBO;
import com.aimsphm.nuclear.core.entity.bo.MdSensorExtrainfoBO;
import com.aimsphm.nuclear.core.entity.bo.MdSensorConfigBO;
import com.aimsphm.nuclear.core.service.SystemSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Mao
 * @since 2020-05-07
 */
@Slf4j
@RestController
@RequestMapping("systemSetting")
@Api(tags = "系统设置功能")
public class SystemSettingController {
    @Autowired
    SystemSettingService systemSettingService;


    @ApiOperation(value = "获取设备报警列表")
    @GetMapping("getDeviceAlarmSetting/{subSystemId}")
    public Object getDeviceAlarmSetting(@PathVariable(name = "subSystemId") Long subSystemId) {
        Object obj = systemSettingService.getDeviceAlarmSetting(subSystemId);
        return ResponseUtils.success(obj);
    }

    @ApiOperation(value = "重置设备报警")
    @PutMapping("resetAlarm/{deviceId}")
    public Object resetAlarm(@PathVariable(name = "deviceId") Long deviceId) {
        systemSettingService.resetAlarm(deviceId);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "启动设备报警")
    @PutMapping("enableAlarm/{deviceId}")
    public Object enableAlarm(@PathVariable(name = "deviceId") Long deviceId) {
        systemSettingService.enableAlarm(deviceId);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "停止设备报警")
    @PutMapping("disableAlarm/{deviceId}")
    public Object disableAlarm(@PathVariable(name = "deviceId") Long deviceId) {
        systemSettingService.disableAlarm(deviceId);
        return ResponseUtils.success();
    }

    @ApiOperation(value = "缓存清除接口")
    @PutMapping("purgeRedis")
    public Object purgeRedis() {
        systemSettingService.purgeRedis();
        return ResponseUtils.success();
    }

    @ApiOperation(value = "传感器设置---操作编辑接口",notes = "传感器设置页面---修改传感器型号/序列号/灵敏度（此接口只用于更新，相关表内容手工添加维护）")
    @PutMapping("sensorConfig")
    public ReturnResponse<Integer> modifySensorDetail(@RequestBody MdSensorExtrainfoBO bo) {
        return ResponseUtils.success(systemSettingService.modifySensorDetail(bo));
    }

    @ApiOperation(value = "传感器设置---详情列表接口",notes = "传入对应设备（如循环水泵A）id")
    @GetMapping("sensorConfig/{deviceId}")
    public ReturnResponse<Map<String,List<MdSensorConfigBO>>> sensorConfigList(@PathVariable(name = "deviceId") Long deviceId) {
        return ResponseUtils.success(systemSettingService.sensorConfigList(deviceId));
    }

    @ApiOperation(value = "传感器设置---采样配置详情接口",notes = "传入一个与设备采样配置相关的传感器id作为sensorId，得到该组传感器的采样配置")
    @GetMapping("sensorConfig/daqConfig/{sensorId}")
    public ReturnResponse<List<MdSensorDaqConfigBO>> daqConfigDetail(@PathVariable(name = "sensorId") Long sensorId) {
        return ResponseUtils.success(systemSettingService.daqConfigDetail(sensorId));
    }

    @ApiOperation(value = "传感器设置---采样配置应用接口",notes = "新增、修改传感器采样配置(根据需求，只允许手动采样配置模式)")
    @PostMapping("sensorConfig/daqConfig")
    public ReturnResponse<Boolean> daqConfig(@RequestBody MdSensorDaqConfigBO bo) {
        return ResponseUtils.success(systemSettingService.daqConfig(bo));
    }

    @ApiOperation(value = "查询用户操作日志")
    @GetMapping("getDailyUsersLog")
    public Object getDailyUsersLog(SysLogQueryBo sysLogQueryBo) {
        return ResponseUtils.success(systemSettingService.getDailyUsersLog(sysLogQueryBo));
    }

}
