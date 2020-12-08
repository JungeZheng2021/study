package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonDeviceDetailsDO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.ext.service.CommonDeviceDetailsServiceExt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <设备详细信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "deviceDetails-设备详细信息-相关接口")
@RequestMapping(value = "/common/device/details", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonDeviceDetailsController {
    @Autowired
    private CommonDeviceDetailsServiceExt iCommonDeviceDetailsServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "设备详细信息分页查询")
    public Page<CommonDeviceDetailsDO> listCommonDeviceDetailsServiceByPage(QueryBO<CommonDeviceDetailsDO> query) {
        return iCommonDeviceDetailsServiceExt.page(query.getPage() == null ? new Page() : query.getPage(), query.initQueryWrapper());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "设备详细信息获取某一实体")
    public CommonDeviceDetailsDO getCommonDeviceDetailsServiceDetails(@PathVariable Long id) {
        return iCommonDeviceDetailsServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "设备详细信息新增数据")
    public boolean saveCommonDeviceDetailsService(@RequestBody CommonDeviceDetailsDO dto) {
        return iCommonDeviceDetailsServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "设备详细信息修改数据")
    public boolean modifyCommonDeviceDetailsService(@RequestBody CommonDeviceDetailsDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iCommonDeviceDetailsServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "设备详细信息批量删除数据")
    public boolean batchRemoveCommonDeviceDetailsService(@RequestParam(value = "ids") List<Long> ids) {
        return iCommonDeviceDetailsServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "设备详细信息删除数据")
    public boolean removeCommonDeviceDetailsService(@PathVariable Long id) {
        return iCommonDeviceDetailsServiceExt.removeById(id);
    }

    @GetMapping("")
    @ApiOperation(value = "根据条件获取需要的设备信息")
    public List<CommonDeviceDetailsDO> listDetailByConditions(CommonQueryBO query) {
        return iCommonDeviceDetailsServiceExt.listDetailByConditions(query);
    }
}