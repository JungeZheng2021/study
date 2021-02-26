package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <测点Visible枚举类>
 * @Author: MILLA
 * @CreateDate: 2020/4/17 14:30
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/17 14:30
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum PointVisibleEnum {
    DOWN_SAMPLE(2, "降采样"),
    THRESHOLD(3, "阈值"),
    HISTORY_DATA(5, "历史数据"),
    SOMETHING_ELSE(-1, "其他");


    PointVisibleEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        PointVisibleEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return PointVisibleEnum.SOMETHING_ELSE.getDesc();
        }
        return typeEnum.getDesc();
    }

    public static PointVisibleEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }

        PointVisibleEnum[] instances = PointVisibleEnum.values();
        for (PointVisibleEnum i : instances) {
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
