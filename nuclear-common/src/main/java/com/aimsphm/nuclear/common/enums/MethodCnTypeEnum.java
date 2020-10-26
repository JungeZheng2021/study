package com.aimsphm.nuclear.common.enums;

public enum MethodCnTypeEnum {
    add(0,"新增"),
    update(1,"修改"),
    delete(2,"删除"),
    others(3,"其它");
    private MethodCnTypeEnum(Integer value, String name) {
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
        if (value == null) {
            return null;
        }

        MethodCnTypeEnum[] instances = MethodCnTypeEnum.values();
        for (MethodCnTypeEnum i: instances) {
            if (value != null && value.equals(i.getValue())) {
                return i.getName();
            }
        }

        return null;
    }
}
