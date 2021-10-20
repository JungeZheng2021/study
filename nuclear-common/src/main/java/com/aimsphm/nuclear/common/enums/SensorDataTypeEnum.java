package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum SensorDataTypeEnum {
    PI(1),
    WIRED(3),
    WIFI(2);


    SensorDataTypeEnum(Integer value) {
        this.value = value;

    }

    public static SensorDataTypeEnum getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        SensorDataTypeEnum[] instances = SensorDataTypeEnum.values();
        for (SensorDataTypeEnum i : instances) {
            if (i.getValue().equals(value)) {
                return i;
            }
        }

        return null;
    }

    public Integer getValue() {
        return value;
    }


    private Integer value;
}
