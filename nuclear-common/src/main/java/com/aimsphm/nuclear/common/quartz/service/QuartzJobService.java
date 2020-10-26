package com.aimsphm.nuclear.common.quartz.service;

import org.quartz.SchedulerException;

/**
 * @Package: com.aimsphm.nuclear.common.quartz.service
 * @Description: <>
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
