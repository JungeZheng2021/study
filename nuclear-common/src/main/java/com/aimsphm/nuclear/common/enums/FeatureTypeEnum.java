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
public enum FeatureTypeEnum {

    OTHER(-1, "其他"),
    TYPE_0(0, "自定义"),
    TYPE_1(1, "上升"),
    TYPE_2(2, "下降"),
    TYPE_3(3, "阶跃陡升"),
    TYPE_4(4, "阶跃陡降"),
    TYPE_5(5, "超阈值上限"),
    TYPE_6(6, "超阈值下限"),
    TYPE_7(7, "存在差异"),
    TYPE_8(8, "触发"),
    TYPE_9(9, "数据异常"),
    TYPE_10(10, "测量值为零"),
    TYPE_11(11, "超阈值"),
    TYPE_12(12, "波动"),
    TYPE_13(13, "阶跃突变"),
    TYPE_14(14, "出现"),
    TYPE_15(15, "闪发");

    FeatureTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public static FeatureTypeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        FeatureTypeEnum[] instances = FeatureTypeEnum.values();
        for (FeatureTypeEnum i : instances) {
            if (i.getValue().equals(value)) {
                return i;
            }
        }
        return FeatureTypeEnum.OTHER;
    }

    public static String getDescByValue(Integer value) {
        FeatureTypeEnum byValue = getByValue(value);
        if (Objects.nonNull(byValue)) {
            return byValue.getDesc();
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
