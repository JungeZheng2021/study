package com.aimsphm.nuclear.report.enums;

import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <图谱类型枚举类>
 * @Author: MILLA
 * @CreateDate: 2020/4/17 14:30
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/17 14:30
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum FigureTypeEnum {
    ENVELOPE_SPECTRUM("envelopeSpectrum", "包络谱"),
    OTHER("others", "其他");

    FigureTypeEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public static FigureTypeEnum getByName(String name) {
        if (Objects.isNull(name) || name.length() == 0) {
            return null;
        }

        FigureTypeEnum[] instances = FigureTypeEnum.values();
        for (FigureTypeEnum i : instances) {
            if (name.equals(i.getName())) {
                return i;
            }
        }

        return FigureTypeEnum.OTHER;
    }

    public static String getDesc(String name) {
        FigureTypeEnum healthEnum = getByName(name);
        if (Objects.isNull(healthEnum)) {
            return null;
        }
        return healthEnum.desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    private String name;
    private String desc;
}
