package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.core.service.SensorTrendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author lu.yi
 * @since 2020-03-18
 */
@Slf4j
@RestController
@RequestMapping("mdSensorTrend")
@Api(tags = "测点趋势特诊识别等相关接口")
public class SensorTrendController {

    @Autowired
    SensorTrendService sensorTrendService;

	@GetMapping("/getTrendHotSpot/{deviceId}")
	@ApiOperation(value = "根据设备id查询相关的实时测点，按照温度，频率等分组")
	public Object getTrendHotSpot(@PathVariable(name = "deviceId") Long deviceId) {

		return ResponseUtils.success(sensorTrendService.getTrendHotSpot(deviceId));
	}
	@GetMapping("/getTrendHotSpotBySubSystemId/{subSystemId}")
	@ApiOperation(value = "根据子系统id查询相关集群分析的实时测点，按照温度，频率等分组")
	public Object getTrendHotSpotBySubSystemId(@PathVariable(name = "subSystemId") Long subSystemId) {

		return ResponseUtils.success(sensorTrendService.getTrendHotSpotBySubSystemId(subSystemId));
	}

	@GetMapping("/getTrendHotSpotDetails")
	@ApiOperation(value = "根据tagId查询相关的测点趋势识别结果")
	public Object getTrendHotSpotDetails(@RequestParam String tags) {
		List<String> tagslist = Arrays.asList(tags.split(","));

		return ResponseUtils.success(sensorTrendService.getTrendHotSpotDetails(tagslist));
	}

	@GetMapping("/getTrendHotSpotDetails/{deviceId}")
	@ApiOperation(value = "根据deviceId查询相关的测点趋势识别结果")
	public Object getTrendHotSpotDetails(@PathVariable(name = "deviceId") Long deviceId) {

		return ResponseUtils.success(sensorTrendService.getTrendHotSpotDetails(deviceId));
	}
	@GetMapping("/getSubSystemTrendHotSpotDetails/{subSystemId}")
	@ApiOperation(value = "根据subSystemId查询相关的测点趋势识别结果")
	public Object getSubSystemTrendHotSpotDetails(@PathVariable(name = "subSystemId") Long subSystemId) {

		return ResponseUtils.success(sensorTrendService.getSubSystemTrendHotSpotDetails(subSystemId));
	}

}
