package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <设备类型枚举>
 * @Author: MILLA
 * @CreateDate: 2020/12/17 13:47
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/17 13:47
 * @UpdateRemark: <>
 * @Version: 1.0
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
        if (value == null) {
            return null;
        }

        DeviceTypeEnum[] instances = DeviceTypeEnum.values();
        for (DeviceTypeEnum i : instances) {
            if (value != null && value.equals(i.getType())) {
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
