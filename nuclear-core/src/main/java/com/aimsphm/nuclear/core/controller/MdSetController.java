package com.aimsphm.nuclear.core.controller;


import com.aimsphm.nuclear.common.pojo.QueryObject;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.util.CommonUtil;
import com.aimsphm.nuclear.core.entity.MdSet;
import com.aimsphm.nuclear.core.service.MdSetService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 
 * @author lu.yi
 * @since 2020-03-18
 */
@Slf4j
@RestController
@RequestMapping("mdSet")
@Api(tags = "机组接口")
public class MdSetController {

    @Autowired
    private MdSetService iMdSetService;

    @PostMapping("list")
    @ApiOperation(value = "查询机组列表接口")
    public Object getMdSetList(@RequestBody QueryObject<MdSet> queryObject){
        QueryWrapper<MdSet> queryWrapper = CommonUtil.initQueryWrapper(queryObject);
        return ResponseUtils.success(iMdSetService.page(queryObject.getPage()==null?new Page():queryObject.getPage(), queryWrapper));}

    @PostMapping
    @ApiOperation(value = "新建机组接口")
    public Object saveMdSet(@RequestBody MdSet MdSet){
        return ResponseUtils.success(iMdSetService.save(MdSet));
    }

    @PutMapping
    @ApiOperation(value = "修改机组接口")
    public Object modifyMdSet(@RequestBody MdSet MdSet){
        return ResponseUtils.success(iMdSetService.updateById(MdSet));
    }

    @DeleteMapping
    @ApiOperation(value = "删除机组接口")
    public Object delMdSet(@RequestBody String ids){
        return ResponseUtils.success(iMdSetService.removeByIds(Arrays.asList(ids.split(","))));
    }
}
