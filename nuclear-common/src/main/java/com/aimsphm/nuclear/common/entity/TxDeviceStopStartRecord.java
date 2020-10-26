package com.aimsphm.nuclear.common.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.aimsphm.nuclear.common.entity.ModelBase;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author lu.yi
 * @since 2020-06-12
 */
@Data
public class TxDeviceStopStartRecord extends ModelBase {
    private static final long serialVersionUID = -4987584212380110441L;
    /**
     * 测点名称
     */
    private Long deviceId;
    private Integer actionTimes;
    private Integer startFlag;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startBeginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startEndTime;
    private Long durationOfStart;
    /**
     * 启动说明
     */
    private String startBrief;
    /**
     * 启动备注
     */
    private String startNote;
    private Integer stopFlag;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stopBeginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stopEndTime;
    private Long durationOfStop;
    /**
     * 停机说明
     */
    private String stopBrief;
    /**
     * 停机备注
     */
    private Integer deviceType;
    private String stopNote;
}