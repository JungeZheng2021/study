package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:算法报警级别枚举
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
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
        if (Objects.isNull(value)) {
            return null;
        }

        AlgorithmLevelEnum[] instances = AlgorithmLevelEnum.values();
        for (AlgorithmLevelEnum i : instances) {
            if (i.getValue().equals(value)) {
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
