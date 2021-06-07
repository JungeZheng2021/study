package com.aimsphm.nuclear.algorithm.enums;


import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.algorithm.enums
 * @Description: <特征值枚举类>
 * @Author: MILLA
 * @CreateDate: 2020/12/17 13:47
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/17 13:47
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum FeatureNameEnum {
    PT("PT", "acc-ZeroPeak", "峰值趋势"),

    PGF("PGF", "abr-total", "油液微粒增长特征"),

    MPE("MPE", "周期性频率能量比"),

    BGER3("MPE", "窄带组能量比3"),

    GMSBF("GMSBF", "齿轮啮合边带特征"),

    MPFF3("MPFF3", "最大周期频率特征3"),

    OTHERS("-1", "其他");


    FeatureNameEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    FeatureNameEnum(String type, String value, String desc) {
        this.type = type;
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(String value) {
        FeatureNameEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return null;
        }
        return typeEnum.getDesc();
    }

    public static FeatureNameEnum getByValue(String type) {
        if (type == null) {
            return null;
        }

        FeatureNameEnum[] instances = FeatureNameEnum.values();
        for (FeatureNameEnum i : instances) {
            if (type.equals(i.getType())) {
                return i;
            }
        }

        return null;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        return value;
    }

    private String type;

    private String value;

    private String desc;

}
