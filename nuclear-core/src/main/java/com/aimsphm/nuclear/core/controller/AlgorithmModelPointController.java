package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.AlgorithmModelPointDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.AlgorithmModelPointService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 功能描述:模型对应测点信息-前端控制器
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/12/23 10:54
 */
@RestController
@Api(tags = "AlgorithmModelPoint-模型对应测点信息-相关接口")
@RequestMapping(value = "algorithm/modelPoint", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmModelPointController {

    @Autowired
    private AlgorithmModelPointService service;

    @GetMapping("list")
    @ApiOperation(value = "模型对应测点信息分页查询", notes = "多条件组合查询")
    public Page<AlgorithmModelPointDO> listAlgorithmModelPointByPageWithParams(Page<AlgorithmModelPointDO> page, AlgorithmModelPointDO entity, ConditionsQueryBO query) {
        return service.listAlgorithmModelPointByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "模型对应测点信息获取某一实体")
    public AlgorithmModelPointDO getAlgorithmModelPointDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "模型对应测点信息新增数据")
    public boolean saveAlgorithmModelPoint(@RequestBody AlgorithmModelPointDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "模型对应测点信息修改数据")
    public boolean modifyAlgorithmModelPoint(@RequestBody AlgorithmModelPointDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "模型对应测点信息批量删除数据")
    public boolean batchRemoveAlgorithmModelPoint(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "模型对应测点信息删除数据")
    public boolean removeAlgorithmModelPoint(@PathVariable Long id) {
        return service.removeById(id);
    }
}