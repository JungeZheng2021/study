package com.aimsphm.nuclear.report.enums;

/**
 * <p>
 * 功能描述:测点配置类型
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/07/20 13:44
 */
public enum ReportCategoryEnum {
    /**
     * 折线图
     */
    LINE(1)
    /**
     *多Y轴折线图
     */
    , LINE_MULTI_Y(11)
    /**
     * 波形数据-折线图(旋机)
     */
    , LINE_WAVEFORM(12)
    /**
     *频谱数据-折线图(旋机)
     */
    , LINE_SPECTRUM(13)
    /**
     * 非Pi测点的趋势图
     */
    , LINE_NONE_PI(14)
    /**
     * 有报警事件的测点需要画趋势图
     */
    , LINE_DYNAMIC_THRESHOLD(15)
    /**
     * 柱状图
     */
    , BAR(2)
    /**
     * 饼状图
     */
    , PIE(3)
    /**
     * 旋机最近有效值
     */
    , NEWEST_RMS(98)
    /**
     * 汽机最大值
     */
    ,
    MAX_INFO(99);

    ReportCategoryEnum(int category) {
        this.category = category;
    }

    /**
     * 类型
     */
    private Integer category;

    public Integer getCategory() {
        return category;
    }
}
