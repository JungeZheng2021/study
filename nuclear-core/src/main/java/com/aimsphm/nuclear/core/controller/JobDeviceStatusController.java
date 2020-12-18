package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.JobDeviceStatusService;
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
 * @CreateDate: 2020-12-04
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "deviceStatus-设备状态-相关接口")
@RequestMapping(value = "/job/deviceStatus", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobDeviceStatusController {
    @Autowired
    private JobDeviceStatusService iJobDeviceStatusServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "设备状态分页查询")
    public Page<JobDeviceStatusDO> listJobDeviceStatusServiceByPage(Page<JobDeviceStatusDO> page, JobDeviceStatusDO entity, ConditionsQueryBO query) {
        return iJobDeviceStatusServiceExt.listJobDeviceStatusByPageWithParams(new QueryBO<>(page, entity, query));
    }

    @GetMapping("{deviceId}")
    @ApiOperation(value = "根据设备编号获取运行状态")
    public JobDeviceStatusDO getJobDeviceStatusDetails(@PathVariable Long deviceId) {
        return iJobDeviceStatusServiceExt.getDeviceRunningStatus(deviceId);
    }

    @PostMapping
    @ApiOperation(value = "设备状态新增数据")
    public boolean saveJobDeviceStatus(@RequestBody JobDeviceStatusDO dto) {
        return iJobDeviceStatusServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "设备状态修改数据")
    public boolean modifyJobDeviceStatus(@RequestBody JobDeviceStatusDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iJobDeviceStatusServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "设备状态批量删除数据")
    public boolean batchRemoveJobDeviceStatus(@RequestParam(value = "ids") List<Long> ids) {
        return iJobDeviceStatusServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "设备状态删除数据")
    public boolean removeJobDeviceStatus(@PathVariable Long id) {
        return iJobDeviceStatusServiceExt.removeById(id);
    }
}