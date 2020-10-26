package com.aimsphm.nuclear.common.entity.vo;

import lombok.Data;

@Data
public class CommonSensorVO {
    private Long id;
    private String tagId;
    private Boolean isVirtual;
    private String sensorName;
    private String sensorDesc;
    private String unit;
    /**
     * 表达式
     */
    private String expression;


}
