package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:阈值状态
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum ThresholdStatusEnum {

    IN_ACTIVITY(1, "未标记"),
    ACKNOWLEDGED(2, "已确认"),
    IGNORED(3, "已忽略"),
    OTHERS(-1, "其他");


    ThresholdStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        ThresholdStatusEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return ThresholdStatusEnum.OTHERS.getDesc();
        }
        return typeEnum.getDesc();
    }

    public static ThresholdStatusEnum getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        ThresholdStatusEnum[] instances = ThresholdStatusEnum.values();
        for (ThresholdStatusEnum i : instances) {
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
