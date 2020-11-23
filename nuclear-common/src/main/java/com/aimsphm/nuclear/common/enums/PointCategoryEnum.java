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
public enum PointCategoryEnum {

    //-1其他相关[业务中使用] 0.流量 1.温度 2.转速和电信号 3.振动 4.振动特征-1X 5.振动特征-2X 6.额外公共量 7.报警测点
    SOMETHING_ELSE((byte) -1, "其他相关"),
    FLOW((byte) 0, "流量相关"),
    TEMPERATURE((byte) 1, "温度相关"),
    SPEED_OR_ELECTRIC((byte) 2, "转速与电信号"),
    VIBRATION((byte) 3, "振动相关"),
    VIBRATION_F1X((byte) 4, "振动一倍相关"),
    VIBRATION_F2X((byte) 5, "振动二倍相关"),
    OTHER_PUBLIC((byte) 6, "额外公共量"),
    ALARM((byte) 7, "报警相关");

    PointCategoryEnum(Byte value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getValue(String desc) {
        if (desc == null) {
            return null;
        }

        PointCategoryEnum[] instances = PointCategoryEnum.values();
        for (PointCategoryEnum i : instances) {
            if (desc != null && desc.equals(i.getDesc())) {
                if (i == PointCategoryEnum.SOMETHING_ELSE) {
                    return "8";
                }
                return i.getValue().toString();
            }
        }
        return null;
    }

    public static String getDesc(byte value) {
        PointCategoryEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return PointCategoryEnum.SOMETHING_ELSE.getDesc();
        }
        return typeEnum.getDesc();
    }

    public static PointCategoryEnum getByValue(Byte value) {
        if (value == null) {
            return null;
        }

        PointCategoryEnum[] instances = PointCategoryEnum.values();
        for (PointCategoryEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
                return i;
            }
        }

        return null;
    }

    public Byte getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    private Byte value;

    private String desc;
}
