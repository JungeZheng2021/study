package com.aimsphm.nuclear.core.controller;


import java.util.Arrays;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.aimsphm.nuclear.common.entity.TxPumpsnapshot;
import com.aimsphm.nuclear.common.pojo.QueryObject;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.service.TxPumpsnapshotService;
import com.aimsphm.nuclear.common.util.CommonUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author lu.yi
 * @since 2020-03-18
 */
@Slf4j
@RestController
@RequestMapping("txPumpsnapshot")
@Api(tags = "主泵快照基本接口")
public class TxPumpsnapshotController {

    @Autowired
    private TxPumpsnapshotService iTxPumpsnapshotService;

    @PostMapping("list")
    @ApiOperation(value = "主泵快照查询接口")
    public Object getTxPumpsnapshotList(@RequestBody QueryObject<TxPumpsnapshot> queryObject){
        QueryWrapper<TxPumpsnapshot> queryWrapper = CommonUtil.initQueryWrapper(queryObject);
        return ResponseUtils.success(iTxPumpsnapshotService.page(queryObject.getPage()==null?new Page():queryObject.getPage(), queryWrapper));}

    @PostMapping
    @ApiOperation(value = "新增主泵快照接口")
    public Object saveTxPumpsnapshot(@RequestBody TxPumpsnapshot TxPumpsnapshot){
        return ResponseUtils.success(iTxPumpsnapshotService.save(TxPumpsnapshot));
    }

    @PutMapping
    @ApiOperation(value = "修改主泵快照接口")
    public Object modifyTxPumpsnapshot(@RequestBody TxPumpsnapshot TxPumpsnapshot){
        return ResponseUtils.success(iTxPumpsnapshotService.updateById(TxPumpsnapshot));
    }

    @DeleteMapping
    @ApiOperation(value = "删除主泵快照接口")
    public Object delTxPumpsnapshot(@RequestBody String ids){
        return ResponseUtils.success(iTxPumpsnapshotService.removeByIds(Arrays.asList(ids.split(","))));
    }
    
    @GetMapping("/{id}")
    @ApiOperation(value = "获取主泵设备的快照")
	public Object getDeviceSubTree(@PathVariable(name = "id") Long id) {

		return ResponseUtils.success(iTxPumpsnapshotService.getById(id));
	}
}
