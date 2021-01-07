package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <测点类型枚举类>
 * @Author: MILLA
 * @CreateDate: 2020/4/17 14:30
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/17 14:30
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum EventStatusEnum {

    IN_ACTIVITY(1, "活动中"),
    ACKNOWLEDGED(2, "已确认"),
    IGNORED(3, "已忽略"),
    PAUSED(4, "已暂停"),
    FINISHED(5, "已结束"),
    OTHERS(-1, "其他");


    EventStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        EventStatusEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return EventStatusEnum.OTHERS.getDesc();
        }
        return typeEnum.getDesc();
    }

    public static EventStatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }

        EventStatusEnum[] instances = EventStatusEnum.values();
        for (EventStatusEnum i : instances) {
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
