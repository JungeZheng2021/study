package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.AlgorithmRulesDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.AlgorithmRulesService;
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
@Api(tags = "AlgorithmRules--相关接口")
@RequestMapping(value = "algorithm/rules", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmRulesController {

    @Resource
    private AlgorithmRulesService service;

    @GetMapping("list")
    @ApiOperation(value = "列表查询", notes = "多条件组合查询")
    public List<AlgorithmRulesDO> listAlgorithmRulesWithParams(AlgorithmRulesDO entity, ConditionsQueryBO query) {
        return service.listAlgorithmRulesWithParams(new QueryBO(entity, query));
    }

    @GetMapping("pages")
    @ApiOperation(value = "分页查询", notes = "多条件组合查询")
    public Page<AlgorithmRulesDO> listAlgorithmRulesByPageWithParams(Page<AlgorithmRulesDO> page, AlgorithmRulesDO entity, ConditionsQueryBO query) {
        return service.listAlgorithmRulesByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "获取某一实体")
    public AlgorithmRulesDO getAlgorithmRulesDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public boolean saveAlgorithmRules(@RequestBody AlgorithmRulesDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "修改数据")
    public boolean modifyAlgorithmRules(@RequestBody AlgorithmRulesDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "批量删除数据")
    public boolean batchRemoveAlgorithmRules(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "删除数据")
    public boolean removeAlgorithmRules(@PathVariable Long id) {
        return service.removeById(id);
    }
}