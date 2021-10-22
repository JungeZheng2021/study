package com.aimsphm.nuclear.common.quartz.dto;

import com.aimsphm.nuclear.common.entity.BizJobQuartzConfigDO;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.quartz.dto
 * @Description: <任务实体>
 * @Author: MILLA
 * @CreateDate: 2020/5/12 19:40
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/12 19:40
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class QuartzJobDTO extends BizJobQuartzConfigDO {
    /**
     * 主键
     */
    private Long id;
    /**
     * 任务名
     */
    private String jobName;
    /**
     * 任务描述
     */
    private String description;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 表达式描述
     */
    private String expressionDesc;
    /**
     * 任务执行时调用哪个类的方法 包名+类名
     */
    private String beanClass;
    /**
     * 任务状态
     */
    private String jobStatus;
    /**
     * 任务分组
     */
    private String jobGroup;
}
