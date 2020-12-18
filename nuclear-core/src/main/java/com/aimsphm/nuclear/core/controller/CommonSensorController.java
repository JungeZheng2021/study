package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonSensorDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.ext.service.CommonSensorServiceExt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <传感器信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "sensor-传感器信息-相关接口")
@RequestMapping(value = "/common/sensor", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonSensorController {
    @Autowired
    private CommonSensorServiceExt iCommonSensorServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "传感器信息分页查询")
    public Page<CommonSensorDO> listCommonSensorServiceByPage(Page<CommonSensorDO> page, CommonSensorDO entity, ConditionsQueryBO query) {
        return iCommonSensorServiceExt.listCommonSensorByPageWithParams(new QueryBO<>(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "传感器信息获取某一实体")
    public CommonSensorDO getCommonSensorServiceDetails(@PathVariable Long id) {
        CommonSensorDO byId = iCommonSensorServiceExt.getById(id);
        byId.setSensorDesc(UUID.randomUUID().toString());
        return byId;
    }

    @PostMapping
    @ApiOperation(value = "传感器信息新增数据")
    public boolean saveCommonSensorService(@RequestBody CommonSensorDO dto) {
        return iCommonSensorServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "传感器信息修改数据")
    public boolean modifyCommonSensorService(@RequestBody CommonSensorDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iCommonSensorServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "传感器信息批量删除数据")
    public boolean batchRemoveCommonSensorService(@RequestParam(value = "ids") List<Long> ids) {
        return iCommonSensorServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "传感器信息删除数据")
    public boolean removeCommonSensorService(@PathVariable Long id) {
        return iCommonSensorServiceExt.removeById(id);
    }
}