package com.aimsphm.nuclear.core.enums;

/**
 * @Package: com.aimsphm.nuclear.core.enums
 * @Description: <系统初始设置运行状态单位>
 * @Author: Mao
 * @CreateDate: 2020/4/17 14:30
 * @UpdateUser: Mao
 * @UpdateDate: 2020/5/13 13:30
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum RuntimeBaseUnitEnum {


    HOUR(1, "小时"), MINUTE(2, "分钟"),
    SECOND(3, "秒"), TIME(4, "次数"),UNDEFINED(-1, "未定义");

    RuntimeBaseUnitEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;

    }

    public static RuntimeBaseUnitEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }

        RuntimeBaseUnitEnum[] instances = RuntimeBaseUnitEnum.values();

        for (RuntimeBaseUnitEnum i : instances) {
            if (value != null && value.equals(i.getValue())) {
                return i;
            }
        }
       return RuntimeBaseUnitEnum.UNDEFINED;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    private Integer value;
    private String desc;
}
