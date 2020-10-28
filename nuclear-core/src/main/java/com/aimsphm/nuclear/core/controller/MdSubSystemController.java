package com.aimsphm.nuclear.core.controller;


import com.aimsphm.nuclear.common.entity.MdSubSystem;
import com.aimsphm.nuclear.common.pojo.QueryObject;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.aimsphm.nuclear.common.util.CommonUtil;
import com.aimsphm.nuclear.core.service.MdSubSystemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * @author lu.yi
 * @since 2020-03-18
 */
@Slf4j
@RestController
@RequestMapping("mdSubSystem")
@Api(tags = "子系统接口")
public class MdSubSystemController {

    @Autowired
    private MdSubSystemService iMdSubSystemService;

    @PostMapping("list")
    @ApiOperation(value = "查询子系统列表接口")
    public Object getMdSubSystemList(@RequestBody QueryObject<MdSubSystem> queryObject) {
        QueryWrapper<MdSubSystem> queryWrapper = CommonUtil.initQueryWrapper(queryObject);
        return ResponseUtils.success(iMdSubSystemService.page(queryObject.getPage() == null ? new Page() : queryObject.getPage(), queryWrapper));
    }

    @PostMapping
    @ApiOperation(value = "新增子系统接口")
    public Object saveMdSubSystem(@RequestBody MdSubSystem MdSubSystem) {
        return ResponseUtils.success(iMdSubSystemService.save(MdSubSystem));
    }

    @PutMapping
    @ApiOperation(value = "修改子系统接口")
    public Object modifyMdSubSystem(@RequestBody MdSubSystem MdSubSystem) {
        return ResponseUtils.success(iMdSubSystemService.updateById(MdSubSystem));
    }

    @DeleteMapping
    @ApiOperation(value = "删除子系统接口")
    public Object delMdSubSystem(@RequestBody String ids) {
        return ResponseUtils.success(iMdSubSystemService.removeByIds(Arrays.asList(ids.split(","))));
    }

    @GetMapping("listAll")
    @ApiOperation(value = "查询所有子系统接口")
    public Object listAllSubSystem() {
        return ResponseUtils.success(iMdSubSystemService.list());
    }

    @GetMapping("getOne/{subSystemId}")
    @ApiOperation(value = "查询某一个子系统的接口")
    public ResponseData<MdSubSystem> getOne(@PathVariable(name = "subSystemId") Long subSystemId) {
        return ResponseUtils.success(iMdSubSystemService.getById(subSystemId));

    }

}
