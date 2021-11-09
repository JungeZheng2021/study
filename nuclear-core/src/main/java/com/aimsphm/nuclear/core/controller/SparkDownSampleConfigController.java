package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.SparkDownSampleConfigDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.SparkDownSampleConfigService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 功能描述:降采样配置表-前端控制器
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-11-01
 */
@RestController
@Api(tags = "SparkDownSampleConfig-降采样配置表-相关接口")
@RequestMapping(value = "spark/downSampleConfig", produces = MediaType.APPLICATION_JSON_VALUE)
public class SparkDownSampleConfigController {

    @Resource
    private SparkDownSampleConfigService service;

    @GetMapping("list")
    @ApiOperation(value = "降采样配置表列表查询", notes = "多条件组合查询")
    public List<SparkDownSampleConfigDO> listSparkDownSampleConfigWithParams(SparkDownSampleConfigDO entity, ConditionsQueryBO query) {
        return service.listSparkDownSampleConfigWithParams(new QueryBO(entity, query));
    }

    @GetMapping("pages")
    @ApiOperation(value = "降采样配置表分页查询", notes = "多条件组合查询")
    public Page<SparkDownSampleConfigDO> listSparkDownSampleConfigByPageWithParams(Page<SparkDownSampleConfigDO> page, SparkDownSampleConfigDO entity, ConditionsQueryBO query) {
        return service.listSparkDownSampleConfigByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "降采样配置表获取某一实体")
    public SparkDownSampleConfigDO getSparkDownSampleConfigDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "降采样配置表新增数据")
    public boolean saveSparkDownSampleConfig(@RequestBody SparkDownSampleConfigDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "降采样配置表修改数据")
    public boolean modifySparkDownSampleConfig(@RequestBody SparkDownSampleConfigDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "降采样配置表批量删除数据")
    public boolean batchRemoveSparkDownSampleConfig(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "降采样配置表删除数据")
    public boolean removeSparkDownSampleConfig(@PathVariable Long id) {
        return service.removeById(id);
    }
}