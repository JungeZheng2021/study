package com.aimsphm.nuclear.common.entity;

import com.aimsphm.nuclear.common.entity.ModelBase;
import lombok.Data;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-08-13
 */
@Data
public class MdSensorSetting extends ModelBase {

private Long sensorId;
private String tagId;
private Double ytMin;
private Double ytMax;
private Integer sleepTime;
private Integer daqFrequency;
private Integer daqTime;
private Double fmin;
private Double fmax;
private Boolean active;
    /**
     * 0 自动 1手动
     */
private Integer mode;
}