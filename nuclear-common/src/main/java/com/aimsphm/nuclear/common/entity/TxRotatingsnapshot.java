package com.aimsphm.nuclear.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author lu.yi
 * @since 2020-08-18
 */
@Data
public class TxRotatingsnapshot extends ModelBase {

    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    private Integer lowVoltageSensor;
    private Integer connectFaultSensor;
    private Integer faultSensor;
    /**
     * 上次启机时间
     */
    private Date lastStartTime;
    /**
     * 上次停机时间
     */
    private Date lastStopTime;
    /**
     * 持续运行时长-(非停机状态)
     */
    private Long heathRunningTime;
    /**
     * 健康状态// 0:健康 1:待观察 2:预警 3:报警 4:停机
     */
    private Integer healthStatus;
    /**
     * 总运行时长
     */
    private Long totalRunningDuration;
    /**
     * 总启停次数
     */
    private Integer stopTimes;
    /**
     * 快照时间
     */
    private Date snapshotTime;
    private Integer additionalType;
}