package com.aimsphm.nuclear.common.enums;

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
    LEVEL_ONE((byte) 1, "1级报警"),
    LEVEL_TWO((byte) 2, "2级报警"),
    LEVEL_THREE((byte) 3, "3级报警"),
    LEVEL_FOUR((byte) 4, "4级报警"),
    LEVEL_FIVE((byte) 5, "5级报警"),
    LEVEL_SIX((byte) 6, "6级报警");

    AlgorithmLevelEnum(byte value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public static AlgorithmLevelEnum getByValue(Byte value) {
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

    public Byte getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    private Byte value;
    private String desc;
}
