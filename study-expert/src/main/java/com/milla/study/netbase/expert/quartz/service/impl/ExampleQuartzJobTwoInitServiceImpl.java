package com.milla.study.netbase.expert.quartz.service.impl;

import com.milla.study.netbase.expert.quartz.dto.QuartzJobDTO;
import com.milla.study.netbase.expert.quartz.enums.QuartzJobStateEnum;
import com.milla.study.netbase.expert.quartz.mapper.QuartzJobDTOMapper;
import com.milla.study.netbase.expert.quartz.service.QuartzJobService;
import com.milla.study.netbase.expert.quartz.util.QuartzManager;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Package: com.milla.study.netbase.expert.quartz.service.impl
 * @Description: <启动的时候加载加载在运行状态的任务并启动>
 * @Author: MILLA
 * @CreateDate: 2020/5/12 19:39
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/12 19:39
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service("report")
public class ExampleQuartzJobTwoInitServiceImpl implements QuartzJobService {
    @Autowired
    private QuartzManager quartzUtil;
    @Autowired
    private QuartzJobDTOMapper mapper;

    @Override
    public void initSchedule() throws SchedulerException, ClassNotFoundException {
        // 获取所有报告的定时任务
        List<QuartzJobDTO> jobList = mapper.selectJobByGroup("report");
        if (CollectionUtils.isEmpty(jobList)) {
            return;
        }
        for (QuartzJobDTO job : jobList) {
            if (QuartzJobStateEnum.RUNNING.getCode().equals(job.getJobStatus())) {
                quartzUtil.addJob(job);
            }
        }
    }
}
