package com.milla.study.netbase.expert.quartz.service;

import org.quartz.SchedulerException;

/**
 * @Package: com.milla.study.netbase.expert.quartz.service
 * @Description: <初始化任务>
 * @Author: MILLA
 * @CreateDate: 2020/5/12 19:38
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/12 19:38
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface QuartzJobService {
    void initSchedule() throws SchedulerException, ClassNotFoundException;
}
