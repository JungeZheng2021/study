package com.aimsphm.nuclear.common.entity.dto;

import lombok.Data;

@Data
public class TrendFeatureCellDTO {
    private Long timestamp;
    private Object mean; //均值
    private Object std;
}
