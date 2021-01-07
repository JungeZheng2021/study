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
        if (value == null) {
            return null;
        }

        ThresholdStatusEnum[] instances = ThresholdStatusEnum.values();
        for (ThresholdStatusEnum i : instances) {
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
