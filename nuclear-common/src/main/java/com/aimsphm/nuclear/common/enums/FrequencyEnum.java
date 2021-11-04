package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:降采样的取点数量
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum FrequencyEnum {

    HOURLY("hourly", 417, 10, "每小时", null),
    DAILY_60("daily", 1428, 60, "每天", TableNameEnum.WEEKLY.getValue()),
    DAILY_130("daily", 666, 130, "每天", TableNameEnum.HALF_MONTHLY.getValue()),
    DAILY_260("daily", 332, 260, "每天", TableNameEnum.MONTHLY.getValue()),
    WEEKLY_780("weekly", 777, 780, "每周", TableNameEnum.QUARTERLY.getValue()),
    WEEKLY_1560("weekly", 417, 1560, "每周", TableNameEnum.HALF_ANNUALLY.getValue()),
    MONTHLY_3155("monthly", 838, 3155, "每月", TableNameEnum.ANNUALLY.getValue()),
    MONTHLY_6310("monthly", 410, 6310, "每月", TableNameEnum.DOUBLE_ANNUALLY.getValue()),
    MONTHLY_9461("monthly", 273, 9461, "每月", TableNameEnum.TRIPLE_ANNUALLY.getValue()),
    OTHERS("-1", -1, -1, "其他", "");

    FrequencyEnum(String frequency, Integer targetNumber, Integer rate, String desc, String tableName) {
        this.frequency = frequency;
        this.targetNumber = targetNumber;
        this.rate = rate;
        this.desc = desc;
        this.tableName = tableName;
    }

    public static String getDesc(Integer rate) {
        FrequencyEnum typeEnum = getByRate(rate);
        if (Objects.isNull(typeEnum)) {
            return FrequencyEnum.OTHERS.getDesc();
        }
        return typeEnum.getDesc();
    }

    public static FrequencyEnum getByRate(Integer rate) {
        if (Objects.isNull(rate)) {
            return null;
        }

        FrequencyEnum[] instances = FrequencyEnum.values();
        for (FrequencyEnum i : instances) {
            if (i.getRate().equals(rate)) {
                return i;
            }
        }
        return null;
    }

    public static FrequencyEnum getByTableName(String tableName) {
        if (Objects.isNull(tableName) || TableNameEnum.DAILY.getValue().equals(tableName)) {
            return HOURLY;
        }

        FrequencyEnum[] instances = FrequencyEnum.values();
        for (FrequencyEnum i : instances) {
            if (Objects.nonNull(i.getTableName()) && i.getTableName().equals(tableName)) {
                return i;
            }
        }
        return null;
    }

    public String getFrequency() {
        return frequency;
    }

    public Integer getTargetNumber() {
        return targetNumber;
    }

    public Integer getRate() {
        return rate;
    }

    public String getDesc() {
        return desc;
    }

    public String getTableName() {
        return tableName;
    }

    private String tableName;
    private String frequency;
    private Integer targetNumber;
    private Integer rate;
    private String desc;
}
