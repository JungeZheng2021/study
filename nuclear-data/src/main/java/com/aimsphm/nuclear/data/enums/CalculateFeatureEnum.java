package com.aimsphm.nuclear.data.enums;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <需要计算的特征类型枚举类>
 * @Author: MILLA
 * @CreateDate: 2020/11/23 16:55
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/23 16:55
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum CalculateFeatureEnum {

    abr_fe40("abr-fe40", "40-59微米范围微粒数(铁磁性)"),
    abr_fe60("abr-fe60", "60-99微米范围微粒数(铁磁性)"),
    abr_fe100("abr-fe100", "100-299微米范围微粒数(铁磁性)"),
    abr_fe300("abr-fe300", "300-399微米范围微粒数(铁磁性)"),
    abr_fe400("abr-fe400", "≥400微米范围微粒数(铁磁性)"),
    abr_nfe135("abr-nfe135", "135-149微米范围微粒数(非铁磁性)"),
    abr_nfe150("abr-nfe150", "150-249微米范围微粒数(非铁磁性)"),
    abr_nfe250("abr-nfe250", "250-349微米范围微粒数(非铁磁性)"),
    abr_nfe350("abr-nfe350", "350-449微米范围微粒数(非铁磁性)"),
    abr_nfe450("abr-nfe450", "≥450微米范围微粒数(非铁磁性)");

    /**
     * 真实英文
     */
    private String value;
    /**
     * 显示值
     */
    private String label;

    CalculateFeatureEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public static String getLabel(String value) {
        if (value == null) {
            return null;
        }
        CalculateFeatureEnum[] instances = CalculateFeatureEnum.values();
        for (CalculateFeatureEnum i : instances) {
            if (value.equals(i.getValue())) {
                return i.getLabel();
            }
        }

        return null;
    }

    public static CalculateFeatureEnum value(String value) {
        if (value == null) {
            return null;
        }
        CalculateFeatureEnum[] instances = CalculateFeatureEnum.values();
        for (CalculateFeatureEnum i : instances) {
            if (value.equals(i.getValue())) {
                return i;
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
