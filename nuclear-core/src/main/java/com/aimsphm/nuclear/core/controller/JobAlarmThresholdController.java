package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.JobAlarmThresholdDO;
import com.aimsphm.nuclear.common.entity.bo.AlarmQueryBO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.JobAlarmThresholdService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <阈值报警信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2021-01-04
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "JobAlarmThreshold-阈值报警信息-相关接口")
@RequestMapping(value = "job/alarmThreshold", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobAlarmThresholdController {

    @Resource
    private JobAlarmThresholdService service;

    @GetMapping("list")
    @ApiOperation(value = "阈值报警信息分页查询", notes = "多条件组合查询")
    public Page<JobAlarmThresholdDO> listJobAlarmThresholdByPageWithParams(Page<JobAlarmThresholdDO> page, JobAlarmThresholdDO entity, AlarmQueryBO query) {
        return service.listJobAlarmThresholdByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("list/export")
    @ApiOperation(value = "阈值报警信息导出")
    public void listJobAlarmThresholdByPageWithParams(JobAlarmThresholdDO entity, AlarmQueryBO query, HttpServletResponse response) {
        service.listJobAlarmThresholdByPageWithParams(new QueryBO(null, entity, query), response);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "阈值报警信息获取某一实体")
    public JobAlarmThresholdDO getJobAlarmThresholdDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "阈值报警信息新增数据")
    public boolean saveJobAlarmThreshold(@RequestBody JobAlarmThresholdDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "阈值报警信息修改数据")
    public boolean modifyJobAlarmThreshold(@RequestBody JobAlarmThresholdDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "阈值报警信息批量删除数据")
    public boolean batchRemoveJobAlarmThreshold(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "阈值报警信息删除数据")
    public boolean removeJobAlarmThreshold(@PathVariable Long id) {
        return service.removeById(id);
    }
}