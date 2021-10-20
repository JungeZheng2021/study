package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:设备操作状态枚举
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum DeviceOperationStatusEnum {
    Good(1, "正常运行", "green"),
    Stop(2, "停机", "red"),
    StartUpProgress(3, "启动过程", "lightgreen"),
    UnKnown(9, "无法判断", "orange");


    DeviceOperationStatusEnum(Integer value, String desc, String color) {
        this.value = value;
        this.desc = desc;
        this.color = color;
    }

    public static DeviceOperationStatusEnum getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        DeviceOperationStatusEnum[] instances = DeviceOperationStatusEnum.values();
        for (DeviceOperationStatusEnum i : instances) {
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

    public String getColor() {
        return color;
    }

    private Integer value;
    private String desc;//描述
    private String color;//颜色
}
