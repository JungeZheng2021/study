package com.aimsphm.nuclear.common.enums;

public enum DeviceOperationStatusEnum {
    Good(1, "正常运行", "green"),
    Stop(2, "停机", "red"),
    StartUpProgress(3, "启动过程", "lightgreen"),
    UnKnown(9, "无法判断", "orange");


    private DeviceOperationStatusEnum(Integer value, String desc, String color) {
        this.value = value;
        this.desc = desc;
        this.color = color;
    }

    public static DeviceOperationStatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }

        DeviceOperationStatusEnum[] instances = DeviceOperationStatusEnum.values();
        for (DeviceOperationStatusEnum i : instances) {
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
