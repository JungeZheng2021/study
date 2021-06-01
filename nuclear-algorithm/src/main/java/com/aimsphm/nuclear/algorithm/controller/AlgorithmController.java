package com.aimsphm.nuclear.algorithm.controller;

import com.aimsphm.nuclear.algorithm.service.AlgorithmService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Package: com.aimsphm.nuclear.algorithm.controller
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/18 18:01
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/18 18:01
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "algorithm", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmController {
    @Resource
    private AlgorithmService algorithmService;

    @GetMapping("demo/{deviceId}/{p}")
    @ApiOperation(value = "获取某一实体")
    public void getDeviceStateMonitorInfo(@PathVariable Long deviceId, @PathVariable Integer p) throws IOException {
        algorithmService.getDeviceStateMonitorInfo(deviceId, p);
    }
}
