package com.aimsphm.nuclear.core.enums;

/**
 * @Package: com.aimsphm.nuclear.core.enums
 * @Description: <可见测点类型枚举类：3,5,7,11,13,17等质数递增>
 * @Author: MILLA
 * @CreateDate: 2020/11/30 17:29
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/30 17:29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum PointVisibleEnum {
    /**
     * 系统总览/设备监测
     */
    DEVICE_MONITOR(3)
    /**
     * 数据分析
     */
    , DATA_ANALYSIS(5)
    /**
     *历史数据
     */
    , HISTORY_DATA(7);


    private int category;

    PointVisibleEnum(int category) {
        this.category = category;
    }

    public int getCategory() {
        return category;
    }
}
