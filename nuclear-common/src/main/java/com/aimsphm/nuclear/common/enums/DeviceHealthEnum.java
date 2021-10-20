package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:设备状态枚举
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum DeviceHealthEnum {
    HEALTH(0, "健康", "green"),
    PENDING(1, "待观察", "lightgreen"),
    WARNING(2, "预警", "orange"),
    ALARM(3, "报警", "red"),
    STOP(4, "停机", "gray");

    DeviceHealthEnum(Integer value, String desc, String color) {
        this.value = value;
        this.desc = desc;
        this.color = color;
    }

    public static DeviceHealthEnum getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }
        DeviceHealthEnum[] instances = DeviceHealthEnum.values();
        for (DeviceHealthEnum i : instances) {
            if (i.getValue().equals(value)) {
                return i;
            }
        }
        return null;
    }

    public static String getDesc(Integer value) {
        DeviceHealthEnum healthEnum = getByValue(value);
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

    public String getColor() {
        return color;
    }

    private Integer value;
    /**
     * 描述
     */
    private String desc;
    /**
     * 颜色
     */
    private String color;
}
