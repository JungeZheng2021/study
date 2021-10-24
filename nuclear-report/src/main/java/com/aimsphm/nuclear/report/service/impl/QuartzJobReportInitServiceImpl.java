package com.aimsphm.nuclear.report.service.impl;

import com.aimsphm.nuclear.common.entity.BizJobQuartzConfigDO;
import com.aimsphm.nuclear.common.quartz.dto.QuartzJobDTO;
import com.aimsphm.nuclear.common.quartz.enums.QuartzJobStateEnum;
import com.aimsphm.nuclear.common.quartz.service.QuartzJobService;
import com.aimsphm.nuclear.common.quartz.util.QuartzManager;
import com.aimsphm.nuclear.common.service.BizJobQuartzConfigService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 功能描述:启动的时候加载加载在运行状态的任务并启动
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/5/12 19:39
 */
@Service("report")
public class QuartzJobReportInitServiceImpl implements QuartzJobService {
    @Resource
    private QuartzManager quartzUtil;
    @Resource
    private BizJobQuartzConfigService quartzConfigService;

    @Override
    public void initSchedule() throws SchedulerException, ClassNotFoundException {
        // 获取所有报告定时任务
        LambdaQueryWrapper<BizJobQuartzConfigDO> wrapper = Wrappers.lambdaQuery(BizJobQuartzConfigDO.class);
        wrapper.eq(BizJobQuartzConfigDO::getJobGroup, "report");
        List<BizJobQuartzConfigDO> jobList = quartzConfigService.list(wrapper);
        if (CollectionUtils.isEmpty(jobList)) {
            return;
        }
        for (BizJobQuartzConfigDO job : jobList) {
            if (QuartzJobStateEnum.RUNNING.getCode().equals(job.getJobStatus())) {
                QuartzJobDTO jobDTO = new QuartzJobDTO();
                BeanUtils.copyProperties(job, jobDTO);
                quartzUtil.addJob(jobDTO);
            }
        }
    }
}
