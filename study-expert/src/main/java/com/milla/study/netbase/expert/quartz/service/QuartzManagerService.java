package com.milla.study.netbase.expert.quartz.service;

import com.milla.study.netbase.expert.quartz.dto.QuartzJobDTO;
import com.milla.study.netbase.expert.quartz.enums.QuartzJobOperateEnum;
import org.quartz.SchedulerException;

/**
 * @Package: com.milla.study.netbase.expert.quartz.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/5/13 13:34
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/13 13:34
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface QuartzManagerService {
    void addJob(QuartzJobDTO job) throws ClassNotFoundException, SchedulerException;

    void modifyJob(Integer id, QuartzJobOperateEnum operateEnum, QuartzJobDTO job) throws SchedulerException;

    void removeJob(Integer id) throws SchedulerException;
}
