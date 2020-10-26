package com.aimsphm.nuclear.common.entity;


import java.util.Date;


import com.aimsphm.nuclear.common.entity.ModelBase;

import lombok.Data;

/**
 * @author lu.yi
 * @since 2020-04-07
 */
@Data
public class TxPumpsnapshot extends ModelBase {
    private static final long serialVersionUID = -4431450104050316382L;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    private Long heathRunningTime;
    /**
     * 健康状态
     */
    private Integer healthStatus;
    private Long totalRunningDuration;
    private Double currentGearShiftSpeed;
    private Integer stopTimes;
    private Long gearShiftSpeedG100;
    private Long gearShiftSpeedG88;
    private Long gearShiftSpeedG50;
    private Long gearShiftSpeedG23;
    private Long overD35Temp;
    private Long overD36Temp;
    /**
     * 快照时间
     */
    private Date snapshotTime;
    private Date lastStopTime;
    /**
     * 设备附加类型
     */
    private Integer additionalType;
}