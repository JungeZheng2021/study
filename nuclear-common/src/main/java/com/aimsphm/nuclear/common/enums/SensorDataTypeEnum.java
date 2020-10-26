package com.aimsphm.nuclear.common.enums;

public enum SensorDataTypeEnum {
    PI(1),
    WIRED(3),
    WIFI(2);


    private SensorDataTypeEnum(Integer value) {
        this.value = value;

    }

    public static SensorDataTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }

        SensorDataTypeEnum[] instances = SensorDataTypeEnum.values();
        for (SensorDataTypeEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
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
