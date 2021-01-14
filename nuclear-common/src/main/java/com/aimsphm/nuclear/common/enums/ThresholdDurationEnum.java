package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <阈值报警枚举类>
 * @Author: MILLA
 * @CreateDate: 2020/4/17 14:30
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/17 14:30
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum ThresholdDurationEnum {

    LESS_ONE_MINUTE(1, 0L, 60L),
    ONE_MINUTE_2_HOUR(2, 60L, 3600L),
    ONE_HOUR_2_DAY(3, 3600L, 86400L),
    ONE_DAY_2_WEEK(4, 86400L, 7 * 86400L),
    ONE_WEEK_2_MONTH(5, 7 * 86400L, 23 * 86400L),
    GREATER_ONE_MONTH(6, 30 * 86400L, null),
    OTHERS(-1, -1L, -1L);


    ThresholdDurationEnum(Integer value, Long start, Long end) {
        this.value = value;
        this.start = start;
        this.end = end;
    }

    public static ThresholdDurationEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }

        ThresholdDurationEnum[] instances = ThresholdDurationEnum.values();
        for (ThresholdDurationEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
                return i;
            }
        }

        return null;
    }

    public Integer getValue() {
        return value;
    }

    public Long getStart() {
        return start;
    }

    public Long getEnd() {
        return end;
    }

    private Integer value;

    private Long start;

    private Long end;
}
