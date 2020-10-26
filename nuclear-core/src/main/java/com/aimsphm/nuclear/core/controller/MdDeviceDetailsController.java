package com.aimsphm.nuclear.core.controller;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/6/19 12:57
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/19 12:57
 * @UpdateRemark: <>
 * @Version: 1.0
 */

import com.aimsphm.nuclear.common.entity.MdDeviceDetails;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.service.MdDeviceDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@RestController
@Api(tags = "设计信息接口")
@RequestMapping("mdDeviceDetails")
public class MdDeviceDetailsController {

    @Autowired
    private MdDeviceDetailsService iMdDeviceDetailsService;

    @GetMapping("list")
    @ApiOperation(value = "获取设备详细信息-列表")
    public Object getMdDeviceDetailsList(MdDeviceDetails query) {
        return ResponseUtils.success(iMdDeviceDetailsService.listDeviceInfo(query));
    }

    @PostMapping
    @ApiOperation(value = "新增设备详细信息")
    public Object saveMdDeviceDetails(@RequestBody MdDeviceDetails MdDeviceDetails) {
        return ResponseUtils.success(iMdDeviceDetailsService.save(MdDeviceDetails));
    }

    @PutMapping
    @ApiOperation(value = "修改设备详细信息")
    public Object modifyMdDeviceDetails(@RequestBody MdDeviceDetails MdDeviceDetails) {
        return ResponseUtils.success(iMdDeviceDetailsService.updateById(MdDeviceDetails));
    }

    @DeleteMapping
    @ApiOperation(value = "删除设备详细信息")
    public Object delMdDeviceDetails(@RequestParam String ids) {
        return ResponseUtils.success(iMdDeviceDetailsService.removeByIds(Arrays.asList(ids.split(","))));
    }
}

