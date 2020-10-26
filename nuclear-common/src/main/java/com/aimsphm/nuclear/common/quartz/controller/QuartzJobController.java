package com.aimsphm.nuclear.common.quartz.controller;

import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.quartz.dto.QuartzJobDTO;
import com.aimsphm.nuclear.common.quartz.enums.QuartzJobOperateEnum;
import com.aimsphm.nuclear.common.quartz.service.QuartzManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Package: com.aimsphm.nuclear.common.quartz.controller
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/5/13 13:23
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/13 13:23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "任务管理")
@RequestMapping(value = "quartz", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuartzJobController {

    @Autowired(required = false)
    private QuartzManagerService service;

    @PostMapping
    @ApiOperation(value = "新增任务")
    public void addJob(@RequestBody QuartzJobDTO job) throws ClassNotFoundException, SchedulerException {
        checkService();
        service.addJob(job);
    }

    private void checkService() {
        if (service == null) {
            throw new CustomMessageException("当前服务没有开启任务管理模式");
        }
    }

    @PutMapping("{id}")
    @ApiOperation(value = "修改任务", notes = "operateEnum值：stop, resume, update")
    public void modifyJob(@PathVariable Integer id, @RequestBody QuartzJobDTO job, QuartzJobOperateEnum operateEnum) throws SchedulerException {
        checkService();
        service.modifyJob(id, operateEnum, job);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "删除任务")
    public void removeJob(@PathVariable Integer id) throws SchedulerException {
        checkService();
        service.removeJob(id);
    }
}
