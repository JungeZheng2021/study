package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalRuleDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.AlgorithmNormalRuleService;
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
 * @CreateDate: 2021-06-04
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-06-04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "AlgorithmNormalRule--相关接口")
@RequestMapping(value = "algorithm/normalRule", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlgorithmNormalRuleController {

    @Resource
    private AlgorithmNormalRuleService service;

    @GetMapping("list")
    @ApiOperation(value = "列表查询", notes = "多条件组合查询")
    public List<AlgorithmNormalRuleDO> listAlgorithmNormalRuleWithParams(AlgorithmNormalRuleDO entity, ConditionsQueryBO query) {
        return service.listAlgorithmNormalRuleWithParams(new QueryBO(entity, query));
    }

    @GetMapping("pages")
    @ApiOperation(value = "分页查询", notes = "多条件组合查询")
    public Page<AlgorithmNormalRuleDO> listAlgorithmNormalRuleByPageWithParams(Page<AlgorithmNormalRuleDO> page, AlgorithmNormalRuleDO entity, ConditionsQueryBO query) {
        return service.listAlgorithmNormalRuleByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "获取某一实体")
    public AlgorithmNormalRuleDO getAlgorithmNormalRuleDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public boolean saveAlgorithmNormalRule(@RequestBody AlgorithmNormalRuleDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "修改数据")
    public boolean modifyAlgorithmNormalRule(@RequestBody AlgorithmNormalRuleDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "批量删除数据")
    public boolean batchRemoveAlgorithmNormalRule(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "删除数据")
    public boolean removeAlgorithmNormalRule(@PathVariable Long id) {
        return service.removeById(id);
    }
}