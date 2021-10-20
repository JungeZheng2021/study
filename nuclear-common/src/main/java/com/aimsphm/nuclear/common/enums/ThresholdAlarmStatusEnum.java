package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:阈值报警枚举类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum ThresholdAlarmStatusEnum {

    IN_ACTIVITY(1, "活动中"),
    FINISHED(2, "已结束"),
    OTHERS(-1, "其他");


    ThresholdAlarmStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        ThresholdAlarmStatusEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return ThresholdAlarmStatusEnum.OTHERS.getDesc();
        }
        return typeEnum.getDesc();
    }

    public static ThresholdAlarmStatusEnum getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        ThresholdAlarmStatusEnum[] instances = ThresholdAlarmStatusEnum.values();
        for (ThresholdAlarmStatusEnum i : instances) {
            if (i.getValue().equals(value)) {
                return i;
            }
        }

        return null;
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
