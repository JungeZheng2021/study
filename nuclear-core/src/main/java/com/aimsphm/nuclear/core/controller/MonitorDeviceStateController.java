package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.MonitorDeviceStateDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.ext.service.MonitorDeviceStateServiceExt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <设备状态-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2020-11-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "设备状态-相关接口")
@RequestMapping(value = "/monitor/deviceState", produces = MediaType.APPLICATION_JSON_VALUE)
public class MonitorDeviceStateController {
    @Autowired
    private MonitorDeviceStateServiceExt iMonitorDeviceStateServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "设备状态分页查询")
    public Page<MonitorDeviceStateDO> listMonitorDeviceStateServiceByPage(QueryBO<MonitorDeviceStateDO> query) {
        return iMonitorDeviceStateServiceExt.page(query.getPage() == null ? new Page() : query.getPage(), query.initQueryWrapper());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "设备状态获取某一实体")
    public MonitorDeviceStateDO getMonitorDeviceStateServiceDetails(@PathVariable Long id) {
        return iMonitorDeviceStateServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "设备状态新增数据")
    public boolean saveMonitorDeviceStateService(@RequestBody MonitorDeviceStateDO dto) {
        return iMonitorDeviceStateServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "设备状态修改数据")
    public boolean modifyMonitorDeviceStateService(@RequestBody MonitorDeviceStateDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iMonitorDeviceStateServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "设备状态批量删除数据")
    public boolean batchRemoveMonitorDeviceStateService(@RequestParam(value = "ids") List<Long> ids) {
        return iMonitorDeviceStateServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "设备状态删除数据")
    public boolean removeMonitorDeviceStateService(@PathVariable Long id) {
        return iMonitorDeviceStateServiceExt.removeById(id);
    }
}