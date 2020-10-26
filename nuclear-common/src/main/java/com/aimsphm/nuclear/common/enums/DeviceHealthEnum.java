package com.aimsphm.nuclear.common.enums;

public enum DeviceHealthEnum {
    Health(0, "健康", "green"),
    Pending(1, "待观察", "lightgreen"),
    Warning(2, "预警", "orange"),
    Alarm(3, "报警", "red"),
    Stop(4, "停机", "gray");

    private DeviceHealthEnum(Integer value, String desc, String color) {
        this.value = value;
        this.desc = desc;
        this.color = color;
    }

    public static DeviceHealthEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }

        DeviceHealthEnum[] instances = DeviceHealthEnum.values();
        for (DeviceHealthEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
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
