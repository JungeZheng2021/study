package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

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
    HIGH_HIGH_ALARM("高高报", 3, 6),
    HIGH_ALARM("高报", 2, 5),
    HIGH_EARLY_ALARM("高预警", 1, 4),
    LOW_EARLY_ALARM("低预警", 1, 3),
    LOW_ALARM("低报", 2, 2),
    LOW_LOW_ALARM("低低报", 3, 1),
    ALARM_TEXT("报警测点", 3, 0),
    OTHERS("其他", -1, -1);

    AlarmMessageEnum(String text, Integer color, Integer level) {
        this.text = text;
        this.color = color;
        this.level = level;
    }

    public static String getDescByLevel(Integer level) {
        AlarmMessageEnum typeEnum = getByValue(level);
        if (Objects.isNull(typeEnum)) {
            return AlarmMessageEnum.OTHERS.getText();
        }
        return typeEnum.getText();
    }

    public static AlarmMessageEnum getByValue(Integer level) {
        if (level == null) {
            return null;
        }

        AlarmMessageEnum[] instances = AlarmMessageEnum.values();
        for (AlarmMessageEnum i : instances) {
            if (level != null && level.equals(i.getLevel())) {
                return i;
            }
        }

        return null;
    }

    public String getText() {
        return text.concat(":");
    }

    public byte getColor() {
        return (byte) (int) color;
    }

    public Integer getLevel() {
        return level;
    }

    private String text;

    private Integer color;

    private Integer level;

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
