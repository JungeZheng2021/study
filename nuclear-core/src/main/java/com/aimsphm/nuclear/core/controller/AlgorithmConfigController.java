package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.AlgorithmConfigDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.AlgorithmConfigService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 功能描述:算法配置-前端控制器
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/12/23 10:54
 */
@RestController
@Api(tags = "AlgorithmConfig-算法配置-相关接口")
@RequestMapping(value = "algorithm/config", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmConfigController {

    @Autowired
    private AlgorithmConfigService service;

    @GetMapping("list")
    @ApiOperation(value = "算法配置分页查询", notes = "多条件组合查询")
    public Page<AlgorithmConfigDO> listAlgorithmConfigByPageWithParams(Page<AlgorithmConfigDO> page, AlgorithmConfigDO entity, ConditionsQueryBO query) {
        return service.listAlgorithmConfigByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "算法配置获取某一实体")
    public AlgorithmConfigDO getAlgorithmConfigDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "算法配置新增数据")
    public boolean saveAlgorithmConfig(@RequestBody AlgorithmConfigDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "算法配置修改数据")
    public boolean modifyAlgorithmConfig(@RequestBody AlgorithmConfigDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "算法配置批量删除数据")
    public boolean batchRemoveAlgorithmConfig(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "算法配置删除数据")
    public boolean removeAlgorithmConfig(@PathVariable Long id) {
        return service.removeById(id);
    }
}