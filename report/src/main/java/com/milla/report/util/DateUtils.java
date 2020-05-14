package com.milla.report.util;

import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.MILLIS;

/**
 * @Package: com.milla.report.util
 * @Description: <时间操作工具>
 * @Author: MILLA
 * @CreateDate: 2018/5/4 16:02
 * @UpdateUser: MILLA
 * @UpdateDate: 2018/5/4 16:02
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final String YEAR_MONTH_DAY_I = "yyyy/MM/dd";
    public static final String YEAR_MONTH_DAY_M = "yyyy-MM-dd";
    public static final String YEAR_MONTH_DAY_ZH = "yyyy年MM月dd日";
    public static final String YEAR_MONTH_DAY_HH_MM_SS_I = "yyyy/MM/dd HH:mm:ss";
    public static final String YEAR_MONTH_DAY_HH_MM_SS_M = "yyyy-MM-dd HH:mm:ss";
    public static final String YEAR_MONTH_DAY_HH_MM_SS_NONE = "yyyyMMddHHmmss";
    public static final String YEAR_MONTH_DAY_HH_MM_SS_SSS_NONE = "yyyyMMddHHmmssSSS";
    public static final String YEAR_MONTH_DAY_HH_MM_I = "yyyy/MM/dd HH:mm";
    public static final String YEAR_MONTH_DAY_HH_MM_M = "yyyy-MM-dd HH:mm";
    public static final String YEAR_MONTH_DAY_HH_MM_SS_SSS_I = "yyyy/MM/dd HH:mm:ss.SSS";
    public static final String YEAR_MONTH_DAY_HH_MM_SS_SSS_M = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String WEB_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static void main(String[] args) {
        System.out.println(previousMonthFirstDay().getTime());
        System.out.println(previousMonthLastDay().getTime());
        long until = DateUtils.until(previousMonthFirstDay(), new Date(), ChronoUnit.DAYS) + 1;
        System.out.println(until);
        System.out.println(new Date());
        System.out.println(plus(new Date(), 1L, ChronoUnit.DAYS));
    }

    public static String formatCurrentDateTime() {
        return formatCurrentDateTime(null);
    }


    /**
     * 对给定时间进行时间的加减操作
     *
     * @param now         指定时间
     * @param amountToAdd 增加的数量
     * @param unit        增加的单位
     * @return
     */
    public static Date plus(Date now, Long amountToAdd, TemporalUnit unit) {
        LocalDateTime nowDateTime = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime plus = nowDateTime.plus(amountToAdd, unit);
        Instant instant = plus.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 对给定时间进行时间的加减操作
     *
     * @param now         指定时间
     * @param amountToAdd 增加的数量
     * @param unit        增加的单位
     * @return
     */
    public static Date plus(Long now, Long amountToAdd, TemporalUnit unit) {
        return plus(new Date(now), amountToAdd, unit);
    }

    /**
     * 格式化当前时间
     *
     * @param pattern 指定格式
     * @return 时间字符串输出
     */
    public static String formatCurrentDateTime(String pattern) {
        LocalDate now = LocalDate.now();
        return format(pattern, now);
    }

    /**
     * 获取当前月份第一天
     *
     * @return 日期的字符串格式输出
     */
    public static String formatCurrentMonthFirstDay() {
        return formatCurrentMonthFirstDay(null);
    }

    /**
     * 获取当前月份最后一天
     *
     * @return 日期的字符串格式输出
     */
    public static String formatCurrentMonthLastDay() {
        return formatCurrentMonthLastDay(null);
    }

    /**
     * 获取前一月第一天
     *
     * @return 日期的字符串格式输出
     */
    public static String formatPreviousMonthFirstDay() {
        return formatPreviousMonthFirstDay(null);
    }

    /**
     * 获取前一个月最后一天
     *
     * @return 日期的字符串格式输出
     */
    public static String formatPreviousMonthLastDay() {
        return formatPreviousMonthLastDay(null);
    }

    /**
     * 获取前一个月第一天[从凌晨开始]
     *
     * @return 日期的字符串格式输出
     */
    public static Date previousMonthFirstDay() {
        LocalDate today = LocalDate.now();
        //本月的第一天
        LocalDate localDate = LocalDate.of(today.getYear(), today.getMonth().plus(-1), 1);
        ZonedDateTime dateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(dateTime.toInstant());
    }

    /**
     * 获取前一个月最后一天[下一个凌晨前一秒结束]
     *
     * @return 日期的字符串格式输出
     */
    public static Date previousMonthLastDay() {
        LocalDate today = LocalDate.now();
        //本月的第一天
        LocalDate firstDay = LocalDate.of(today.getYear(), today.getMonth(), 1);
        ZonedDateTime dateTime = firstDay.atStartOfDay(ZoneId.systemDefault());
        Instant plus = dateTime.toInstant().plus(-1, MILLIS);
        return Date.from(plus);
    }

    /**
     * 获取当前月份第一天
     *
     * @param pattern 指定格式
     * @return 日期的字符串格式输出
     */
    public static String formatCurrentMonthFirstDay(String pattern) {
        LocalDate today = LocalDate.now();
        //本月的第一天
        LocalDate firstDay = LocalDate.of(today.getYear(), today.getMonth(), 1);
        return format(pattern, firstDay);
    }

    /**
     * 获取前一个月最后一天
     *
     * @param pattern 指定格式
     * @return 日期的字符串格式输出
     */
    public static String formatPreviousMonthFirstDay(String pattern) {
        LocalDate today = LocalDate.now();
        //本月的第一天
        LocalDate firstDay = LocalDate.of(today.getYear(), today.getMonth().plus(-1), 1);
        return format(pattern, firstDay);
    }

    /**
     * 获取前一个月第一天
     *
     * @param pattern 指定格式
     * @return 日期的字符串格式输出
     */
    public static String formatPreviousMonthLastDay(String pattern) {
        LocalDate today = LocalDate.now();
        //本月的第一天
        LocalDate firstDay = LocalDate.of(today.getYear(), today.getMonth().plus(-1), 1);
        LocalDate last = firstDay.with(TemporalAdjusters.lastDayOfMonth());
        return format(pattern, last);
    }

    /**
     * 格式化
     *
     * @param pattern 指定样式
     * @param date    时间
     * @return
     */
    public static String format(String pattern, LocalDate date) {
        if (Objects.isNull(pattern) || pattern.length() == 0) {
            return date.format(DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_ZH, Locale.CHINA));
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化
     *
     * @param pattern 指定样式
     * @param date    时间
     * @return
     */
    public static String format(String pattern, LocalDateTime date) {
        if (Objects.isNull(pattern) || pattern.length() == 0) {
            return date.format(DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_HH_MM_M, Locale.CHINA));
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化
     *
     * @param pattern 指定样式
     * @param date    时间
     * @return
     */
    public static String format(String pattern, Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return format(pattern, localDateTime);
    }

    /**
     * 格式化
     *
     * @param pattern   指定样式
     * @param timestamp 时间戳
     * @return
     */
    public static String format(String pattern, Long timestamp) {
        return format(pattern, new Date(timestamp));
    }

    /**
     * 计算两个时间的差值
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param unit  单位
     * @return
     */
    public static long until(Date start, Date end, TemporalUnit unit) {
        LocalDateTime startDateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDateTime = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return startDateTime.until(endDateTime, unit);
    }

    /**
     * 计算两个时间的差值
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param unit      单位
     * @return
     */
    public static long until(Long startTime, Long endTime, TemporalUnit unit) {
        return until(new Date(startTime), new Date(endTime), unit);
    }


    /**
     * 获取当前月份最后一天
     *
     * @param pattern 指定格式
     * @return 日期的字符串格式输出
     */
    public static String formatCurrentMonthLastDay(String pattern) {
        LocalDate today = LocalDate.now();
        //本月的最后一天
        LocalDate lastDay = today.with(TemporalAdjusters.lastDayOfMonth());
        return format(pattern, lastDay);
    }
}
