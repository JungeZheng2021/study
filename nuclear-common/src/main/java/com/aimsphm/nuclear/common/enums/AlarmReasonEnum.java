package com.aimsphm.nuclear.common.enums;

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
public enum AlarmReasonEnum {

//    alarmCategoryList: [
//    { key: 1, value: 1, label: '工况扰动' },
//    { key: 2, value: 2, label: '设备异常' },
//    { key: 3, value: 3, label: '传感器异常' },
//    { key: 4, value: 4, label: '潜在故障' },
//    { key: 5, value: 5, label: '虚假事件' },
//    { key: 6, value: -1, label: '其他' }
//    ],
    PENDINGA("1", "工况扰动"), ANALYSISING("2", "设备异常"),
    PENDINGH("3", "传感器异常"), HANDLING("4", "潜在故障"),
    HANDLED("5", "虚假事件"), OTHER("-1", "其他");

    AlarmReasonEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public static AlarmReasonEnum getByValue(String value) {
        if (value == null) {
            return null;
        }

        AlarmReasonEnum[] instances = AlarmReasonEnum.values();
        for (AlarmReasonEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
                return i;
            }
        }

        return AlarmReasonEnum.OTHER;
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
