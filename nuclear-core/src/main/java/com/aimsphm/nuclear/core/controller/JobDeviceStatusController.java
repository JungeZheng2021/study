package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.ext.service.JobDeviceStatusServiceExt;
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
    private JobDeviceStatusServiceExt iJobDeviceStatusServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "设备状态分页查询")
    public Page<JobDeviceStatusDO> listJobDeviceStatusServiceByPage(QueryBO<JobDeviceStatusDO> query) {
        return iJobDeviceStatusServiceExt.page(query.getPage() == null ? new Page() : query.getPage(), query.initQueryWrapper());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "设备状态获取某一实体")
    public JobDeviceStatusDO getJobDeviceStatusServiceDetails(@PathVariable Long id) {
        return iJobDeviceStatusServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "设备状态新增数据")
    public boolean saveJobDeviceStatusService(@RequestBody JobDeviceStatusDO dto) {
        return iJobDeviceStatusServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "设备状态修改数据")
    public boolean modifyJobDeviceStatusService(@RequestBody JobDeviceStatusDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iJobDeviceStatusServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "设备状态批量删除数据")
    public boolean batchRemoveJobDeviceStatusService(@RequestParam(value = "ids") List<Long> ids) {
        return iJobDeviceStatusServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "设备状态删除数据")
    public boolean removeJobDeviceStatusService(@PathVariable Long id) {
        return iJobDeviceStatusServiceExt.removeById(id);
    }
}