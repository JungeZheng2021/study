package com.aimsphm.nuclear.core.controller;


import com.aimsphm.nuclear.common.pojo.QueryObject;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.aimsphm.nuclear.common.util.CommonUtil;
import com.aimsphm.nuclear.core.entity.MdPumpinfo;
import com.aimsphm.nuclear.core.service.MdPumpinfoService;
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
@RequestMapping("mdPumpinfo")
@Api(tags = "主泵信息接口")
public class MdPumpinfoController {

    @Autowired
    private MdPumpinfoService iMdPumpinfoService;

    @GetMapping("listByDeviceId")
    @ApiOperation(value = "通过设备id获取主泵设计信息")
    public Object getMdPumpinfoListByDeviceId(@RequestParam Long deviceId) {
        QueryObject<MdPumpinfo> qo = new QueryObject<>();
        MdPumpinfo pumpInfo = new MdPumpinfo();
        pumpInfo.setDeviceId(deviceId);
        qo.setQueryObject(pumpInfo);
        QueryWrapper<MdPumpinfo> queryWrapper = CommonUtil.initQueryWrapper(qo);
        return ResponseUtils.success(iMdPumpinfoService.list(queryWrapper));
    }

    @PostMapping("savePumpInfoWithTs")
    @ApiOperation(value = "保存主泵设计信息")
    public Object saveMdPumpinfoOptismicLock(@RequestBody MdPumpinfo MdPumpinfo) {
        try {
            iMdPumpinfoService.updatePumpInfo(MdPumpinfo);
        } catch (Exception ex) {
            return ResponseUtils.systemError(ex.getMessage());

        }
        return ResponseUtils.success("update successfully");
    }

    @PostMapping("list")
    @ApiOperation(value = "获取主泵信息列表")
    public Object getMdPumpinfoList(@RequestBody QueryObject<MdPumpinfo> queryObject) {
        QueryWrapper<MdPumpinfo> queryWrapper = CommonUtil.initQueryWrapper(queryObject);
        return ResponseUtils.success(iMdPumpinfoService.page(queryObject.getPage() == null ? new Page() : queryObject.getPage(), queryWrapper));
    }

    @PostMapping
    @ApiOperation(value = "保存主泵信息")
    public Object saveMdPumpinfo(@RequestBody MdPumpinfo MdPumpinfo) {
        return ResponseUtils.success(iMdPumpinfoService.save(MdPumpinfo));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "根据数据库主键id获取主泵详细信息")
    public ResponseData getPumpInfoById(@PathVariable Long id) {//此处传数据库id
        return ResponseUtils.success(iMdPumpinfoService.getById(id));
    }

    @PutMapping
    @ApiOperation(value = "修改主泵信息")
    public Object modifyMdPumpinfo(@RequestBody MdPumpinfo MdPumpinfo) {
        return ResponseUtils.success(iMdPumpinfoService.updateById(MdPumpinfo));
    }

    @DeleteMapping
    @ApiOperation(value = "删除主泵信息")
    public Object delMdPumpinfo(@RequestBody String ids) {
        return ResponseUtils.success(iMdPumpinfoService.removeByIds(Arrays.asList(ids.split(","))));
    }


}
