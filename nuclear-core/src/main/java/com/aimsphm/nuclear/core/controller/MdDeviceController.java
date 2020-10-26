package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.MdDevice;
import com.aimsphm.nuclear.common.entity.TxPumpsnapshot;
import com.aimsphm.nuclear.common.entity.TxRotatingsnapshot;
import com.aimsphm.nuclear.common.pojo.QueryObject;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.service.AlgorithmCacheService;
import com.aimsphm.nuclear.common.service.MdDeviceService;
import com.aimsphm.nuclear.common.service.TxRotatingsnapshotService;
import com.aimsphm.nuclear.common.util.CommonUtil;
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
@RequestMapping("mdDevice")
@Api(tags = "通用设备类")
public class MdDeviceController {

    @Autowired
    private MdDeviceService iMdDeviceService;
    @Autowired
    private AlgorithmCacheService cacheService;
    @Autowired
    private TxRotatingsnapshotService txRotatingsnapshotService;

    @PostMapping("list")
    @ApiOperation(value = "设备列表查询")
    public Object getMdDeviceList(@RequestBody QueryObject<MdDevice> queryObject) {
        QueryWrapper<MdDevice> queryWrapper = CommonUtil.initQueryWrapper(queryObject);
        return ResponseUtils.success(iMdDeviceService
                .page(queryObject.getPage() == null ? new Page() : queryObject.getPage(), queryWrapper));
    }

    @GetMapping("/getDeviceType/{deviceId}")
    @ApiOperation(value = "通过设备id查询设备")
    public Object getDeviceType(@PathVariable(name = "deviceId") Long deviceId) throws Exception {
        Integer tp = iMdDeviceService.getTypeById(deviceId);
        return ResponseUtils.success(tp);
    }

    @PostMapping
    @ApiOperation(value = "新增设备")
    public Object saveMdDevice(@RequestBody MdDevice MdDevice) {
        return ResponseUtils.success(iMdDeviceService.save(MdDevice));
    }

    @PutMapping
    @ApiOperation(value = "修改设备")
    public Object modifyMdDevice(@RequestBody MdDevice MdDevice) {
        return ResponseUtils.success(iMdDeviceService.updateById(MdDevice));
    }

    @DeleteMapping
    @ApiOperation(value = "删除设备")
    public Object delMdDevice(@RequestBody String ids) {
        return ResponseUtils.success(iMdDeviceService.removeByIds(Arrays.asList(ids.split(","))));
    }

    @PutMapping("putPumpSnapShot")
    @ApiOperation(value = "缓存主泵快照接口")
    public Object putPumpSnapShot(@RequestBody TxPumpsnapshot txpump) throws Exception {
        cacheService.putSnapshot(0, txpump.getDeviceId(), txpump);
        return ResponseUtils.success();
    }
    @GetMapping("isMidRunningStatus")
    @ApiOperation(value = "获取设备是否在中间运行状态")
    public Object isMidRunningStatus(Long deviceId) throws Exception {
        Integer isMidStatus =  cacheService.getDeviceMidStatus(deviceId)==null?0:cacheService.getDeviceMidStatus(deviceId);
        return ResponseUtils.success(isMidStatus);
    }
    @GetMapping("getPumpSnapShot")
    @ApiOperation(value = "获取主泵快照接口")
    public Object getPumpSnapShot(Long deviceId) throws Exception {
        TxPumpsnapshot tp = cacheService.getPumpSnapshot(deviceId);
        return ResponseUtils.success(tp);
    }

    @GetMapping("getRoatatingSnapShot")
    @ApiOperation(value = "获取旋机快照接口")
    public Object getRoatatingSnapShot(Long id) throws Exception {
		TxRotatingsnapshot txRotatingsnapshot = txRotatingsnapshotService.getById(id);
		if(txRotatingsnapshot!=null) {
			cacheService.putRotatingsnapshot(txRotatingsnapshot.getDeviceId(), txRotatingsnapshot);
		}
        return ResponseUtils.success(null);
    }
}
