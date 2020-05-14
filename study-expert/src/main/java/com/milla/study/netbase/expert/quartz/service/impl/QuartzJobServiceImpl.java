package com.milla.study.netbase.expert.quartz.service.impl;

import com.milla.study.netbase.expert.quartz.service.QuartzJobService;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

/**
 * @Package: com.milla.study.netbase.expert.quartz.service.impl
 * @Description: <为了防止注入异常增加一个默认实现>
 * @Author: MILLA
 * @CreateDate: 2020/5/12 19:39
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/12 19:39
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service("default")
public class QuartzJobServiceImpl implements QuartzJobService {

    @Override
    public void initSchedule() throws SchedulerException, ClassNotFoundException {
    }
}
