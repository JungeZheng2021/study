package com.aimsphm.nuclear.common.entity;

import java.util.Date;

import lombok.Data;

/**
 * @author lu.yi
 * @since 2020-04-11
 */
@Data
public class TxSensorTrend extends ModelBase {
    private static final long serialVersionUID = 6272506437292252325L;
    private String tagId;
    private Integer trend;
    private Double changeAmplitude;
    private Double changeRatio;
    private Double zValue;
    private Double pValue;
    private Integer trendPeriod;
    private Date trendTime;
}