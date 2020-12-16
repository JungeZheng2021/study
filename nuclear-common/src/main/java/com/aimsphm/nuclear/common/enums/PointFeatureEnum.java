package com.aimsphm.nuclear.common.enums;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <测点特征类型枚举类(一级特征)>
 * @Author: MILLA
 * @CreateDate: 2020/11/23 16:55
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/23 16:55
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum PointFeatureEnum {
    ABR("abr", "磨损分析"),
    ACC("acc", "加速度分析"),
    ANA("ana", "油品分析"),
    //    CMD("cmd", ""),
    RAW("raw", "时频分析"),
    //    UNU("unu", ""),
    VEC("vec", "速度分析"),
    WEI("wei", "诊断分析");
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
        if (value == null) {
            return null;
        }
        PointFeatureEnum[] instances = PointFeatureEnum.values();
        for (PointFeatureEnum i : instances) {
            if (value.equals(i.getValue())) {
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
