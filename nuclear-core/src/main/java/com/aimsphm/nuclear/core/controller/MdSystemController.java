package com.aimsphm.nuclear.core.controller;


import com.aimsphm.nuclear.common.pojo.QueryObject;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.util.CommonUtil;
import com.aimsphm.nuclear.core.entity.MdSystem;
import com.aimsphm.nuclear.core.service.MdSystemService;
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
@RequestMapping("mdSystem")
@Api(tags = "系统接口")
public class MdSystemController {

    @Autowired
    private MdSystemService iMdSystemService;

    @PostMapping("list")
    @ApiOperation(value = "查询系统列表接口")
    public Object getMdSystemList(@RequestBody QueryObject<MdSystem> queryObject){
        QueryWrapper<MdSystem> queryWrapper = CommonUtil.initQueryWrapper(queryObject);
        return ResponseUtils.success(iMdSystemService.page(queryObject.getPage()==null?new Page():queryObject.getPage(), queryWrapper));}

    @PostMapping
    @ApiOperation(value = "新增系统接口")
    public Object saveMdSystem(@RequestBody MdSystem MdSystem){
        return ResponseUtils.success(iMdSystemService.save(MdSystem));
    }

    @PutMapping
    @ApiOperation(value = "修改系统接口")
    public Object modifyMdSystem(@RequestBody MdSystem MdSystem){
        return ResponseUtils.success(iMdSystemService.updateById(MdSystem));
    }

    @DeleteMapping
    @ApiOperation(value = "删除系统接口")
    public Object delMdSystem(@RequestBody String ids){
        return ResponseUtils.success(iMdSystemService.removeByIds(Arrays.asList(ids.split(","))));
    }
}
