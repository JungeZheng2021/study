package com.aimsphm.nuclear.core.constant;

import java.text.MessageFormat;

/**
 * @Package: com.aimsphm.nuclear.core.constant
 * @Description: <常量类>
 * @Author: MILLA
 * @CreateDate: 2020/6/28 13:17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/28 13:17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class CoreConstants {
    /**
     * 总览中超限测点个数字符串常量
     */
    public static final String PANORAMA_TRANSFINITE = "超限测点{0}个";
    /**
     * 总览中异常测点个数字符串常量
     */
    public static final String PANORAMA_ANOMALY = "异常测点{0}个";
    /**
     * 设备启动时间 - 最新的启动时间
     */
    public static final String START_TIME = "start_time";

    /**
     * 总运行时间
     */
    public static final String TOTAL_RUNNING_DURATION = "total_running_duration";
    /**
     * 启停次数
     */
    public static final String STOP_TIMES = "stop_times";


    /**
     * 主泵总览key
     */
    public static final String PUMP_PANORAMA = "pump-panorama";
    /**
     * 汽机总览key
     */
    public static final String TURBINE_PANORAMA = "tb-panorama";
    /**
     * 换热器总览key
     */
    public static final String EXCHANGER_PANORAMA = "exchanger-panorama";
    /**
     * 主泵系统监控key
     */
    public static final String PUMP_MONITOR = "pump-monitoring";
    /**
     * 汽机系统监控key
     */
    public static final String TURBINE_MONITOR = "tb-monitoring";
    /**
     * 换热器系统监测key
     */
    public static final String EXCHANGER_MONITOR = "exchanger-monitoring";
    /**
     * /**
     * 阀系统监控key
     */
    public static final String VALVE_MONITOR = "valve-monitoring";

    /**
     * 本年度运行时间
     */
    public static final String CURRENT_YEAR_RUNNING_DURATION = "current_year_running_duration";
    /**
     * 上年度运行时间
     */
    public static final String PREVIOUS_YEAR_RUNNING_DURATION = "previous_year_running_duration";

    public static void main(String[] args) {
        String s = "我是{0}的测试";
        System.out.println(MessageFormat.format(s, "44"));
    }


}
