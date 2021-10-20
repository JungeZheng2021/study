package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * <p>
 * 功能描述:测点分组排序枚举类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/17 14:30
 */
public enum GroupSortEnum {
    TEMPERATURE("轴承温度", 1),
    VIBRATION("轴承振动", 2),
    ACOUSTICS("声学相关", 3),
    OIL_QUALITY("油液相关", 4),
    ELECTRIC_MACHINE("电机绕组", 5),

    OTHERS("其他", -1);

    GroupSortEnum(String name, Integer sort) {
        this.name = name;
        this.sort = sort;
    }

    public static String getName(Integer value) {
        GroupSortEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return GroupSortEnum.OTHERS.getName();
        }
        return typeEnum.getName();
    }

    public static GroupSortEnum getByValue(Integer sort) {
        if (Objects.isNull(sort)) {
            return null;
        }

        GroupSortEnum[] instances = GroupSortEnum.values();
        for (GroupSortEnum i : instances) {
            if (i.getSort().equals(sort)) {
                return i;
            }
        }

        return null;
    }

    public static Integer getSorted(String name) {
        if (Objects.isNull(name)) {
            return OTHERS.getSort();
        }

        GroupSortEnum[] instances = GroupSortEnum.values();
        for (GroupSortEnum i : instances) {
            if (i.getName().equals(name)) {
                return i.getSort();
            }
        }

        return OTHERS.getSort();
    }

    public Integer getSort() {
        return sort;
    }

    public String getName() {
        return name;
    }

    private String name;

    private Integer sort;

}
