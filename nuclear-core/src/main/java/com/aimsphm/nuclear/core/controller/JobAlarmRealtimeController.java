package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.JobAlarmRealtimeDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.JobAlarmRealtimeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2020-12-24
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-24
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "JobAlarmRealtime--相关接口")
@RequestMapping(value = "job/alarmRealtime", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobAlarmRealtimeController {

    @Autowired
    private JobAlarmRealtimeService service;

    @GetMapping("list")
    @ApiOperation(value = "分页查询", notes = "多条件组合查询")
    public Page<JobAlarmRealtimeDO> listJobAlarmRealtimeByPageWithParams(Page<JobAlarmRealtimeDO> page, JobAlarmRealtimeDO entity, ConditionsQueryBO query) {
        return service.listJobAlarmRealtimeByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "获取某一实体")
    public JobAlarmRealtimeDO getJobAlarmRealtimeDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public boolean saveJobAlarmRealtime(@RequestBody JobAlarmRealtimeDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "修改数据")
    public boolean modifyJobAlarmRealtime(@RequestBody JobAlarmRealtimeDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "批量删除数据")
    public boolean batchRemoveJobAlarmRealtime(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "删除数据")
    public boolean removeJobAlarmRealtime(@PathVariable Long id) {
        return service.removeById(id);
    }
}