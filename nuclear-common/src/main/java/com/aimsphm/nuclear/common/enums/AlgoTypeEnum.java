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
public enum AlgoTypeEnum {
    //    1阈值、2波动、3尖峰、4阶跃、5算法
    THRESHOLD_VALUE((byte) 1, "阈值"),
    FLUCTUATE((byte) 2, "波动"),
    PEAK((byte) 3, "尖峰"),
    PHASE_STEP((byte) 4, "阶跃"),
    ALGORITHM((byte) 5, "算法");

    AlgoTypeEnum(byte value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public static AlgoTypeEnum getByValue(Byte value) {
        if (value == null) {
            return null;
        }

        AlgoTypeEnum[] instances = AlgoTypeEnum.values();
        for (AlgoTypeEnum i : instances) {
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
