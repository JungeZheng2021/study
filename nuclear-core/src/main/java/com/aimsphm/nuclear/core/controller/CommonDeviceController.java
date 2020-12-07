package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.ext.service.CommonDeviceServiceExt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <设备信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "device-设备信息-相关接口")
@RequestMapping(value = "/common/device", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonDeviceController {
    @Autowired
    private CommonDeviceServiceExt iCommonDeviceServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "设备信息分页查询")
    public Page<CommonDeviceDO> listCommonDeviceServiceByPage(QueryBO<CommonDeviceDO> query) {
        return iCommonDeviceServiceExt.page(query.getPage() == null ? new Page() : query.getPage(), query.initQueryWrapper());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "设备信息获取某一实体")
    public CommonDeviceDO getCommonDeviceServiceDetails(@PathVariable Long id) {
        return iCommonDeviceServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "设备信息新增数据")
    public boolean saveCommonDeviceService(@RequestBody CommonDeviceDO dto) {
        return iCommonDeviceServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "设备信息修改数据")
    public boolean modifyCommonDeviceService(@RequestBody CommonDeviceDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iCommonDeviceServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "设备信息批量删除数据")
    public boolean batchRemoveCommonDeviceService(@RequestParam(value = "ids") List<Long> ids) {
        return iCommonDeviceServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "设备信息删除数据")
    public boolean removeCommonDeviceService(@PathVariable Long id) {
        return iCommonDeviceServiceExt.removeById(id);
    }
}