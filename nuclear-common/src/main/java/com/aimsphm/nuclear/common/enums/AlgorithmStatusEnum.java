package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:算法报警类型枚举
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum AlgorithmStatusEnum {

    PENDINGA(1, "待分析"), ANALYSISING(2, "分析中"),
    PENDINGH(3, "待处理"), HANDLING(4, "处理中"),
    HANDLED(5, "已处理"), OTHER(-1, "其他");

    AlgorithmStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public static AlgorithmStatusEnum getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        AlgorithmStatusEnum[] instances = AlgorithmStatusEnum.values();
        for (AlgorithmStatusEnum i : instances) {
            if (i.getValue().equals(value)) {
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
