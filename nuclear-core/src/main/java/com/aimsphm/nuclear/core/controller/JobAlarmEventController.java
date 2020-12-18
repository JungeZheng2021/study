package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.ext.service.JobAlarmEventServiceExt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <报警事件-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2020-12-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "报警事件-相关接口")
@RequestMapping(value = "/job/alarmEvent", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobAlarmEventController {
    @Autowired
    private JobAlarmEventServiceExt iJobAlarmEventServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "报警事件分页查询")
    public Page<JobAlarmEventDO> listJobAlarmEventByPageWithParams(Page<JobAlarmEventDO> page, JobAlarmEventDO entity, ConditionsQueryBO query) {
        return iJobAlarmEventServiceExt.listJobAlarmEventByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "报警事件获取某一实体")
    public JobAlarmEventDO getJobAlarmEventServiceDetails(@PathVariable Long id) {
        return iJobAlarmEventServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "报警事件新增数据")
    public boolean saveJobAlarmEventService(@RequestBody JobAlarmEventDO dto) {
        return iJobAlarmEventServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "报警事件修改数据")
    public boolean modifyJobAlarmEventService(@RequestBody JobAlarmEventDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iJobAlarmEventServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "报警事件批量删除数据")
    public boolean batchRemoveJobAlarmEventService(@RequestParam(value = "ids") List<Long> ids) {
        return iJobAlarmEventServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "报警事件删除数据")
    public boolean removeJobAlarmEventService(@PathVariable Long id) {
        return iJobAlarmEventServiceExt.removeById(id);
    }
}