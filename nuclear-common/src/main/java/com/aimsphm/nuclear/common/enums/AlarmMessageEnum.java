package com.aimsphm.nuclear.common.enums;

import java.util.Objects;


/**
 * <p>
 * 功能描述:报警类型枚举
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
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
        if (Objects.isNull(level)) {
            return null;
        }

        AlarmMessageEnum[] instances = AlarmMessageEnum.values();
        for (AlarmMessageEnum i : instances) {
            if (i.getLevel().equals(level)) {
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
        if (Objects.isNull(text)) {
            return null;
        }
        AlarmMessageEnum[] instances = AlarmMessageEnum.values();
        for (AlarmMessageEnum i : instances) {
            if (i.getText().equals(text)) {
                return i;
            }
        }
        return null;
    }
}
