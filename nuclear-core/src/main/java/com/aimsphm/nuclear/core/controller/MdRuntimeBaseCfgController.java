package com.aimsphm.nuclear.core.controller;


import com.aimsphm.nuclear.common.pojo.QueryObject;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.util.CommonUtil;
import com.aimsphm.nuclear.core.entity.MdRuntimeBaseCfg;
import com.aimsphm.nuclear.core.service.MdRuntimeBaseCfgService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mao
 * @since 2020-05-11
 */
@Slf4j
@RestController
@RequestMapping("mdRuntimeBaseCfg")
@Api(tags = "系统初始统计信息设置接口")
public class MdRuntimeBaseCfgController {

    @Autowired
    private MdRuntimeBaseCfgService iMdRuntimeBaseCfgService;


    @GetMapping("getCfgByDeviceId")
    @ApiOperation(value = "根据设备分组编号得到相应的设置记录")
    public Object getCfgByDeviceId(@RequestParam Long deviceId) {
        return ResponseUtils.success(iMdRuntimeBaseCfgService.getAllConfigByDeviceId(deviceId));
    }

    @GetMapping("getCfgBySubSystemId")
    @ApiOperation(value = "根据设备子系统得到相应的设置记录", notes = "针对气机，以子系统分类标识")
    public Object getCfgBySubSystemId(@RequestParam Long subSystemId) {
        return ResponseUtils.success(iMdRuntimeBaseCfgService.getCreateAllConfigBySubSystemId(subSystemId));
    }

    @PostMapping("updateConfigBySubSystemId")
    @ApiOperation(value = "根据设备子系统设置初始化记录", notes = "针对气机，以子系统分类标识")
    public Object updateConfigBySubSystemId(@RequestBody List<MdRuntimeBaseCfg> cfgList) {

        try {
            iMdRuntimeBaseCfgService.updateConfigBySubSystemId(cfgList);
        } catch (Exception ex) {
            log.error("DB Config update failed");
            return ResponseUtils.error("DB Config update failed.inconsistency happened");
        }
        return ResponseUtils.success("cfg record updated successfully");
    }

    @PostMapping("updateConfigByDeviceId")
    @ApiOperation(value = "根据设备分组编号设置记录")
    public Object saveCfgByDeviceId(@RequestBody List<MdRuntimeBaseCfg> cfgList) {

        try {
            iMdRuntimeBaseCfgService.updateConfigByDeviceId(cfgList);
        } catch (Exception ex) {
            log.error("DB Config update failed");
            return ResponseUtils.error("DB Config update failed.inconsistency happened");
        }
        return ResponseUtils.success("cfg record updated successfully");
    }

    @PostMapping("updateSingleConfig")
    @ApiOperation(value = "根据数据库的id设置相应记录")
    public Object saveSingleCfg(@RequestBody MdRuntimeBaseCfg cfg) {

        try {
            iMdRuntimeBaseCfgService.updateSingleConfig(cfg);
        } catch (Exception ex) {
            log.error("DB Config update failed");
            return ResponseUtils.error(ex.getMessage());
        }
        return ResponseUtils.success("cfg record updated successfully");
    }

    @PostMapping("list")
    @ApiOperation(value = "得到所有设备的设置信息")
    public Object getMdRuntimeBaseCfgList(@RequestBody QueryObject<MdRuntimeBaseCfg> queryObject) {
        QueryWrapper<MdRuntimeBaseCfg> queryWrapper = CommonUtil.initQueryWrapper(queryObject);
        return ResponseUtils.success(iMdRuntimeBaseCfgService.page(queryObject.getPage() == null ? new Page() : queryObject.getPage(), queryWrapper));
    }

    @PostMapping
    public Object saveMdRuntimeBaseCfg(@RequestBody MdRuntimeBaseCfg MdRuntimeBaseCfg) {
        return ResponseUtils.success(iMdRuntimeBaseCfgService.save(MdRuntimeBaseCfg));
    }

    @PutMapping
    public Object modifyMdRuntimeBaseCfg(@RequestBody MdRuntimeBaseCfg MdRuntimeBaseCfg) {
        return ResponseUtils.success(iMdRuntimeBaseCfgService.updateById(MdRuntimeBaseCfg));
    }

    @DeleteMapping
    public Object delMdRuntimeBaseCfg(@RequestParam String ids) {
        return ResponseUtils.success(iMdRuntimeBaseCfgService.removeByIds(Arrays.asList(ids.split(","))));
    }
}
