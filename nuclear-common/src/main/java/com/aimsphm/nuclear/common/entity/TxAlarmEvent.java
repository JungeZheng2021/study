package com.aimsphm.nuclear.common.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lu.yi
 * @since 2020-03-27
 */
@Data
public class TxAlarmEvent extends ModelBase {
    private static final long serialVersionUID = 6126562769485392763L;
    private String alarmCode;
    private Long modelId;
    private Long deviceId;
    private String deviceName;
    private Long subSystemId;
    private String deviceCode;
    private Integer alarmLevel;
    private Date lastAlarm;
    private Date firstAlarm;
    private String alarmContent;
    private Integer alarmStatus;
    private Boolean stopFlag;
    private Integer alarmCount;
    private String sensorTagids;
    private Boolean isAlgorithmAlarm;
    private Double alarmFrequency;
    private Integer alarmType;
    private String alarmReason;
    private String remark;
    @TableField(exist = false)
    private List<TxAlarmRealtime> realTimeAlarms = new ArrayList<>();

    @TableField(exist = false)
    private String displaySensorTagids;
}