package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:测点类型枚举类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum EventStatusEnum {

    FINISHED(0, "已结束"),
    IN_ACTIVITY(1, "待分析"),
    ACKNOWLEDGED(2, "分析中"),

    IGNORED(3, "已忽略"),
    PAUSED(4, "已暂停"),
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
        if (Objects.isNull(value)) {
            return null;
        }

        EventStatusEnum[] instances = EventStatusEnum.values();
        for (EventStatusEnum i : instances) {
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

    private Integer value;

    private String desc;
}
