package com.aimsphm.nuclear.report.constant;

/**
 * <p>
 * 功能描述:占位符常量类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/6/12 17:05
 */
public class PlaceholderConstant {
    private PlaceholderConstant() {
    }

    /**
     * 设备编码
     */
    public static final String DEVICE_CODE = "#deviceCode#";
    /**
     * 报告创建时间
     */
    public static final String RANGE_DATE = "#rangeDate#";
    /**
     * 报告名称
     */
    public static final String REPORT_NAME = "#reportName#";

    /**
     * 运行状态
     */
    public static final String RUNNING_STATUS = "#status#";
    /**
     * 运行启动时间
     */
    public static final String RUNNING_START_TIME = "#startTime#";
    /**
     * 运行持续时间
     */
    public static final String RUNNING_CONTINUE_TIME = "#continueTime#";
    /**
     * 运行总时间
     */
    public static final String RUNNING_TOTAL_TIME = "#totalTime#";
    /**
     * 运行启停次数
     */
    public static final String RUNNING_STOP_TIMES = "#stopTimes#";

    /**
     * 报警列表
     */
    public static final String TABLE_ALARM_EVENT = "#tableAlarmEvent#";
    /**
     * 报警列表
     */
    public static final String TABLE_ALARM_THRESHOLD = "#tableAlarmThreshold#";
    /**
     * 动态阈值报警事件图
     */
    public static final String PARAGRAPH_GRAPH_DATA_ITEMS = "#paragraphAlarmEventList#";
    /**
     * 融合诊断结果
     */
    public static final String PARAGRAPH_DIAGNOSIS_RESULTS = "#paragraphDiagnosisResults#";
    /**
     * 设备运行明细
     */
    public static final String DEVICE_RUNNING_DETAILS = "#deviceRunningDetails#";

}
