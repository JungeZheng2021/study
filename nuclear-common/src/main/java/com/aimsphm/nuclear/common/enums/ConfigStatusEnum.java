package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:配置状态枚举
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum ConfigStatusEnum {
    OTHER(-1, "其他"),
    IN_CONFIG(1, "配置中"),
    CONFIG_SUCCESS(2, "配置成功"),
    CONFIG_FAILED(3, "配置失败");


    ConfigStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDesc(Integer value) {
        ConfigStatusEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return ConfigStatusEnum.OTHER.getDesc();
        }
        return typeEnum.getDesc();
    }

    public static ConfigStatusEnum getByValue(Integer value) {
        if (Objects.isNull(value)) {
            return null;
        }

        ConfigStatusEnum[] instances = ConfigStatusEnum.values();
        for (ConfigStatusEnum i : instances) {
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
