package com.aimsphm.nuclear.generator;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * @Package: com.aimsphm.nuclear.generator
 * @Description: <表格和数据源配置项>
 * @Author: milla
 * @CreateDate: 2020/11/12 17:48
 * @UpdateUser: milla
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
    public static String ENTITY_IGNORE_PREFIX = "md_";
    /**
     * 表名
     */
    public static String[] TABLES = {
            "common_site"
            , "common_set"
            , "common_system"
            , "common_sub_system"
            , "common_device"
            , "common_device_details"
            , "common_component"
            , "common_sensor"
            , "common_measure_point"
            , "job_device_status"
            , "job_alarm_event"
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
