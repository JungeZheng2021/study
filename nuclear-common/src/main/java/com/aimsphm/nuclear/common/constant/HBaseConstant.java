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
public class HBaseConstant {
    public static final String TABLE_MODEL_ESTIMATE_RESULT = "model_estimate_result";
    public static final String FAMILY_MODEL_ESTIMATE_RESULT = "m1";
    /**
     * rowKey分隔符号
     */
    public static final String ROW_KEY_SEPARATOR = "_";
    /**
     * 实时表格名称 npc-核电站简写
     */
    public static final String H_BASE_TABLE_NPC_REAL_TIME = "npc_real_time";
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
    public static final String H_BASE_FAMILY_NPC_SENSOR_RMS = "sRms";
}
