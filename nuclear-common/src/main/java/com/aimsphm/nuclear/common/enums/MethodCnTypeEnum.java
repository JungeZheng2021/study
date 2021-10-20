package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:方法类型枚举类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum MethodCnTypeEnum {
    add(0, "新增"),
    update(1, "修改"),
    delete(2, "删除"),
    others(3, "其它");

    MethodCnTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    private Integer value;

    public String getName() {
        return name;
    }

    private String name;

    public static String getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        MethodCnTypeEnum[] instances = MethodCnTypeEnum.values();
        for (MethodCnTypeEnum i : instances) {
            if (i.getValue().equals(value)) {
                return i.getName();
            }
        }

        return null;
    }
}
