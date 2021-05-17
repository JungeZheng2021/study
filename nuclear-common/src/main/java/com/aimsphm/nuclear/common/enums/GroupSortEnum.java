package com.aimsphm.nuclear.common.enums;

import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.enums
 * @Description: <测点分组排序枚举类>
 * @Author: MILLA
 * @CreateDate: 2020/4/17 14:30
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/17 14:30
 * @UpdateRemark: <>
 * @Version: 1.0
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
        if (sort == null) {
            return null;
        }

        GroupSortEnum[] instances = GroupSortEnum.values();
        for (GroupSortEnum i : instances) {
            if (sort != null && sort.equals(i.getSort())) {
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
            if (name != null && name.equals(i.getName())) {
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
