package com.aimsphm.nuclear.common.quartz.service.impl;

import com.aimsphm.nuclear.common.entity.BizJobQuartzConfigDO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.BizJobQuartzConfigMapper;
import com.aimsphm.nuclear.common.quartz.dto.QuartzJobDTO;
import com.aimsphm.nuclear.common.quartz.enums.QuartzJobOperateEnum;
import com.aimsphm.nuclear.common.quartz.enums.QuartzJobStateEnum;
import com.aimsphm.nuclear.common.quartz.service.QuartzManagerService;
import com.aimsphm.nuclear.common.quartz.util.QuartzManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 功能描述:任务管理业务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/5/13 13:34
 */
@Service
@ConditionalOnProperty(prefix = "mybatis-plus", name = "quartz-enable", havingValue = "1")
public class QuartzManagerServiceImpl implements QuartzManagerService {
    @Autowired
    private QuartzManager manager;

    private final BizJobQuartzConfigMapper mapper;

    public QuartzManagerServiceImpl(BizJobQuartzConfigMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addJob(QuartzJobDTO job) throws ClassNotFoundException, SchedulerException {
        Assert.hasText(job.getJobName(), "任务名称不能为空");
        Assert.hasText(job.getCronExpression(), "任务执行周期不能为空");
        Assert.hasText(job.getJobGroup(), "任务分组不能为空");
        LambdaQueryWrapper<BizJobQuartzConfigDO> wrapper = Wrappers.lambdaQuery(BizJobQuartzConfigDO.class);
        wrapper.eq(BizJobQuartzConfigDO::getJobName, job.getJobName());
        BizJobQuartzConfigDO find = mapper.selectOne(wrapper);
        Assert.isNull(find, "要添加的任务已经存在");
        manager.addJob(job);
        BizJobQuartzConfigDO real = new BizJobQuartzConfigDO();
        BeanUtils.copyProperties(job, real);
        mapper.insert(real);
    }

    @Override
    public void modifyJob(Integer id, QuartzJobOperateEnum operateEnum, QuartzJobDTO job) throws SchedulerException {

        BizJobQuartzConfigDO real = mapper.selectById(id);
        Assert.notNull(real, "要操作的任务不存在");
        QuartzJobDTO find = new QuartzJobDTO();
        BeanUtils.copyProperties(real, find);
        if (StringUtils.hasText(job.getJobName())) {
            find.setJobName(job.getJobName());
        }
        if (StringUtils.hasText(job.getJobGroup())) {
            find.setJobGroup(job.getJobGroup());
        }
        if (StringUtils.hasText(job.getBeanClass())) {
            find.setBeanClass(job.getBeanClass());
        }
        if (StringUtils.hasText(job.getModifier())) {
            find.setModifier(job.getModifier());
        }
        switch (operateEnum) {
            //删除
            case stop:
                find.setJobStatus(QuartzJobStateEnum.STOPPED.getCode());
                manager.pauseJob(find);
                break;
            case resume:
                find.setJobStatus(QuartzJobStateEnum.RUNNING.getCode());
                manager.resumeJob(find);
                break;
            case update:
                if (StringUtils.isEmpty(job.getCronExpression())) {
                    throw new CustomMessageException("任务表达式书写错误");
                }
                if (StringUtils.isEmpty(job.getExpressionDesc())) {
                    find.setExpressionDesc(job.getExpressionDesc());
                }
                find.setCronExpression(job.getCronExpression());
                manager.updateJobCron(find);
                break;
            default:
                throw new CustomMessageException("任务不支持该操作");
        }
        mapper.updateById(find);
    }

    @Override
    public void removeJob(Integer id) throws SchedulerException {
        BizJobQuartzConfigDO job = mapper.selectById(id);
        Assert.notNull(job, "要操作的任务不存在");
        job.setJobStatus("-1");
        mapper.updateById(job);
        QuartzJobDTO find = new QuartzJobDTO();
        BeanUtils.copyProperties(job, find);
        manager.deleteJob(find);
    }
}
