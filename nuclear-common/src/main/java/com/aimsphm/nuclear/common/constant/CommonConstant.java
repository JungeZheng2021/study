package com.aimsphm.nuclear.common.constant;

/**
 * 共用常量类
 *
 * @author
 */
public class CommonConstant {

    static public final String I18N_PREFIX = "nuclear__";
    static public final String I18N_LOCALE = "locale";
    static public final String I18N_EN = "EN";
    static public final String I18N_CN = "CN";
    /**
     * 测点存储在redis中前缀
     */
    public static final String REDIS_POINT_REAL_TIME_PRE = "pump:real_time:";
    /**
     * 预警测点redis中前缀
     */
    public static final String REDIS_POINT_REAL_TIME_WARNING_LIST_PRE = "pump:real_time:warning_list:";
    /**
     * 测点信息集合
     */
    public static final String REDIS_POINT_INFO_LIST = "pump:point_info";
    /**
     * 非Pi测点信息集合
     */
    public static final String REDIS_NONE_PI_POINT_INFO_LIST = "pump:npi_point_info";
    /**
     * 设备健康状况前缀
     */
    public static final String REDIS_DEVICE_MIDDLE_STATUS_PRE = "pump:device_middle_status:";
    public static final String REDIS_DEVICE_HEALTH_INFO_PRE = "pump:device_health_info:";
    public static final String REDIS_TURBINE_DEVICE_HEALTH_INFO_PRE = "turbine:device_health_info:";
    /**
     * 报警状况前缀
     */
    public static final String REDIS_ALARM_PUMP_INFO_PRE = "pump:alarm_device_info:";
    public static final String REDIS_BASE_SNAPSHOT_LIST = "pump:base_snapshot_list:";
    public static final String REDIS_SENSOR_TREND_RECOGNITION_PRE = "sensor:trend_recognition:";
    public static final String REDIS_SENSOR_COLLECTOR_PRE = "sensor:collector_time:";
    public static final String REDIS_LAST_VIB_UNIT_ALARM_PRE = "device:vib_unit_alarm_time:";
    public static final String REDIS_LAST_VIB_UNIT_ALARM_RETRY_PRE = "device:vib_unit_alarm_time:retry:";
//    //子系统下设备id集合
//    public static final String REDIS_DEVICE_ID_LIST_PRE = "subsystem:device_list_info:";

    public static final String HBASE_TABLE_NAME = "npc_real_time";

    public static final String HBASE_CF = "pRaw";
    /**
     * redis key中的下划线
     */
    public static final String REDIS_KEY_UNDERLINE = "_";
    /**
     * 冒号
     */
    public static final String REDIS_KEY_COLON = ":";
    /**
     * 缓存前缀
     */
    public static final String CACHE_KEY_PREFIX = "NUCLEAR_CACHE:";

    public static final String VEC_RMS = "_vec-Rms";

    public static final String RMS_VEC = "vec-Rms";
    public static final long ONEDAY_DURATION = 86400000l;
}
