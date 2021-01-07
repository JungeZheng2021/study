package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <阈值报警枚举类>
 * @Author: MILLA
 * @CreateDate: 2020/4/17 14:30
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/17 14:30
 * @UpdateRemark: <>
 * @Version: 1.0
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
        if (value == null) {
            return null;
        }

        ThresholdAlarmStatusEnum[] instances = ThresholdAlarmStatusEnum.values();
        for (ThresholdAlarmStatusEnum i : instances) {
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

    private Integer value;

    private String desc;
}
