package com.aimsphm.nuclear.common.entity.bo;

import lombok.Data;

import java.util.List;

@Data
public class HistoryQueryResult {
    //private Integer algo;
    private String tag;
    private Double earlyWarningHi;
    private Double earlyWarningLo;
    private Double thrHi;
    private Double thrLo;
    private Double thrHihi;
    private Double thrLolo;
    private String unit;
    //private Long startTimestamp;
    //private String points;
    private List<List<Object>> pointList;
}
