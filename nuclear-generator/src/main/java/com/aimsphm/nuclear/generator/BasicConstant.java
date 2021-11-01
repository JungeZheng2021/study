package com.aimsphm.nuclear.generator;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * @Package: com.aimsphm.nuclear.generator
 * @Description: <表格和数据源配置项>
 * @Author: MILLA
 * @CreateDate: 2020/11/12 17:48
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/12 17:48
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class BasicConstant {
    /**
     * 作者
     */
    public static String AUTHOR = "MILLA";
    /**
     * 生成的实体类忽略表前缀: 不需要则置空
     */
    public static String ENTITY_IGNORE_PREFIX = "";
    /**
     * 表名
     */
    public static String[] TABLES = {
//            "common_site"
//            , "common_set"
//            , "common_system"
//            , "common_sub_system"
//            , "common_device"
//            , "common_device_details"
//            "common_component",
//            "common_sensor_component"
//            "common_sensor"
//            "biz_original_data"
//            "common_sensor_settings"
//            , "common_measure_point"
//            "job_device_status"
//            "algorithm_config",
//            "algorithm_device_model",
//            "algorithm_model"
//            "algorithm_model_point",
//            "spark_down_sample_annually"
//            "job_alarm_realtime",
//            "job_alarm_event",
//            "job_alarm_threshold"
//            "analysis_favorite",
//            "analysis_favorite_remark"
//            "algorithm_rules",
//            "algorithm_rules_parameter",
//            "algorithm_rules_conclusion"
//            "biz_diagnosis_result"
//            "auth_privilege"
//            "job_alarm_process_record"
//            "algorithm_parameter_alarm_event"
//            "algorithm_normal_rule_feature",
//            "algorithm_normal_feature_feature"
//            "algorithm_normal_fault_conclusion"
//            "algorithm_prognostic_fault_feature"
            "spark_down_sample_config"
//            "biz_down_sample"
//            "job_forecast_result"
//            "algorithm_normal_rule"
//            "algorithm_normal_fault_feature"
//            "biz_report",
//            "biz_job_quartz_config",
//            "biz_report_config"
    };

    /**
     * 实体类的父类Entity
     */
    public static String SUPER_ENTITY_CLASS = "com.aimsphm.nuclear.common.entity.BaseDO";

    /**
     * 基础父类继承字段
     */
    public static String[] SUPER_ENTITY_COLUMNS = {
            "id", "creator", "modifier", "gmt_modified", "gmt_create", "deleted", "sort"
    };

    /**
     * 数据库
     */
    public static String username = "root";
    public static String password = "aims2016";
    public static String url = "jdbc:mysql://192.168.16.28:3306/nuclear_tw?useAffectedRows=true&allowMultiQueries=true&characterEncoding=utf8&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
    public static DbType DB_TYPE = DbType.MYSQL;
    public static String driverClassName = "com.mysql.cj.jdbc.Driver";
}
