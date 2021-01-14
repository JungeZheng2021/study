package com.aimsphm.nuclear.algorithm.enums;


import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.algorithm.enums
 * @Description: <算法枚举类>
 * @Author: MILLA
 * @CreateDate: 2020/12/17 13:47
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/17 13:47
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public enum AlgorithmTypeEnum {
    MOVING_AVERAGE("MA", "滑动平均值"),

    TREND_FORECAST("TF", "趋势预测"),

    STATE_MONITOR("HCM-PAF", "主送风机预警算法"),

    DATA_ANALYSIS("ANALYSIS", "数据分析"),
    DATA_ANALYSIS_TIME_DOMAIN("TIME-DOMAIN", "时域波形"),
    DATA_ANALYSIS_FRE_DOMAIN("FRE-DOMAIN", "频域波形"),
    DATA_ANALYSIS_ENVELOPE("ENVE-FRE", "包络谱"),
    DATA_ANALYSIS_DWP_TIME("DWP-TIME", "小波包时域波形"),
    DATA_ANALYSIS_DWP_FRE("DWP-FRE", "小波包频域波形"),
    DATA_ANALYSIS_DWP_ENVELOPE("DWP-ENVE-FRE", "小波包包络谱"),

    OTHERS("-1", "其他");


    AlgorithmTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static String getDesc(String value) {
        AlgorithmTypeEnum typeEnum = getByValue(value);
        if (Objects.isNull(typeEnum)) {
            return null;
        }
        return typeEnum.getDesc();
    }

    public static AlgorithmTypeEnum getByValue(String type) {
        if (type == null) {
            return null;
        }

        AlgorithmTypeEnum[] instances = AlgorithmTypeEnum.values();
        for (AlgorithmTypeEnum i : instances) {
            if (type != null && type.equals(i.getType())) {
                return i;
            }
        }

        return null;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    private String type;
    private String desc;
}
