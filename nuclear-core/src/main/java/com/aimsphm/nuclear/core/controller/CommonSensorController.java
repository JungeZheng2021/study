package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonSensorDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.SensorVO;
import com.aimsphm.nuclear.common.service.CommonSensorService;
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
@Api(tags = "CommonSensor-传感器信息-相关接口")
@RequestMapping(value = "common/sensor", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonSensorController {

    @Resource
    private CommonSensorService service;

    @GetMapping("list")
    @ApiOperation(value = "传感器信息列表查询", notes = "多条件组合查询")
    public List<CommonSensorDO> listCommonSensorWithParams(CommonSensorDO entity, ConditionsQueryBO query) {
        return service.listCommonSensorWithParams(new QueryBO(entity, query));
    }

    @GetMapping("list/setting")
    @ApiOperation(value = "传感器设置-传感器信息列表查询", notes = "多条件组合查询")
    public List<SensorVO> listCommonSensorSettingsWithParams(CommonSensorDO entity) {
        return service.listCommonSensorSettingsWithParams(entity);
    }

    @GetMapping("pages")
    @ApiOperation(value = "传感器信息分页查询", notes = "多条件组合查询")
    public Page<CommonSensorDO> listCommonSensorByPageWithParams(Page<CommonSensorDO> page, CommonSensorDO entity, ConditionsQueryBO query) {
        return service.listCommonSensorByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "传感器信息获取某一实体")
    public CommonSensorDO getCommonSensorDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "传感器信息新增数据")
    public boolean saveCommonSensor(@RequestBody CommonSensorDO dto) {
        return service.save(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "传感器信息批量删除数据")
    public boolean batchRemoveCommonSensor(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "传感器信息删除数据")
    public boolean removeCommonSensor(@PathVariable Long id) {
        return service.removeById(id);
    }
}