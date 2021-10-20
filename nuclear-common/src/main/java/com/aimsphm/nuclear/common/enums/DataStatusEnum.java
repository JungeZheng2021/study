package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:数据状态枚举
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum DataStatusEnum {
    OTHER(-1, "其他"),

    RUNNING(1, "生成中"),

    SUCCESS(2, "生成成功"),

    FAILED(3, "生成失败");


    DataStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        DataStatusEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return DataStatusEnum.OTHER.getDesc();
        }
        return typeEnum.getDesc();
    }

    public static DataStatusEnum getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }
        DataStatusEnum[] instances = DataStatusEnum.values();
        for (DataStatusEnum i : instances) {
            if (i.getValue().equals(value)) {
                return i;
            }
        }
        return null;
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
