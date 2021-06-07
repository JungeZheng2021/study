package com.aimsphm.nuclear.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <单位枚举 //单位  0 天 1 小时 2 分钟 3 秒 4 毫秒 5 次>
 * @Author: MILLA
 * @CreateDate: 2020/12/17 13:47
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/17 13:47
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum TimeUnitEnum {
    OTHER("other", -1L),
    DAY("d", 24 * 3600 * 1000L),
    HOUR("h", 3600 * 1000L),
    MINUTE("m", 60 * 1000L),
    SECOND("s", 1000L),
    MILLION_SECOND("ms", 1L);

    TimeUnitEnum(String name, Long value) {
        this.name = name;
        this.value = value;
    }

    public static Long getValue(String value) {
        TimeUnitEnum typeEnum = getByName(value);
        if (Objects.isNull(typeEnum)) {
            return TimeUnitEnum.OTHER.getValue();
        }
        return typeEnum.getValue();
    }

    public static Long getGapValue(String gapTime) {
        String name = null;
        if (StringUtils.isBlank(gapTime)) {
            name = gapTime.substring(gapTime.length() - 1);
        }
        TimeUnitEnum typeEnum = getByName(name);
        if (Objects.isNull(typeEnum)) {
            return null;
        }
        return typeEnum.getValue();
    }

    public static TimeUnitEnum getByName(String name) {
        if (name == null) {
            return null;
        }

        TimeUnitEnum[] instances = TimeUnitEnum.values();
        for (TimeUnitEnum i : instances) {
            if (name.equalsIgnoreCase(i.getName())) {
                return i;
            }
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    private String name;

    private Long value;

}
