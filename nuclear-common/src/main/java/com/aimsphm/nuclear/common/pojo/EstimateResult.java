package com.aimsphm.nuclear.common.pojo;

import lombok.Data;

@Data
public class EstimateResult {
private String tagId;
private Double actual;
private Double estimate;
private Double residual;
private Integer alarmCode;
private Long timeStamp;
}
