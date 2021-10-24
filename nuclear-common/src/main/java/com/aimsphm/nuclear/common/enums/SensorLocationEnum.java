package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:旋机测点安装位置枚举
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum SensorLocationEnum {
    M1("M1", "电机非驱动端"),
    M2("M2", "电机驱动端"),
    P1("P1", "泵驱动端"),
    P2("P2", "泵非驱动端"),
    G1("G1", "增速机1"),
    G2("G2", "增速机2"),
    G3("G3", "增速机3"),
    G4("G4", "增速机4"),
    OTHER("OTHER", "其他");


    SensorLocationEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static SensorLocationEnum getByValue(String value) {
        if (Objects.isNull(value)) {
            return OTHER;
        }

        SensorLocationEnum[] instances = SensorLocationEnum.values();
        for (SensorLocationEnum i : instances) {
            if (i.getValue().equals(value)) {
                return i;
            }
        }

        return OTHER;
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
