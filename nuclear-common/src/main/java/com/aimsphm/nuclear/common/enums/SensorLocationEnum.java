package com.aimsphm.nuclear.common.enums;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <旋机测点安装位置枚举>
 * @Author: milla
 * @CreateDate: 2020/08/14 15:38
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/14 15:38
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum SensorLocationEnum {
    M1("M1", "电机非驱动端"),
    M2("M2", "电机驱动端"),
    P1("P1", "泵驱动端"),
    P2("P2", "泵非驱动端"),
    G1("G1", "泵非驱动端"),
    G2("G2", "泵非驱动端"),
    G3("G3", "泵非驱动端"),
    G4("G4", "泵非驱动端"),
    OTHER("OTHER", "其他");


    SensorLocationEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static SensorLocationEnum getByValue(String value) {
        if (value == null) {
            return OTHER;
        }

        SensorLocationEnum[] instances = SensorLocationEnum.values();
        for (SensorLocationEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
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
