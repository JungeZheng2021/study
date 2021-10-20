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
public enum PointTypeEnum {
    //    测点种类 1：网络采集（PI测点） 2：硬件（边缘端）采集 3：算法生成（特征测点）4：指令与反馈 5：平台计算
    PI(1, "PI测点"),
    CALCULATE(5, "平台计算"),
    SOMETHING_ELSE(-1, "其他");


    PointTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        PointTypeEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return PointTypeEnum.SOMETHING_ELSE.getDesc();
        }
        return typeEnum.getDesc();
    }

    public static PointTypeEnum getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        PointTypeEnum[] instances = PointTypeEnum.values();
        for (PointTypeEnum i : instances) {
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
