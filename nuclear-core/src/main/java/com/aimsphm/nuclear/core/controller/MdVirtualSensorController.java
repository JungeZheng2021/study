package com.aimsphm.nuclear.core.controller;


import java.util.Arrays;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.aimsphm.nuclear.common.entity.MdVirtualSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.aimsphm.nuclear.common.service.MdVirtualSensorService;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.aimsphm.nuclear.common.pojo.QueryObject;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.util.CommonUtil;

/**
 *
 * @author lu.yi
 * @since 2020-06-10
 */
@Slf4j
@RestController
@RequestMapping("mdVirtualSensor")
@Api(tags = "虚拟测点接口")
public class MdVirtualSensorController {

    @Autowired
    private MdVirtualSensorService iMdVirtualSensorService;

    @PostMapping("list")
    @ApiOperation(value = "查询虚拟测点列表接口")
    public Object getMdVirtualSensorList(@RequestBody QueryObject<MdVirtualSensor> queryObject){
        QueryWrapper<MdVirtualSensor> queryWrapper = CommonUtil.initQueryWrapper(queryObject);
        return ResponseUtils.success(iMdVirtualSensorService.page(queryObject.getPage()==null?new Page():queryObject.getPage(), queryWrapper));}

    @PostMapping
    @ApiOperation(value = "保存虚拟测点接口")
    public Object saveMdVirtualSensor(@RequestBody MdVirtualSensor MdVirtualSensor){
        return ResponseUtils.success(iMdVirtualSensorService.save(MdVirtualSensor));
    }

    @PutMapping
    @ApiOperation(value = "修改虚拟测点接口")
    public Object modifyMdVirtualSensor(@RequestBody MdVirtualSensor MdVirtualSensor){
        return ResponseUtils.success(iMdVirtualSensorService.updateById(MdVirtualSensor));
    }

    @DeleteMapping
    @ApiOperation(value = "删除虚拟测点接口")
    public Object delMdVirtualSensor(@RequestParam String ids){
        return ResponseUtils.success(iMdVirtualSensorService.removeByIds(Arrays.asList(ids.split(","))));
    }
}
