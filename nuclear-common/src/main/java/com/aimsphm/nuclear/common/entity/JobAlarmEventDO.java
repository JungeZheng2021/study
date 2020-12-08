package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <报警事件信息实体>
 * @Author: MILLA
 * @CreateDate: 2020-12-08
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-08
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("job_alarm_event")
@ApiModel(value = "报警事件信息实体")
public class JobAlarmEventDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -5160324752664202338L;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "子系统id", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "设备编号", notes = "")
    private String deviceCode;

    @ApiModelProperty(value = "设备名称", notes = "")
    private String deviceName;

    @ApiModelProperty(value = "测点集合", notes = "")
    private String tagIds;

    @ApiModelProperty(value = "报警事件/事件名称", notes = "")
    private String eventName;

    @ApiModelProperty(value = "事件分类", notes = "")
    private Integer eventCategory;

    @ApiModelProperty(value = "报警编号", notes = "")
    private String alarmCode;

    @ApiModelProperty(value = "报警类型:", notes = "-1其他、1阈值、2波动、3尖峰、4阶跃、5算法")
    private Integer alarmType;

    @ApiModelProperty(value = "报警级别", notes = "1：1级报警、2：2级报警、3：3级报警、4：4级报警、5：5级报警")
    private Integer alarmLevel;

    @ApiModelProperty(value = "报警发生次数", notes = "")
    private Integer alarmCount;

    @ApiModelProperty(value = "报警原因", notes = "")
    private String alarmReason;

    @ApiModelProperty(value = "报警频率", notes = "")
    private Double alarmFrequency;

    @ApiModelProperty(value = "报警内容", notes = "")
    private String alarmContent;

    @ApiModelProperty(value = "处理状态", notes = "")
    private Integer operateStatus;

    @ApiModelProperty(value = "开始报警时间", notes = "")
    private Date gmtFirstAlarm;

    @ApiModelProperty(value = "最后报警时间", notes = "")
    private Date gmtLastAlarm;

    @ApiModelProperty(value = "备注", notes = "")
    private String remark;

}