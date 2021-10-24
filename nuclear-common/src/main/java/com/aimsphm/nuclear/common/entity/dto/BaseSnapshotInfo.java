package com.aimsphm.nuclear.common.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/3/6 10:04
 */
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
