package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <配置状态枚举>
 * @Author: MILLA
 * @CreateDate: 2020/12/17 13:47
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/17 13:47
 * @UpdateRemark: <>
 * @Version: 1.0
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
        if (value == null) {
            return null;
        }

        ConfigStatusEnum[] instances = ConfigStatusEnum.values();
        for (ConfigStatusEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
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
