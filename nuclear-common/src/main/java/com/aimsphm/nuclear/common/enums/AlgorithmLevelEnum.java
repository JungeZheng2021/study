package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <算法报警类型枚举>
 * @Author: MILLA
 * @CreateDate: 2020/4/17 14:30
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/17 14:30
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum AlgorithmLevelEnum {
    LEVEL_ONE(1, "1级报警"),
    LEVEL_TWO(2, "2级报警"),
    LEVEL_THREE(3, "3级报警"),
    LEVEL_FOUR(4, "4级报警"),
    LEVEL_FIVE(5, "5级报警"),
    LEVEL_SIX(6, "6级报警");

    AlgorithmLevelEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public static AlgorithmLevelEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }

        AlgorithmLevelEnum[] instances = AlgorithmLevelEnum.values();
        for (AlgorithmLevelEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
                return i;
            }
        }

        return null;
    }

    public static String getDesc(Integer value) {
        AlgorithmLevelEnum byValue = getByValue(value);
        return Objects.isNull(byValue) ? null : byValue.getDesc();
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
