package com.aimsphm.nuclear.common.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseSnapshotInfo implements Serializable {
    private static final long serialVersionUID = 6233440584058502729L;
    private Integer healthStatus;
    private Long heathRunningTime;
    private Date snapshotTime;
    private Long totalRunningDuration;
    private Integer stopTimes;
}
