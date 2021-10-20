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
public enum PointCategoryEnum {
    //测点类型：1-温度、2-压力、3-流量、4-液位、5-振动、6-位移、7-电信号、8-声学、9-油质、10-状态类
    TEMPERATURE(1, "温度"),
    PRESSURE(2, "压力"),
    FLOW(3, "流量"),
    LIQUID_LOCATION(4, "液位"),
    VIBRATION(5, "振动"),
    DISPLACEMENT(6, "位移"),
    ELECTRIC(7, "电信号"),
    ACOUSTICS(8, "声学"),
    OIL_QUALITY(9, "油质"),
    STATUS(10, "状态类"),
    ALARM(11, "报警"),
    SOMETHING_ELSE(-1, "其他");


    PointCategoryEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        PointCategoryEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return PointCategoryEnum.SOMETHING_ELSE.getDesc();
        }
        return typeEnum.getDesc();
    }

    public static PointCategoryEnum getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        PointCategoryEnum[] instances = PointCategoryEnum.values();
        for (PointCategoryEnum i : instances) {
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
