package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.AlgorithmRulesConclusionDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.AlgorithmRulesConclusionService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2021-01-29
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "AlgorithmRulesConclusion--相关接口")
@RequestMapping(value = "algorithm/rulesConclusion", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmRulesConclusionController {

    @Resource
    private AlgorithmRulesConclusionService service;

    @GetMapping("list")
    @ApiOperation(value = "列表查询", notes = "多条件组合查询")
    public List<AlgorithmRulesConclusionDO> listAlgorithmRulesConclusionWithParams(AlgorithmRulesConclusionDO entity, ConditionsQueryBO query) {
        return service.listAlgorithmRulesConclusionWithParams(new QueryBO(entity, query));
    }

    @GetMapping("pages")
    @ApiOperation(value = "分页查询", notes = "多条件组合查询")
    public Page<AlgorithmRulesConclusionDO> listAlgorithmRulesConclusionByPageWithParams(Page<AlgorithmRulesConclusionDO> page, AlgorithmRulesConclusionDO entity, ConditionsQueryBO query) {
        return service.listAlgorithmRulesConclusionByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "获取某一实体")
    public AlgorithmRulesConclusionDO getAlgorithmRulesConclusionDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public boolean saveAlgorithmRulesConclusion(@RequestBody AlgorithmRulesConclusionDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "修改数据")
    public boolean modifyAlgorithmRulesConclusion(@RequestBody AlgorithmRulesConclusionDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "批量删除数据")
    public boolean batchRemoveAlgorithmRulesConclusion(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "删除数据")
    public boolean removeAlgorithmRulesConclusion(@PathVariable Long id) {
        return service.removeById(id);
    }
}