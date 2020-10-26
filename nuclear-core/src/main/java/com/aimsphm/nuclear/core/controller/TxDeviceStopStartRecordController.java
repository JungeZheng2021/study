package com.aimsphm.nuclear.core.controller;


import java.util.Arrays;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.aimsphm.nuclear.common.entity.TxDeviceStopStartRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.aimsphm.nuclear.common.service.TxDeviceStopStartRecordService;
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
 * @since 2020-06-12
 */
@Slf4j
@RestController
@RequestMapping("txDeviceStopStartRecord")
@Api(tags = "启停记录基本接口")
public class TxDeviceStopStartRecordController {

    @Autowired
    private TxDeviceStopStartRecordService iTxDeviceStopStartRecordService;

    @PostMapping("list")
    @ApiOperation(value = "启停记录查询接口")
    public Object getTxDeviceStopStartRecordList(@RequestBody QueryObject<TxDeviceStopStartRecord> queryObject){
        QueryWrapper<TxDeviceStopStartRecord> queryWrapper = CommonUtil.initQueryWrapper(queryObject);
        return ResponseUtils.success(iTxDeviceStopStartRecordService.page(queryObject.getPage()==null?new Page():queryObject.getPage(), queryWrapper));}

    @PostMapping
    @ApiOperation(value = "新增启停记录接口")
    public Object saveTxDeviceStopStartRecord(@RequestBody TxDeviceStopStartRecord TxDeviceStopStartRecord){
        return ResponseUtils.success(iTxDeviceStopStartRecordService.save(TxDeviceStopStartRecord));
    }

    @PutMapping
    @ApiOperation(value = "修改启停记录接口")
    public Object modifyTxDeviceStopStartRecord(@RequestBody TxDeviceStopStartRecord TxDeviceStopStartRecord){
        return ResponseUtils.success(iTxDeviceStopStartRecordService.updateById(TxDeviceStopStartRecord));
    }

    @DeleteMapping
    @ApiOperation(value = "删除启停记录接口")
    public Object delTxDeviceStopStartRecord(@RequestParam String ids){
        return ResponseUtils.success(iTxDeviceStopStartRecordService.removeByIds(Arrays.asList(ids.split(","))));
    }
}
