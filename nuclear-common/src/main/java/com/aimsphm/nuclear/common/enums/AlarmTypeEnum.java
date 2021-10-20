package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:算法报警类型枚举
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum AlarmTypeEnum {
    THRESHOLD(1, "阈值"),
    FLUCTUATION(2, "波动"),
    PEAK(3, "尖峰"),
    STEP(4, "阶跃"),
    ALGORITHM(5, "算法"),
    OTHER(-1, "其他");

    AlarmTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public static AlarmTypeEnum getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        AlarmTypeEnum[] instances = AlarmTypeEnum.values();
        for (AlarmTypeEnum i : instances) {
            if (i.getValue().equals(value)) {
                return i;
            }
        }
        return AlarmTypeEnum.OTHER;
    }

    public static String getDesc(Integer value) {
        AlarmTypeEnum healthEnum = getByValue(value);
        if (Objects.isNull(healthEnum)) {
            return null;
        }
        return healthEnum.desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    private Integer value;
    private String desc;
}
