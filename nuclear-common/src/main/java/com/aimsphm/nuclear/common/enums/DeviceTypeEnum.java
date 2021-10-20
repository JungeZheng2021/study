package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:设备类型枚举
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum DeviceTypeEnum {

    PUMP(0, "上充泵"),
    FAN(1, "风机"),
    OTHER(-1, "其他");

    DeviceTypeEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getDesc(Integer value) {
        DeviceTypeEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return DeviceTypeEnum.OTHER.getName();
        }
        return typeEnum.getName();
    }

    public static DeviceTypeEnum getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        DeviceTypeEnum[] instances = DeviceTypeEnum.values();
        for (DeviceTypeEnum i : instances) {
            if (i.getType().equals(value)) {
                return i;
            }
        }

        return null;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private Integer type;

    private String name;
}
