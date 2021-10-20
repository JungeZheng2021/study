package com.aimsphm.nuclear.common.enums;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * <p>
 * 功能描述:单位枚举 //单位  0 天 1 小时 2 分钟 3 秒 4 毫秒 5 次
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
@Slf4j
public enum TimeUnitEnum {
    OTHER("other", -1L),
    WEEK("w", 7 * 86400 * 1000L),
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

        if (StringUtils.isBlank(gapTime)) {
            return null;
        }
        String name = gapTime.substring(gapTime.length() - 1);
        Long gap;
        try {
            gap = Long.parseLong(gapTime.substring(0, gapTime.length() - 1));
        } catch (NumberFormatException e) {
            log.error("转换异常");
            return null;
        }
        TimeUnitEnum typeEnum = getByName(name);
        if (Objects.isNull(typeEnum)) {
            return null;
        }
        return typeEnum.getValue() * gap;
    }

    public static TimeUnitEnum getByName(String name) {
        if (Objects.isNull(name)) {
            return null;
        }

        TimeUnitEnum[] instances = TimeUnitEnum.values();
        for (TimeUnitEnum i : instances) {
            if (i.getName().equalsIgnoreCase(name)) {
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
