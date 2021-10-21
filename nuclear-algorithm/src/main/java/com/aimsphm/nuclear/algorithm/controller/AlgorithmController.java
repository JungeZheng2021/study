package com.aimsphm.nuclear.algorithm.controller;

import com.aimsphm.nuclear.algorithm.enums.AlgorithmTypeEnum;
import com.aimsphm.nuclear.algorithm.service.*;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.aimsphm.nuclear.common.entity.JobDownSampleDO;
import com.aimsphm.nuclear.common.enums.PointTypeEnum;
import com.aimsphm.nuclear.common.service.JobDownSampleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/12/18 18:01
 */
@RestController
@RequestMapping(value = "algorithm", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmController {
    @Resource
    private AlgorithmService algorithmService;
    @Resource
    private AlgorithmAsyncService asyncService;
    @Resource
    private DownSampleService downSampleService;
    @Resource
    private JobDownSampleService bizDownSampleService;
    @Resource
    private FeatureExtractionOperationService featureExtractionService;
    @Resource
    private PrognosticForecastService prognosticForecastService;

    @GetMapping("test/{deviceId}/{type}")
    @ApiOperation(value = "状态监测算法")
    public String getDeviceStateMonitorInfo(@PathVariable Long deviceId, @PathVariable String type) {
        AlgorithmTypeEnum byValue = AlgorithmTypeEnum.getByValue(type);
        if (Objects.isNull(byValue)) {
            return null;
        }
        if (byValue.equals(AlgorithmTypeEnum.THRESHOLD_MONITOR)) {
            algorithmService.deviceThresholdMonitorInfo(byValue, deviceId, 60);
        }
        if (byValue.equals(AlgorithmTypeEnum.STATE_MONITOR)) {
            algorithmService.deviceStateMonitorInfo(byValue, deviceId, 10 * 60);
        }
        return String.format("%s 算法运行成功", byValue.getDesc());
    }

    @GetMapping("test")
    @ApiOperation(value = "计算特征数据")
    public void operationFeatureExtractionData() {
        featureExtractionService.operationFeatureExtractionData(PointTypeEnum.CALCULATE);
    }

    @GetMapping("test/{componentId}")
    @ApiOperation(value = "征兆预测")
    public void prognosticForecastByComponentId(@PathVariable Long componentId) {
        prognosticForecastService.prognosticForecastByComponentId(componentId);
    }

    @GetMapping("test/sample")
    @ApiOperation(value = "手动降采样(一周)")
    public void downSampleService() {
        List<JobDownSampleDO> list = bizDownSampleService.list();
        List<Long> collect = list.stream().map(BaseDO::getId).collect(Collectors.toList());
        bizDownSampleService.removeByIds(collect);
        IntStream.rangeClosed(-7 * 24, 0).forEach(x -> {
            long l = System.currentTimeMillis() + x * 3600 * 1000L;
            downSampleService.executeOnce(new Date(l), -1L);
        });
    }

    @GetMapping("test/test")
    @ApiOperation(value = "手动降采样(执行一次)")
    public void downSampleServiceTest() {
        downSampleService.execute();
    }

    @GetMapping("delete/{deviceId}")
    @ApiOperation(value = "手动删除指定的设备下的Rms值", notes = "如果end有值，会直接删除，如果为空会休眠之后删除")
    public void deleteData(@PathVariable Long deviceId, Long end) {
        asyncService.deleteData(deviceId, end);
    }
}
