package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.AlgorithmModelDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.AlgorithmModelService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 功能描述:算法模型信息-前端控制器
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/12/23 14:30
 */
@RestController
@Api(tags = "AlgorithmModel-算法模型信息-相关接口")
@RequestMapping(value = "algorithm/model", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmModelController {

    @Resource
    private AlgorithmModelService service;

    @GetMapping("list")
    @ApiOperation(value = "算法模型信息查询", notes = "多条件组合查询")
    public List<AlgorithmModelDO> listAlgorithmModelWithParams(AlgorithmModelDO entity, ConditionsQueryBO query) {
        return service.listAlgorithmModelWithParams(new QueryBO(entity, query));
    }

    @GetMapping("pages")
    @ApiOperation(value = "算法模型信息分页查询", notes = "多条件组合查询")
    public Page<AlgorithmModelDO> listAlgorithmModelByPageWithParams(Page<AlgorithmModelDO> page, AlgorithmModelDO entity, ConditionsQueryBO query) {
        return service.listAlgorithmModelByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "算法模型信息获取某一实体")
    public AlgorithmModelDO getAlgorithmModelDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "算法模型信息新增数据")
    public boolean saveAlgorithmModel(@RequestBody AlgorithmModelDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "算法模型信息修改数据")
    public boolean modifyAlgorithmModel(@RequestBody AlgorithmModelDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "算法模型信息批量删除数据")
    public boolean batchRemoveAlgorithmModel(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "算法模型信息删除数据")
    public boolean removeAlgorithmModel(@PathVariable Long id) {
        return service.removeById(id);
    }
}