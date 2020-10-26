package com.aimsphm.nuclear.core.entity.bo;

import lombok.Data;

@Data
public class DeviationStatisticBO {
    private Double avgDeviation;
    private Double tenPercentageQuantile;
    private Double twentyPercentageQuantile;
    private Double variance;
    private Double max;
    private Double quarterPercentile;
    private Double thirdQuarterPercentile;
}
