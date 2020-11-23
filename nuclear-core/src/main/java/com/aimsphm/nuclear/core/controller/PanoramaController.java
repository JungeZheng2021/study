package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.core.service.PanoramaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.pump.controller
 * @Description: <各设备监测控制类>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 9:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 9:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController()
@Api(tags = "系统总览控制类")
@RequestMapping(value = "overview", produces = MediaType.APPLICATION_JSON_VALUE)
public class PanoramaController {
    @Autowired
    private PanoramaService panoramaService;

    @GetMapping("details")
    @ApiOperation(value = "系统总览")
    public Map<String, MeasurePointVO> getDeviceMonitorInfo(@PathVariable @NotNull Long deviceId) {
//        return panoramaService.getMonitorInfo(deviceId);
        return null;
    }
}
