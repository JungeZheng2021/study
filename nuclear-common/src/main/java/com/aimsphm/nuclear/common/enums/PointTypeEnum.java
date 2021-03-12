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
public enum PointTypeEnum {
    //测点类型：1-温度、2-压力、3-流量、4-液位、5-振动、6-位移、7-电信号、8-声学、9-油质、10-状态类
    PI(1, "PI测点"),
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
        if (value == null) {
            return null;
        }

        PointTypeEnum[] instances = PointTypeEnum.values();
        for (PointTypeEnum i : instances) {
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
