package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <单位枚举 //单位  0 天 1 小时 2 分钟 3 秒 4 毫秒 5 次>
 * @Author: MILLA
 * @CreateDate: 2020/12/17 13:47
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/17 13:47
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum UnitEnum {
    OTHER(-1, "其他"), DAY(0, "天"), HOUR(1, "时"), MINUTE(2, "分"), SECOND(3, "秒"), MILLION_SECOND(4, "毫秒"), TIMES(5, "次");


    UnitEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        UnitEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return UnitEnum.OTHER.getDesc();
        }
        return typeEnum.getDesc();
    }

    public static UnitEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }

        UnitEnum[] instances = UnitEnum.values();
        for (UnitEnum i : instances) {
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
