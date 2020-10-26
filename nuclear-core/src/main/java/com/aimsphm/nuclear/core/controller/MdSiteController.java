package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.pojo.QueryObject;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.util.CommonUtil;
import com.aimsphm.nuclear.core.entity.MdSite;
import com.aimsphm.nuclear.core.service.MdSiteService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 
 * @author lu.yi
 * @since 2020-03-18
 */
@Slf4j
@RestController
@RequestMapping("mdSite")
@Api(tags = "电厂级别接口")
public class MdSiteController {

	@Autowired
	private MdSiteService iMdSiteService;
	@Autowired
	@Qualifier("myRabbitTemplate")
	RabbitTemplate rabbitTemplate;
	@PostMapping("list")
	@ApiOperation(value = "查询电厂列表接口")
	public Object getMdSiteList(@RequestBody QueryObject<MdSite> queryObject) {
		QueryWrapper<MdSite> queryWrapper = CommonUtil.initQueryWrapper(queryObject);
		return ResponseUtils.success(
				iMdSiteService.page(queryObject.getPage() == null ? new Page() : queryObject.getPage(), queryWrapper));
	}

	@PostMapping
	@ApiOperation(value = "新增电厂接口")
	public Object saveMdSite(@RequestBody MdSite MdSite) {
		return ResponseUtils.success(iMdSiteService.save(MdSite));
	}

	@PutMapping
	@ApiOperation(value = "修改电厂接口")
	public Object modifyMdSite(@RequestBody MdSite MdSite) {
		return ResponseUtils.success(iMdSiteService.updateById(MdSite));
	}

	@DeleteMapping
	@ApiOperation(value = "删除电厂接口")
	public Object delMdSite(@RequestBody String ids) {
		return ResponseUtils.success(iMdSiteService.removeByIds(Arrays.asList(ids.split(","))));
	}

	@GetMapping("/getSiteTree/{siteId}")
	@ApiOperation(value = "通过siteId查询子层级菜单")
	public Object getSiteTree(@PathVariable(name = "siteId") Long siteId) {

		return ResponseUtils.success(iMdSiteService.getSiteTree(siteId));
	}
	
	@GetMapping("/getSetSubTree/{setId}")
	@ApiOperation(value = "通过setId查询子层级菜单")
	public Object getSetSubTree(@PathVariable(name = "setId") Long setId) {

		return ResponseUtils.success(iMdSiteService.getSetSubTree(setId));
	}
	@GetMapping("/getSubSystemSubTree/{subSystemId}")
	@ApiOperation(value = "通过subSystemId查询子层级菜单")
	public Object getSubSystemSubTree(@PathVariable(name = "subSystemId") Long subSystemId) {

		return ResponseUtils.success(iMdSiteService.getSubSystemSubTree(subSystemId));
	}

	@GetMapping("/getSubSystemSubTreeSensors/{subSystemId}")
	@ApiOperation(value = "通过subSystemId查询子层级传感器")
	public Object getSubSystemSubTreeSensors(@PathVariable(name = "subSystemId") Long subSystemId) {

		return ResponseUtils.success(iMdSiteService.getSensorsBySubSystemId(subSystemId));
	}

	@GetMapping("/getSystemSubTree/{systemId}")
	@ApiOperation(value = "通过SystemId查询子层级菜单")
	public Object getSystemSubTree(@PathVariable(name = "systemId") Long systemId) {

		return ResponseUtils.success(iMdSiteService.getSystemSubTree(systemId));
	}
	
	@GetMapping("/getDeviceSubTree/{deviceId}")
	@ApiOperation(value = "通过deviceId查询子层级菜单")
	public Object getDeviceSubTree(@PathVariable(name = "deviceId") Long deviceId) {

		return ResponseUtils.success(iMdSiteService.getDeviceSubTree(deviceId));
	}
}
