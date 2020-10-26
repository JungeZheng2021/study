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
public enum AlgorithmStatusEnum {

//    { key: 1, value: 1, label: '待分析' },
//    { key: 2, value: 2, label: '分析中' },
//    { key: 3, value: 3, label: '待处理' },
//    { key: 4, value: 4, label: '处理中' },
//    { key: 5, value: 5, label: '已处理' },
//    { key: 6, value: -1, label: '其他' }
    PENDINGA(1, "待分析"), ANALYSISING(2, "分析中"),
    PENDINGH(3, "待处理"), HANDLING(4, "处理中"),
    HANDLED(5, "已处理"), OTHER(-1, "其他");

    AlgorithmStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public static AlgorithmStatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }

        AlgorithmStatusEnum[] instances = AlgorithmStatusEnum.values();
        for (AlgorithmStatusEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
                return i;
            }
        }

        return AlgorithmStatusEnum.OTHER;
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
