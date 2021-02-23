package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.AlgorithmRulesConclusionDO;
import com.aimsphm.nuclear.common.entity.BizDiagnosisResultDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.BizDiagnosisResultService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <故障诊断信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2021-02-01
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-01
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "BizDiagnosisResult-故障诊断信息-相关接口")
@RequestMapping(value = "biz/diagnosis", produces = MediaType.APPLICATION_JSON_VALUE)
public class BizDiagnosisResultController {

    @Resource
    private BizDiagnosisResultService service;

    @GetMapping("list")
    @ApiOperation(value = "故障诊断信息列表查询", notes = "多条件组合查询")
    public List<BizDiagnosisResultDO> listBizDiagnosisResultWithParams(BizDiagnosisResultDO entity, ConditionsQueryBO query) {
        return service.listBizDiagnosisResultWithParams(new QueryBO(entity, query));
    }

    @GetMapping("pages")
    @ApiOperation(value = "故障诊断信息分页查询", notes = "多条件组合查询")
    public Page<BizDiagnosisResultDO> listBizDiagnosisResultByPageWithParams(Page<BizDiagnosisResultDO> page, BizDiagnosisResultDO entity, ConditionsQueryBO query) {
        return service.listBizDiagnosisResultByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "故障诊断信息获取某一实体")
    public BizDiagnosisResultDO getBizDiagnosisResultDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "故障诊断信息新增数据")
    public boolean saveBizDiagnosisResult(@RequestBody BizDiagnosisResultDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "故障诊断信息修改数据")
    public boolean modifyBizDiagnosisResult(@RequestBody BizDiagnosisResultDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "故障诊断信息批量删除数据")
    public boolean batchRemoveBizDiagnosisResult(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "故障诊断信息删除数据")
    public boolean removeBizDiagnosisResult(@PathVariable Long id) {
        return service.removeById(id);
    }

    @PostMapping("event/{eventId}")
    @ApiOperation(value = "根据事件id生成故障推理结果")
    public void saveRulesConclusion(@PathVariable Long eventId) {
        service.saveRulesConclusion(eventId);
    }

    @GetMapping("event/{eventId}")
    @ApiOperation(value = "故障推理状态-根据事件id获取")
    public boolean getDiagnosisResult(@PathVariable Long eventId) {
        return service.getDiagnosisResult(eventId);
    }

    @GetMapping("event/{eventId}/last")
    @ApiOperation(value = "根据事件id获取上一次故障推理结果")
    public List<AlgorithmRulesConclusionDO> lastRulesConclusionWithEventId(@PathVariable Long eventId) {
        return service.lastRulesConclusionWithEventId(eventId);
    }

}