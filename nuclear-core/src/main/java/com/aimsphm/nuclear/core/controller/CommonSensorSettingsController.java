package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonSensorSettingsDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.CommonSensorSettingsService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <传感器信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2021-01-21
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-21
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "CommonSensorSettings-传感器设置-相关接口")
@RequestMapping(value = "common/sensorSettings", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonSensorSettingsController {

    @Resource
    private CommonSensorSettingsService service;

    @GetMapping("list")
    @ApiOperation(value = "传感器信息列表查询", notes = "多条件组合查询")
    public List<CommonSensorSettingsDO> listCommonSensorSettingsWithParams(CommonSensorSettingsDO entity, ConditionsQueryBO query) {
        return service.listCommonSensorSettingsWithParams(new QueryBO(entity, query));
    }

    @GetMapping("list/{edgeId}/{category}")
    @ApiOperation(value = "根据边缘设备id获取配置信息")
    public CommonSensorSettingsDO getCommonSensorByEdgeId(@PathVariable Integer edgeId, @PathVariable Integer category) {
        return service.getCommonSensorByEdgeId(edgeId, category);
    }

    @GetMapping("pages")
    @ApiOperation(value = "传感器信息分页查询", notes = "多条件组合查询")
    public Page<CommonSensorSettingsDO> listCommonSensorSettingsByPageWithParams(Page<CommonSensorSettingsDO> page, CommonSensorSettingsDO entity, ConditionsQueryBO query) {
        return service.listCommonSensorSettingsByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "传感器信息获取某一实体")
    public CommonSensorSettingsDO getCommonSensorSettingsDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "传感器信息新增数据")
    public boolean saveCommonSensorSettings(@RequestBody CommonSensorSettingsDO dto) {
        return service.save(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "传感器信息批量删除数据")
    public boolean batchRemoveCommonSensorSettings(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "传感器信息删除数据")
    public boolean removeCommonSensorSettings(@PathVariable Long id) {
        return service.removeById(id);
    }
}