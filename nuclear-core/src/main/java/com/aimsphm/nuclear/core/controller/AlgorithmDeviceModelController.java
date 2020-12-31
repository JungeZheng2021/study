package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.AlgorithmDeviceModelDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.AlgorithmDeviceModelService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <算法模型关系信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2020-12-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "AlgorithmDeviceModel-算法模型关系信息-相关接口")
@RequestMapping(value = "algorithm/deviceModel", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmDeviceModelController {

    @Autowired
    private AlgorithmDeviceModelService service;

    @GetMapping("list")
    @ApiOperation(value = "算法模型关系信息分页查询", notes = "多条件组合查询")
    public Page<AlgorithmDeviceModelDO> listAlgorithmDeviceModelByPageWithParams(Page<AlgorithmDeviceModelDO> page, AlgorithmDeviceModelDO entity, ConditionsQueryBO query) {
        return service.listAlgorithmDeviceModelByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "算法模型关系信息获取某一实体")
    public AlgorithmDeviceModelDO getAlgorithmDeviceModelDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "算法模型关系信息新增数据")
    public boolean saveAlgorithmDeviceModel(@RequestBody AlgorithmDeviceModelDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "算法模型关系信息修改数据")
    public boolean modifyAlgorithmDeviceModel(@RequestBody AlgorithmDeviceModelDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "算法模型关系信息批量删除数据")
    public boolean batchRemoveAlgorithmDeviceModel(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "算法模型关系信息删除数据")
    public boolean removeAlgorithmDeviceModel(@PathVariable Long id) {
        return service.removeById(id);
    }
}