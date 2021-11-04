package com.aimsphm.nuclear.common.util;

import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static java.time.DayOfWeek.MONDAY;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.springframework.util.StringUtils.hasText;

/**
 * <p>
 * 功能描述:时间操作工具
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2018/5/4 16:02
 */
@Slf4j
public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final String YEAR_MONTH_DAY_I = "yyyy/MM/dd";
    public static final String YEAR_MONTH_DAY_M = "yyyy-MM-dd";
    /**
     * 月日十分
     */
    public static final String MONTH_DAY_HH_MM_M = "MM-dd HH:mm";
    /**
     * 年份
     */
    public static final String YEAR = "yyyy";
    public static final String YEAR_ZH = "yyyy年";
    public static final String YEAR_MONTH_DAY_ZH = "yyyy年MM月dd日";

    public static final String YEAR_MONTH_DAY_HH_MM_SS_ZH = "yyyy年MM月dd日 HH:mm:ss";
    public static final String MONTH_ZH = "M月";
    public static final String YEAR_MONTH_DAY_HH_MM_SS_I = "yyyy/MM/dd HH:mm:ss";
    public static final String YEAR_MONTH_DAY_HH_MM_SS_M = "yyyy-MM-dd HH:mm:ss";
    public static final String YEAR_MONTH_DAY_HH_MM_SS_NONE = "yyyyMMddHHmmss";
    public static final String YEAR_MONTH_DAY_HH_MM_SS_SSS_NONE = "yyyyMMddHHmmssSSS";
    public static final String YEAR_MONTH_DAY_HH_MM_I = "yyyy/MM/dd HH:mm";
    public static final String YEAR_MONTH_DAY_HH_MM_M = "yyyy-MM-dd HH:mm";
    public static final String YEAR_MONTH_DAY_HH_MM_SS_SSS_I = "yyyy/MM/dd HH:mm:ss.SSS";
    public static final String YEAR_MONTH_DAY_HH_MM_SS_SSS_M = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String WEB_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static String formatCurrentDateTime() {
        return formatCurrentDateTime(null);
    }

    /**
     * 对给定时间进行时间的加减操作
     *
     * @param now         指定时间
     * @param amountToAdd 增加的数量
     * @param unit        增加的单位
     * @return 时间
     */
    public static Date plus(Date now, Long amountToAdd, TemporalUnit unit) {
        LocalDateTime nowDateTime = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime plus = nowDateTime.plus(amountToAdd, unit);
        Instant instant = plus.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * 是否是同一年
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 布尔
     */
    public static boolean isSameYear(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return cal1.get(1) == cal2.get(1);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    /**
     * 对给定时间进行时间的加减操作
     *
     * @param now         指定时间
     * @param amountToAdd 增加的数量
     * @param unit        增加的单位
     * @return 时间
     */
    public static Date plus(Long now, Long amountToAdd, TemporalUnit unit) {
        return plus(new Date(now), amountToAdd, unit);
    }

    /**
     * 格式化上一年的时间
     *
     * @param pattern 指定格式
     * @return 时间字符串输出
     */
    public static String formatPreviousYear(String pattern) {
        LocalDate now = LocalDate.now();
        LocalDate plus = now.plus(-1, ChronoUnit.YEARS);
        return format(pattern, plus);
    }

    /**
     * 格式化当前时间
     *
     * @param pattern 指定格式
     * @return 时间字符串输出
     */
    public static String formatCurrentDateTime(String pattern) {
        return format(pattern, new Date());
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
     * 将LocalDate转换程Date对象
     *
     * @param date 时间
     * @return 时间
     */
    public static Date transition(LocalDate date) {
        ZonedDateTime dateTime = date.atStartOfDay(ZoneId.systemDefault());
        return Date.from(dateTime.toInstant());
    }

    /**
     * 将Date转换程LocalDate对象
     *
     * @param date 时间
     * @return 时间
     */
    public static LocalDate transition(Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.toLocalDate();
    }

    /**
     * 将Date转换程LocalDate对象
     *
     * @param timestamp 时间
     * @return 时间
     */
    public static LocalDate transition(Long timestamp) {
        LocalDateTime localDateTime = new Date(timestamp).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.toLocalDate();
    }

    /**
     * 获取某时间的开始[从凌晨开始]
     *
     * @param timestamps 指定时间戳
     * @return 日期的字符串格式输出
     */
    public static Long getStartOfDay(Long timestamps) {
        Date date = new Date(timestamps);
        LocalDate transition = transition(date);
        return transition(transition).getTime();
    }

    /**
     * 获取某一时间的开始小时
     *
     * @param timestamps 指定时间戳
     * @return 日期的字符串格式输出
     */
    public static Long getEndOfHour(Long timestamps) {
        Date date = Objects.isNull(timestamps) ? new Date() : new Date(timestamps);
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime newLocalDateTime = localDateTime.plusHours(1);
        Instant instant = newLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date from = Date.from(instant);
        return from.getTime() / 3600 / 1000 * 3600 * 1000 - 99;
    }

    /**
     * 获取某一时间的结束小时
     *
     * @param timestamps 指定时间戳
     * @return 日期的字符串格式输出
     */
    public static Long getStartOfHour(Long timestamps) {
        if (Objects.isNull(timestamps)) {
            return System.currentTimeMillis() / 3600 / 1000 * 3600 * 1000;
        }
        return timestamps / 3600 / 1000 * 3600 * 1000;
    }

    /**
     * 获取前一个月第一天[从凌晨开始]
     *
     * @param timestamps 指定时间戳
     * @return 日期的字符串格式输出
     */
    public static Long getEndOfDay(Long timestamps) {
        Date date = new Date(timestamps);
        LocalDate transition = transition(date);
        LocalDate localDate = transition.plusDays(1);
        return transition(localDate).getTime() - 99;
    }

    /**
     * 获取前一天的开始
     *
     * @return 长整型
     */
    public static Long getStartOfPreviousDay() {
        LocalDate localDate = LocalDate.now();
        return transition(localDate.plusDays(-1)).getTime();
    }

    /**
     * 获取前一天的结束
     *
     * @return 长整型
     */
    public static Long getEndOfPreviousDay() {
        LocalDate localDate = LocalDate.now();
        return transition(localDate).getTime() - 99;
    }

    /**
     * 获取前一天的开始
     *
     * @return 长整型
     */
    public static Long getStartOfPreviousDay(Long timestamps) {
        Date date = new Date(timestamps);
        LocalDate localDate = transition(date);
        return transition(localDate.plusDays(-1)).getTime();
    }

    /**
     * 获取前一天的结束
     *
     * @return 长整型
     */
    public static Long getEndOfPreviousDay(Long timestamps) {
        Date date = new Date(timestamps);
        LocalDate localDate = transition(date);
        return transition(localDate).getTime() - 99;
    }

    /**
     * 获取前一周的开始时间
     *
     * @return 长整型
     */
    public static Long getStartOfPreviousWeek() {
        LocalDate localDate = LocalDate.now();
        return transition(localDate.plusWeeks(-1).with(DayOfWeek.MONDAY)).getTime();
    }

    /**
     * 获取前一周的结束时间
     *
     * @return 长整型
     */
    public static Long getEndOfPreviousWeek() {
        LocalDate localDate = LocalDate.now();
        return transition(localDate.plusWeeks(0).with(DayOfWeek.MONDAY)).getTime() - 99;
    }

    /**
     * 获取某时间前一周的开始时间
     *
     * @return 长整型
     */
    public static Long getStartOfPreviousWeek(Long timestamp) {
        LocalDate localDate = transition(new Date(timestamp));
        return transition(localDate.plusWeeks(-1).with(DayOfWeek.MONDAY)).getTime();
    }

    /**
     * 获取时间前一周的结束时间
     *
     * @return 长整型
     */
    public static Long getEndOfPreviousWeek(Long timestamp) {
        LocalDate localDate = transition(new Date(timestamp));
        return transition(localDate.plusWeeks(0).with(DayOfWeek.MONDAY)).getTime() - 99;
    }

    /**
     * 获取前一个月第一天[从凌晨开始]
     *
     * @param timestamps 指定时间戳
     * @return 日期的字符串格式输出
     */
    public static Date previousMonthFirstDay(long timestamps) {
        Date date = new Date(timestamps);
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDate now = localDateTime.toLocalDate();
        //本月的第一天
        LocalDate localDate = LocalDate.of(now.getYear(), now.getMonth().plus(-1), 1);
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
     * 获取前一个月最后一天[下一个凌晨前一秒结束]
     *
     * @param timestamps 指定时间戳
     * @return 日期的字符串格式输出
     */
    public static Date previousMonthLastDay(long timestamps) {
        Date date = new Date(timestamps);
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDate now = localDateTime.toLocalDate();
        //本月的第一天
        LocalDate firstDay = LocalDate.of(now.getYear(), now.getMonth(), 1);
        ZonedDateTime dateTime = firstDay.atStartOfDay(ZoneId.systemDefault());
        Instant plus = dateTime.toInstant().plus(-1, MILLIS);
        return Date.from(plus);
    }

    /**
     * 获取当前时间前一个月月份
     *
     * @param timestamps 指定时间戳
     * @return 日期的字符串格式输出
     */
    public static String formatPreviousMonth(long timestamps, String pattern) {
        Date previousMonth = previousMonthFirstDay(timestamps);
        return format(hasText(pattern) ? pattern : MONTH_ZH, previousMonth);
    }

    /**
     * 格式化给定时间的月份
     *
     * @param timestamps 指定时间戳
     * @return 日期的字符串格式输出
     */
    public static String formatMonth(Long timestamps, String pattern) {
        return format(hasText(pattern) ? pattern : MONTH_ZH, new Date(timestamps));
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
     * @return 字符串
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
     * @return 字符串
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
     * @param date 时间
     * @return 字符串
     */
    public static String format(LocalDateTime date) {
        return format(YEAR_MONTH_DAY_HH_MM_SS_M, date);
    }

    /**
     * 格式化
     *
     * @param pattern 指定样式
     * @param date    时间
     * @return 字符串
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
     * @return 字符串
     */
    public static String format(String pattern, Long timestamp) {
        return format(pattern, new Date(timestamp));
    }

    /**
     * 格式化数据
     *
     * @param timestamp 时间戳
     * @return 字符串
     */
    public static String format(Long timestamp) {
        return format(YEAR_MONTH_DAY_HH_MM_SS_M, timestamp);
    }

    /**
     * 计算两个时间的差值
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param unit  单位
     * @return 整型
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
     * @return 整型
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

    /**
     * 将时间间隔格式化程天数，周数，月数，年数等
     *
     * @param durationTimes 时间间隔
     * @param unit          要格式化成的单位
     * @return 格式化结果
     */
    public static Double format(Long durationTimes, TemporalUnit unit) {
        Duration duration = unit.getDuration();
        return BigDecimalUtils.divide(durationTimes, (double) duration.getSeconds() * 1000, 2);
    }

    /**
     * 当前时间加上几个小时的最大值
     *
     * @return 时间戳
     */
    public static Long plusHoursMaxValue(Long previousHours) {
        return plusHoursMaxValue(previousHours, new Date());
    }

    /**
     * 当前时间加上几个小时的最小值
     *
     * @return 时间戳
     */
    public static Long plusHoursMinValue(Long previousHours) {
        return plusHoursMinValue(previousHours, new Date());
    }

    /**
     * 指定时间加上几个小时的最大值
     *
     * @return 时间戳
     */
    public static Long plusHoursMaxValue(Long previousHours, Date date) {
        LocalDateTime localDateTime = Objects.isNull(date) ? LocalDateTime.now() : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return getLongMax(previousHours, localDateTime);
    }

    /**
     * 指定时间加上几个小时的最大值
     *
     * @return 时间戳
     */
    public static Long plusHoursMinValue(Long previousHours, LocalDateTime localDateTime) {
        if (Objects.isNull(previousHours)) {
            return System.currentTimeMillis() / 3600 / 1000 * 3600 * 1000;
        }
        LocalDateTime newLocalDateTime = (Objects.isNull(localDateTime) ? LocalDateTime.now() : localDateTime).plusHours(previousHours);
        Instant instant = newLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date from = Date.from(instant);
        return from.getTime() / 3600 / 1000 * 3600 * 1000;
    }

    /**
     * 指定时间加上几个小时的最大值
     *
     * @return 时间戳
     */
    public static Long plusHoursMaxValue(Long previousHours, LocalDateTime localDateTime) {
        localDateTime = (Objects.isNull(localDateTime) ? LocalDateTime.now() : localDateTime);
        return getLongMax(previousHours, localDateTime);
    }

    private static Long getLongMax(Long previousHours, LocalDateTime localDateTime) {
        LocalDateTime newLocalDateTime = localDateTime.plusHours(Objects.isNull(previousHours) ? 1 : previousHours + 1);
        Instant instant = newLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date from = Date.from(instant);
        return from.getTime() / 3600 / 1000 * 3600 * 1000 - 999;
    }

    /**
     * 指定时间加上几个小时的最大值
     *
     * @return 时间戳
     */
    public static Long plusHoursMinValue(Long previousHours, Date date) {
        if (Objects.isNull(previousHours)) {
            return System.currentTimeMillis() / 3600 / 1000 * 3600 * 1000;
        }
        LocalDateTime localDateTime = Objects.isNull(date) ? LocalDateTime.now() : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime newLocalDateTime = localDateTime.plusHours(previousHours);
        Instant instant = newLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date from = Date.from(instant);
        return from.getTime() / 3600 / 1000 * 3600 * 1000;
    }

    /**
     * 获取当前时间的前一个小时最大值
     *
     * @return 时间戳
     */
    public static Long previousHourMaxValue() {
        return plusHoursMaxValue(-1L);
    }

    /**
     * 获取当前时间的前一个小时最小值
     *
     * @return 时间戳
     */
    public static Long previousHourMinValue() {
        return plusHoursMinValue(-1L);
    }

    /**
     * 判断是不是每月的第一天
     *
     * @param timestamps 指定时间戳
     * @return 日期的字符串格式输出
     */
    public static boolean isStartOfMonth(Long timestamps) {
        return DateUtils.transition(timestamps).getDayOfMonth() == 1;
    }

    /**
     * 判断是不是每周的第一天
     *
     * @param timestamps 指定时间戳
     * @return 日期的字符串格式输出
     */
    public static boolean isStartOfWeek(Long timestamps) {
        return DateUtils.transition(timestamps).getDayOfWeek() == MONDAY;
    }

    /**
     * 判断是不是每周的第一天
     *
     * @param timestamps 指定时间戳
     * @return 日期的字符串格式输出
     */
    public static boolean isStartOfWeek(Long timestamps, DayOfWeek week) {
        return DateUtils.transition(timestamps).getDayOfWeek() == week;
    }

    /**
     * 判断是不是每天的开始
     *
     * @param timestamps 指定时间戳
     * @return 日期的字符串格式输出
     */
    public static boolean isStartOfDay(Long timestamps) {
        ZonedDateTime dateTime = DateUtils.transition(timestamps).atStartOfDay(ZoneId.systemDefault());
        Instant plus = dateTime.toInstant();
        return Date.from(plus).getTime() - timestamps / 1000 * 1000 == 0;
    }
}
