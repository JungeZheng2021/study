package com.aimsphm.nuclear.common.enums;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <算法报警类型枚举>
 * @Author: LUyi
 * @CreateDate: 2020/4/17 14:30
 * @UpdateUser: LUyi
 * @UpdateDate: 2020/4/17 14:30
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum AlarmTypeEnum {

    //    {key: 1, value: 1, label: 'threshold'},
//
////{key: 2, value: 2, label: 'fluctuation'},
//
////{key: 3, value: 3, label: 'peak'},
//
////{key: 4, value: 4, label: 'step'},
//
////{key: 5, value: 5, label: 'algorithm'},
//
//    //{key: 6, value: - 1, label: 'other'}
    THRESHOLD(1, "阈值"), FLUCTUATION(2, "波动"),
    PEAK(3, "尖峰"), STEP(4, "阶跃"),
    ALGORITHM(5, "算法"), OTHER(-1, "其他");

    AlarmTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public static AlarmTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }

        AlarmTypeEnum[] instances = AlarmTypeEnum.values();
        for (AlarmTypeEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
                return i;
            }
        }

        return AlarmTypeEnum.OTHER;
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
