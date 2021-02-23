package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <数据状态枚举>
 * @Author: MILLA
 * @CreateDate: 2020/12/17 13:47
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/17 13:47
 * @UpdateRemark: <>
 * @Version: 1.0
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
        if (value == null) {
            return null;
        }

        DataStatusEnum[] instances = DataStatusEnum.values();
        for (DataStatusEnum i : instances) {
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
