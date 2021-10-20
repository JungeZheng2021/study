package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:报警原因枚举
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum AlarmReasonEnum {

    PENDINGA("1", "工况扰动"), ANALYSISING("2", "设备异常"),
    PENDINGH("3", "传感器异常"), HANDLING("4", "潜在故障"),
    HANDLED("5", "虚假事件"), OTHER("-1", "其他");

    AlarmReasonEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public static AlarmReasonEnum getByValue(String value) {
        if (Objects.isNull(value)) {
            return null;
        }

        AlarmReasonEnum[] instances = AlarmReasonEnum.values();
        for (AlarmReasonEnum i : instances) {
            if (i.getValue().equals(value)) {
                return i;
            }
        }

        return AlarmReasonEnum.OTHER;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    private String value;
    private String desc;
}
