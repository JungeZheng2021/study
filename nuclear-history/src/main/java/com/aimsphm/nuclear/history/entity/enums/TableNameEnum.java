package com.aimsphm.nuclear.history.entity.enums;

/**
 * @Package: com.aimsphm.nuclear.history.entity.enums
 * @Description: <降采样表名枚举类>
 * @Author: MILLA
 * @CreateDate: 2020/11/21 11:39
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/21 11:39
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum TableNameEnum {
    DAILY("spark_down_sample_daily"),
    WEEKLY("spark_down_sample_weekly"),
    HALF_MONTHLY("spark_down_sample_half_monthly"),
    MONTHLY("spark_down_sample_monthly"),
    QUARTERLY("spark_down_sample_quarterly"),
    HALF_ANNUALLY("spark_down_sample_half_annually"),
    ANNUALLY("spark_down_sample_annually"),
    DOUBLE_ANNUALLY("spark_down_sample_double_annually"),
    TRIPLE_ANNUALLY("spark_down_sample_triple_annually");

    TableNameEnum(String value) {
        this.value = value;
    }

    public static TableNameEnum getByValue(String value) {
        if (value == null) {
            return null;
        }

        TableNameEnum[] instances = TableNameEnum.values();
        for (TableNameEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
                return i;
            }
        }

        return null;
    }

    public String getValue() {
        return value;
    }

    private String value;
}
