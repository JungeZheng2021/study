package com.aimsphm.nuclear.common.entity;

import java.time.LocalDateTime;

import com.aimsphm.nuclear.common.entity.ModelBase;
import lombok.Data;

/**
 * @author lu.yi
 * @since 2020-06-09
 */
@Data
public class TxAlarmsnapshot extends ModelBase {
    private static final long serialVersionUID = 411300447456744787L;
    /**
     * 设备id
     */

    private Long deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 正常状态运行时长
     */
    private Long normalStateRuntime;
    /**
     * 待观察状态运行时长
     */
    private Long watchingStateRuntime;
    /**
     * 预警状态运行时长
     */
    private Long warningStateRuntime;
    /**
     * 报警状态运行时长
     */
    private Long alarmingStateRuntime;
    /**
     * 累计正常状态运行时长
     */
    private Long overallNormalStateRuntime;
    /**
     * 累计待观察状态运行时长
     */
    private Long overallWatchingStateRuntime;
    /**
     * 累计预警状态运行时长
     */
    private Long overallWarningStateRuntime;
    /**
     * 累计报警状态运行时长
     */
    private Long overallAlarmingStateRuntime;
    /**
     * 快照时间
     */
    private LocalDateTime snapshotTime;
}