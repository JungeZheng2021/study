package com.aimsphm.nuclear.common.enums;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <报警类型枚举>
 * @Author: MILLA
 * @CreateDate: 2020/4/15 16:26
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/15 16:26
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum AlarmMessageEnum {
    //3红色-三级阈值报警  2橙色-二级阈值报警 1黄色-一级阈值报警
    HIGH_HIGH_ALARM("高高报", 3), HIGH_ALARM("高报", 2), HIGH_EARLY_ALARM("高预警", 1), LOW_EARLY_ALARM("低预警", 1),
    LOW_ALARM("低报", 2), LOW_LOW_ALARM("低低报", 3), ALARM_TEXT("报警测点", 3);

    AlarmMessageEnum(String text, Integer color) {
        this.text = text;
        this.color = color;
    }

    public String getText() {
        return text.concat(":");
    }

    public byte getColor() {
        return (byte) (int) color;
    }

    private String text;

    private Integer color;

    public static AlarmMessageEnum instance(String text) {
        if (text == null || text.length() == 0) {
            return null;
        }

        AlarmMessageEnum[] instances = AlarmMessageEnum.values();
        for (AlarmMessageEnum i : instances) {
            if (text != null && text.equals(i.getText())) {
                return i;
            }
        }
        return null;
    }
}
