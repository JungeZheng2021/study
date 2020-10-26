package com.aimsphm.nuclear.common.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lu.yi
 * @since 2020-04-07
 */
@Data
public class TxAlarmRealtime extends ModelBase {
    private static final long serialVersionUID = -7180491956158890581L;
    private String alarmCode;
    private Long modelId;
    private Long deviceId;
    private Long subSystemId;
    @ApiModelProperty(value = "是否是算法报警")
    private Boolean isAlgorithmAlarm;
    @ApiModelProperty(value = "关联测点")
    private String sensorTagid;
    private Date alarmTime;
    private Long eventId;
    @ApiModelProperty(value = "趋势")
    private Integer trend;
    @ApiModelProperty(value = "报警评价")
    private String evaluation;
    @TableField(exist = false)
    private String evaluationContent;
    @TableField(exist = false)
    private String sensorTagName;
    @TableField(exist = false)
    private String displaySensor;
    @TableField(exist = false)
    private String deviceName;
    private Integer alarmType;
}