package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <算法配置实体>
 * @Author: MILLA
 * @CreateDate: 2021-03-09
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-03-09
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("biz_job_quartz_config")
@ApiModel(value = "算法配置实体")
public class BizJobQuartzConfigDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6523588920916599942L;

    @ApiModelProperty(value = "任务名", notes = "")
    private String jobName;

    @ApiModelProperty(value = "任务描述", notes = "")
    private String description;

    @ApiModelProperty(value = "cron表达式", notes = "")
    private String cronExpression;

    @ApiModelProperty(value = "表达式描述", notes = "")
    private String expressionDesc;

    @ApiModelProperty(value = "任务执行时调用哪个类的方法", notes = "包名+类名")
    private String beanClass;

    @ApiModelProperty(value = "任务状态", notes = "0:停止 1:运行 2:暂停 -1:删除")
    private String jobStatus;

    @ApiModelProperty(value = "任务分组", notes = "")
    private String jobGroup;

}