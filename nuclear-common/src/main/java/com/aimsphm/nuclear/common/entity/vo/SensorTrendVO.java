package com.aimsphm.nuclear.common.entity.vo;

import lombok.Data;

@Data
public class SensorTrendVO {
    private String tagId;
    private Integer trend;
    private Double changeRatio;

    public String desc(Integer trend) {
        String desc = null;
        switch (trend) {
            case 21:
                desc = "上升";
                break;
            case 22:
                desc = "下降";
                break;
            case 10:
                desc = "平稳";
                break;
            case 20:
                desc = "平稳";
                break;
            default:
                break;
        }
        return desc;
    }
}
