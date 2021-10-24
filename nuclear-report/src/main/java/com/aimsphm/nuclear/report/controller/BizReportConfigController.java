package com.aimsphm.nuclear.report.controller;

import com.aimsphm.nuclear.common.entity.BizReportConfigDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.BizReportConfigService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 功能描述:报告生成测点配置表-前端控制器
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/5/12 18:58
 */
@RestController
@Api(tags = "BizReportConfig-报告生成测点配置表-相关接口")
@RequestMapping(value = "biz/reportConfig", produces = MediaType.APPLICATION_JSON_VALUE)
public class BizReportConfigController {

    @Resource
    private BizReportConfigService service;

    @GetMapping("list")
    @ApiOperation(value = "报告生成测点配置表列表查询", notes = "多条件组合查询")
    public List<BizReportConfigDO> listBizReportConfigWithParams(BizReportConfigDO entity, ConditionsQueryBO query) {
        return service.listBizReportConfigWithParams(new QueryBO(entity, query));
    }

    @GetMapping("pages")
    @ApiOperation(value = "报告生成测点配置表分页查询", notes = "多条件组合查询")
    public Page<BizReportConfigDO> listBizReportConfigByPageWithParams(Page<BizReportConfigDO> page, BizReportConfigDO entity, ConditionsQueryBO query) {
        return service.listBizReportConfigByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "报告生成测点配置表获取某一实体")
    public BizReportConfigDO getBizReportConfigDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "报告生成测点配置表新增数据")
    public boolean saveBizReportConfig(@RequestBody BizReportConfigDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "报告生成测点配置表修改数据")
    public boolean modifyBizReportConfig(@RequestBody BizReportConfigDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "报告生成测点配置表批量删除数据")
    public boolean batchRemoveBizReportConfig(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "报告生成测点配置表删除数据")
    public boolean removeBizReportConfig(@PathVariable Long id) {
        return service.removeById(id);
    }
}