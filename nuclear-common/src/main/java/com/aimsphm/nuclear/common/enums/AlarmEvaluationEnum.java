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
public enum AlarmEvaluationEnum {

    //    alarmCategoryList: [
//    { key: 1, value: 1, label: '工况扰动' },
//    { key: 2, value: 2, label: '设备异常' },
//    { key: 3, value: 3, label: '传感器异常' },
//    { key: 4, value: 4, label: '潜在故障' },
//    { key: 5, value: 5, label: '虚假事件' },
//    { key: 6, value: -1, label: '其他' }
//    ],
    HI_HI("hi_hi", "高高报"), HI("hi", "高报"),
    HI_PRE("hi_pre", "高预警"), GROUTH("grouth", "递增"),
    LOW_LOW("low_low", "低低报"), LOW("low", "低报"),
    DESC("desc", "递减"), WAVE("wave", "波动"),
    FLASH("flash", "闪发"), STEP("step", "阶跃"),
    LONG_HI("long_hi", "长高报"), SUD_HI_HI("sud_hi_hi", "突变高高报"),
    SUD_HI("sud_hi", "突变高报"), LONG_LOW("long_low", "长低报"), SUD_LO_LO("sud_lo_lo", "突变低低报"), SUD_LO("sud_lo", "突变低报"), NOTHING("nothing", "其他");

    AlarmEvaluationEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public static AlarmEvaluationEnum getByValue(String value) {
        if (value == null) {
            return null;
        }

        AlarmEvaluationEnum[] instances = AlarmEvaluationEnum.values();
        for (AlarmEvaluationEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
                return i;
            }
        }

        return AlarmEvaluationEnum.NOTHING;
    }

    public static AlarmEvaluationEnum getByDesc(String desc) {
        if (desc == null) {
            return null;
        }

        AlarmEvaluationEnum[] instances = AlarmEvaluationEnum.values();
        for (AlarmEvaluationEnum i : instances) {
            if (desc != null && desc.equals(i.getDesc())) {
                return i;
            }
        }

        return AlarmEvaluationEnum.NOTHING;
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
