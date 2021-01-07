package com.aimsphm.nuclear.common.constant;

/**
 * @Package: com.aimsphm.nuclear.common.constant
 * @Description: <hbase常量类>
 * @Author: MILLA
 * @CreateDate: 2020/3/5 13:16
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/5 13:16
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class HBaseConstant {

    /**
     * rowKey分隔符号
     */
    public static final String ROW_KEY_SEPARATOR = "_";

    /**
     * -----------------------------npc_phm_data表下所有的family-------------------------------------
     * 实时表格名称 npc-核电站简写
     */
    public static final String H_BASE_TABLE_NPC_PHM_DATA = "npc_phm_data";
    /**
     * 列族-pi数据-列族名称
     */
    public static final String H_BASE_FAMILY_NPC_PI_REAL_TIME = "pRaw";
    /**
     * 列族-振动原始数据
     */
    public static final String H_BASE_FAMILY_NPC_VIBRATION_RAW = "vRaw";
    /**
     * 列族-振动计算得到数据
     */
    public static final String H_BASE_FAMILY_NPC_VIBRATION_CALCULATE = "vCalc";
    /**
     * 列族-传感器Rms值[秒级]
     */
    public static final String H_BASE_FAMILY_NPC_SENSOR_RMS = "vec-Rms";

    /**
     * 列族-估计值-实测值-残差
     */
    public static final String H_BASE_FAMILY_NPC_ESTIMATE = "estimate";

    private HBaseConstant() {
    }
}
