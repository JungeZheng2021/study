package com.aimsphm.nuclear.common.enums;

import com.aimsphm.nuclear.common.constant.MillisecondValueConstant;

import java.util.Objects;


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
    /**
     * 天表-单位是小时
     */
    DAILY("spark_down_sample_daily", MillisecondValueConstant.MILLISECOND_VALUE_OF_AN_HOUR),
    /**
     * 周表-单位是天
     */
    WEEKLY("spark_down_sample_weekly", MillisecondValueConstant.MILLISECOND_VALUE_OF_A_DAY),
    /**
     * 半月表-单位是天
     */
    HALF_MONTHLY("spark_down_sample_half_monthly", MillisecondValueConstant.MILLISECOND_VALUE_OF_A_DAY),
    /**
     * 月表-单位是天
     */
    MONTHLY("spark_down_sample_monthly", MillisecondValueConstant.MILLISECOND_VALUE_OF_A_DAY),
    /**
     * 季度表-单位是周
     */
    QUARTERLY("spark_down_sample_quarterly", MillisecondValueConstant.MILLISECOND_VALUE_OF_A_WEEK),
    /**
     * 半年表-单位是15天
     */
    HALF_ANNUALLY("spark_down_sample_half_annually", MillisecondValueConstant.MILLISECOND_VALUE_OF_A_HALF_MONTH),
    /**
     * 年表-单位是15天
     */
    ANNUALLY("spark_down_sample_annually", MillisecondValueConstant.MILLISECOND_VALUE_OF_A_HALF_MONTH),
    /**
     * 2年表-单位是30天
     */
    DOUBLE_ANNUALLY("spark_down_sample_double_annually", MillisecondValueConstant.MILLISECOND_VALUE_OF_A_MONTH),
    /**
     * 3年表-单位是30天
     */
    TRIPLE_ANNUALLY("spark_down_sample_triple_annually", MillisecondValueConstant.MILLISECOND_VALUE_OF_A_MONTH);


    TableNameEnum(String value, Long timeUnit) {
        this.value = value;
        this.timeUnit = timeUnit;
    }

    public static TableNameEnum getByValue(String value) {
        if (Objects.isNull(value)) {
            return null;
        }

        TableNameEnum[] instances = TableNameEnum.values();
        for (TableNameEnum i : instances) {
            if (i.getValue().equals(value)) {
                return i;
            }
        }

        return null;
    }

    public String getValue() {
        return value;
    }

    public Long getTimeUnit() {
        return timeUnit;
    }

    private String value;

    private Long timeUnit;
}
