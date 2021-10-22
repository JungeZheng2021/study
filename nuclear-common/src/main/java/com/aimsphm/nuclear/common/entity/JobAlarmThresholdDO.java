package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <阈值报警信息实体>
 * @Author: MILLA
 * @CreateDate: 2021-01-07
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-07
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("job_alarm_threshold")
@ApiModel(value = "阈值报警信息实体")
public class JobAlarmThresholdDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -4676108111823987302L;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "子系统id", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "设备编号", notes = "")
    private String deviceCode;

    @ApiModelProperty(value = "设备名称", notes = "")
    private String deviceName;

    @ApiModelProperty(value = "测点", notes = "")
    private String pointId;

    @ApiModelProperty(value = "报警名称", notes = "")
    private String name;

    @ApiModelProperty(value = "事件分类-暂时没有用到", notes = "")
    private Integer category;

    @ApiModelProperty(value = "报警编号", notes = "")
    private String alarmCode;

    @ApiModelProperty(value = "报警类型:", notes = "-1其他、1阈值、2波动、3尖峰、4阶跃、5算法")
    private Integer alarmType;

    @ApiModelProperty(value = "报警级别", notes = "1：低低报 2：低报 3：低预警 4：高预警 5：高报 6：高高报")
    private Integer alarmLevel;

    @ApiModelProperty(value = "报警原因", notes = "")
    private String alarmReason;

    @ApiModelProperty(value = "报警内容", notes = "")
    private String alarmContent;

    @ApiModelProperty(value = "报警状态", notes = "0：活动中，1：已结束")
    private Integer alarmStatus;

    @ApiModelProperty(value = "处理状态", notes = "1:未处理 2：已确认 3：已忽略")
    private Integer operateStatus;

    @ApiModelProperty(value = "开始报警时间", notes = "")
    private Date gmtStartAlarm;

    @ApiModelProperty(value = "最后报警时间", notes = "")
    private Date gmtEndAlarm;

    @ApiModelProperty(value = "备注", notes = "")
    private String remark;

}