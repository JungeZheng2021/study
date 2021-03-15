package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonSensorDO;
import com.aimsphm.nuclear.common.entity.CommonSensorSettingsDO;
import com.aimsphm.nuclear.core.service.SettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Package: com.aimsphm.nuclear.pump.controller
 * @Description: <系统设置>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 9:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 9:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController()
@Api(tags = "Settings-系统设置")
@RequestMapping(value = "setting", produces = MediaType.APPLICATION_JSON_VALUE)
public class SettingsController {
    @Resource
    private SettingsService settingsService;

    @PutMapping("/monitor/{deviceId}/{enable}")
    @ApiOperation(value = "报警状态改变")
    public boolean updateDeviceMonitorStatus(@PathVariable Long deviceId, @PathVariable Integer enable) {
        return settingsService.updateDeviceMonitorStatus(deviceId, enable == 0 ? false : true);
    }

    @PutMapping("/reset/{deviceId}")
    @ApiOperation(value = "设备报警重置")
    public boolean updateDeviceEventProduce(@PathVariable Long deviceId) {
        return settingsService.updateDeviceEventProduce(deviceId);
    }

    @PutMapping("sensor/{id}")
    @ApiOperation(value = "传感器信息修改数据", notes = "包括灵敏度，序列号，传感器编码等等")
    public boolean modifyCommonSensor(@RequestBody CommonSensorDO dto, @PathVariable Long id) {
        dto.setId(id);
        return settingsService.modifyCommonSensor(dto);
    }

    @PutMapping("config/{id}")
    @ApiOperation(value = "配置信息修改", notes = "采集配置、数据清零")
    public boolean modifyCommonSensorSettings(@RequestBody CommonSensorSettingsDO dto, @PathVariable Long id) {
        dto.setId(id);
        return settingsService.saveOrModifyCommonSensorSettings(dto);
    }
}
