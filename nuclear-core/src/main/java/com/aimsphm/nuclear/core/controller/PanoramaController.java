package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.TxAlarmEvent;
import com.aimsphm.nuclear.common.entity.TxPumpsnapshot;
import com.aimsphm.nuclear.common.entity.vo.MdDeviceVO;
import com.aimsphm.nuclear.common.mapper.MdDeviceMapper;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.response.ReturnResponse;
import com.aimsphm.nuclear.core.vo.PumpPanoramaVO;
import com.aimsphm.nuclear.pump.service.SystemPanoramaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Package: com.aimsphm.nuclear.turbine
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/6/9 18:36
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/9 18:36
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "系统总览接口")
@RequestMapping(value = "panorama", produces = MediaType.APPLICATION_JSON_VALUE)
public class PanoramaController {
    @Autowired
    private SystemPanoramaService service;

    @GetMapping("pump/{subSystemId}")
    @ApiOperation(value = "主泵系统总览信息")
    public ReturnResponse getPanoramaInfoPump(@PathVariable Long subSystemId) {
        PumpPanoramaVO vo = service.getPanoramaInfo(subSystemId);
        return ResponseUtils.success(vo);
    }

    @GetMapping("turbine/{subSystemId}")
    @ApiOperation(value = "汽机系统总览信息")
    public ReturnResponse getPanoramaInfoTurbine(@PathVariable Long subSystemId) {
        PumpPanoramaVO vo = service.getPanoramaInfo(subSystemId);
        return ResponseUtils.success(vo);
    }

    @GetMapping("rotary/{subSystemId}")
    @ApiOperation(value = "旋机系统总览信息")
    public ReturnResponse getPanoramaInfoRotary(@PathVariable Long subSystemId) {
        PumpPanoramaVO vo = service.getPanoramaInfo(subSystemId);
        return ResponseUtils.success(vo);
    }

    @Autowired
    private MdDeviceMapper deviceMapper;

    @GetMapping("exchanger/{subSystemId}")
    @ApiOperation(value = "换热器系统总览信息")
    public ReturnResponse getPanoramaInfo(@PathVariable Long subSystemId) {
        PumpPanoramaVO vo = service.getPanoramaInfo(subSystemId);
        List<MdDeviceVO> deviceList = deviceMapper.selectDeviceBySubSystemId(subSystemId);
        if (CollectionUtils.isNotEmpty(deviceList)) {
            List<TxPumpsnapshot> collect = deviceList.stream().map(item -> {
                TxPumpsnapshot snapshot = new TxPumpsnapshot();
                snapshot.setDeviceId(item.getId());
                snapshot.setDeviceName(item.getDeviceName());
                snapshot.setAdditionalType(item.getAdditionalType());
                return snapshot;
            }).collect(Collectors.toList());
            vo.setDevices(collect);
        }
        return ResponseUtils.success(vo);
    }

    //------------------------------------------预警信息----------------------------------------
    @GetMapping("pump/warning/newest/{queryId}")
    @ApiOperation(value = "获取最新的预警信息")
    public ReturnResponse getWarningNewest(@PathVariable Long queryId, Integer top, boolean type) {
        List<TxAlarmEvent> vo = service.getWarningNewest(queryId, top, type);
        return ResponseUtils.success(vo);
    }
}
