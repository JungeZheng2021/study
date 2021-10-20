package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:测点特征类型枚举类(一级特征)
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum PointFeatureEnum {
    ABR("abr", "磨损分析"),
    ACC("acc", "加速度"),
    ENVE("enve", "加速度"),
    ANA("ana", "油品分析"),
    //    CMD("cmd", ""),
    RAW("raw", "应力波强度"),
    //    UNU("unu", ""),
    VEC("vec", "速度"),
    WEI("wei", "特征显著性");
    /**
     * 真实英文
     */
    private String value;
    /**
     * 显示值
     */
    private String label;

    PointFeatureEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String getLabel(String value) {
        if (Objects.isNull(value)) {
            return null;
        }
        PointFeatureEnum[] instances = PointFeatureEnum.values();
        for (PointFeatureEnum i : instances) {
            if (i.getValue().equals(value)) {
                return i.getLabel();
            }
        }

        return null;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
