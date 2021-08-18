package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.JobDownSampleDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.JobDownSampleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <等间隔降采样数据-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2021-07-27
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-07-27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "JobDownSample-等间隔降采样数据-相关接口")
@RequestMapping(value = "job/downSample", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobDownSampleController {

    @Resource
    private JobDownSampleService service;

    @GetMapping("list")
    @ApiOperation(value = "等间隔降采样数据列表查询", notes = "多条件组合查询")
    public List<JobDownSampleDO> listBizDownSampleWithParams(JobDownSampleDO entity, ConditionsQueryBO query) {
        return service.listBizDownSampleWithParams(new QueryBO(entity, query));
    }

    @GetMapping("pages")
    @ApiOperation(value = "等间隔降采样数据分页查询", notes = "多条件组合查询")
    public Page<JobDownSampleDO> listBizDownSampleByPageWithParams(Page<JobDownSampleDO> page, JobDownSampleDO entity, ConditionsQueryBO query) {
        return service.listBizDownSampleByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "等间隔降采样数据获取某一实体")
    public JobDownSampleDO getBizDownSampleDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "等间隔降采样数据新增数据")
    public boolean saveBizDownSample(@RequestBody JobDownSampleDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "等间隔降采样数据修改数据")
    public boolean modifyBizDownSample(@RequestBody JobDownSampleDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "等间隔降采样数据批量删除数据")
    public boolean batchRemoveBizDownSample(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "等间隔降采样数据删除数据")
    public boolean removeBizDownSample(@PathVariable Long id) {
        return service.removeById(id);
    }
}