package com.aimsphm.nuclear.data.enums;

/**
 * <p>
 * 功能描述:需要计算的特征类型枚举类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/10/23 11:27
 */
public enum CalculateFeatureEnum {

    ABR_FE40("abr-fe40", "40-59微米范围微粒数(铁磁性)"),
    ABR_FE60("abr-fe60", "60-99微米范围微粒数(铁磁性)"),
    ABR_FE100("abr-fe100", "100-299微米范围微粒数(铁磁性)"),
    ABR_FE300("abr-fe300", "300-399微米范围微粒数(铁磁性)"),
    ABR_FE400("abr-fe400", "≥400微米范围微粒数(铁磁性)"),
    ABR_NFE135("abr-nfe135", "135-149微米范围微粒数(非铁磁性)"),
    ABR_NFE150("abr-nfe150", "150-249微米范围微粒数(非铁磁性)"),
    ABR_NFE250("abr-nfe250", "250-349微米范围微粒数(非铁磁性)"),
    ABR_NFE350("abr-nfe350", "350-449微米范围微粒数(非铁磁性)"),
    ABR_NFE450("abr-nfe450", "≥450微米范围微粒数(非铁磁性)");

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
