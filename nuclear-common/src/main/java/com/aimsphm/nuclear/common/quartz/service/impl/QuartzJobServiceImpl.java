package com.aimsphm.nuclear.common.quartz.service.impl;

import com.aimsphm.nuclear.common.quartz.service.QuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

/**
 * @Package: com.aimsphm.nuclear.common.quartz.service.impl
 * @Description: <为了防止注入异常增加一个默认实现>
 * @Author: MILLA
 * @CreateDate: 2020/5/12 19:39
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/12 19:39
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service("default")
public class QuartzJobServiceImpl implements QuartzJobService {

    @Override
    public void initSchedule() throws SchedulerException, ClassNotFoundException {
        log.debug("initSchedule");
    }
}
