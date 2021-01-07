package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

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
public enum AlarmEvaluationEnum {

    HIGH("02", "高报"),
    HIGHER("03", "高高报"),
    LOW("22", "低报"),
    LOWER("23", "低低报"),
    OTHERS("-1", "其他");

    AlarmEvaluationEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public static String getDesc(String value) {
        AlarmEvaluationEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return AlarmEvaluationEnum.OTHERS.getDesc();
        }
        return typeEnum.getDesc();
    }

    public static AlarmEvaluationEnum getByValue(String value) {
        if (value == null) {
            return null;
        }

        AlarmEvaluationEnum[] instances = AlarmEvaluationEnum.values();
        for (AlarmEvaluationEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
                return i;
            }
        }
        return null;
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
