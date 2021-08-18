package com.aimsphm.nuclear.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.util
 * @Description: <需要保留小数点的操作>
 * @Author: MILLA
 * @CreateDate: 2020/4/17 13:22
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/17 13:22
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class BigDecimalUtils {
    public static void main(String[] args) {
        Double format = BigDecimalUtils.format(0, 4);
        System.out.println(format);
    }

    /**
     * 默认保存1位小数
     */
    public static final int DIGIT = 1;

    /**
     * 格式化精度
     *
     * @param v
     * @param digit 小数位数
     * @return double
     */
    public static Double format(double v, int digit) {
        BigDecimal b = new BigDecimal(v);
        return b.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 格式化精度
     *
     * @param v
     * @param digit 小数位数
     * @return double
     */
    public static Double format(String v, int digit) {
        BigDecimal b = new BigDecimal(v);
        return b.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @param v
     * @param digit
     * @return
     */
    public static Double formatRoundUp(double v, int digit) {
        NumberFormat nf = NumberFormat.getInstance();
        //设置四舍五入
        nf.setRoundingMode(RoundingMode.HALF_UP);
        //设置最小保留几位小数
        nf.setMinimumFractionDigits(digit);
        //设置最大保留几位小数
        nf.setMaximumFractionDigits(digit);
        return Double.valueOf(nf.format(v));
    }

    /**
     * 格式化金额。带千位符
     *
     * @param v
     * @return
     */
    public static String moneyFormat(Double v) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2);
        format.setGroupingSize(3);
        format.setRoundingMode(RoundingMode.FLOOR);
        return format.format(v.doubleValue());
    }

    /**
     * 带小数的显示小数。不带小数的显示整数
     *
     * @param d
     * @return
     */
    public static String doubleTrans(Double d) {
        if (Math.round(d) - d == 0) {
            return String.valueOf((long) d.doubleValue());
        }
        return String.valueOf(d);
    }

    /**
     * BigDecimal 相加
     *
     * @param v1
     * @param v2
     * @return double
     */
    public static Double add(double v1, double v2) {
        BigDecimal n1 = new BigDecimal(String.valueOf(v1));
        BigDecimal n2 = new BigDecimal(String.valueOf(v2));
        return n1.add(n2).doubleValue();
    }

    /**
     * BigDecimal 相减
     *
     * @param v1
     * @param v2
     * @return double
     */
    public static Double subtract(double v1, double v2) {
        BigDecimal n1 = new BigDecimal(String.valueOf(v1));
        BigDecimal n2 = new BigDecimal(String.valueOf(v2));
        return n1.subtract(n2).doubleValue();
    }

    /**
     * BigDecimal 相乘
     *
     * @param v1
     * @param v2
     * @return double
     */
    public static Double multiply(double v1, double v2) {
        BigDecimal n1 = new BigDecimal(String.valueOf(v1));
        BigDecimal n2 = new BigDecimal(String.valueOf(v2));
        return n1.multiply(n2).doubleValue();
    }

    /**
     * BigDecimal 相除
     *
     * @param v1
     * @param v2
     * @return double
     */
    public static Double divide(double v1, double v2) {
        return divide(v1, v2, null);
    }

    public static Double divide(double v1, double v2, Integer digit) {
        BigDecimal n1 = new BigDecimal(String.valueOf(v1));
        BigDecimal n2 = new BigDecimal(String.valueOf(v2));
        return n1.divide(n2, Objects.isNull(digit) ? DIGIT : digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 比较大小 小于0：v1 < v2 大于0：v1 > v2 等于0：v1 = v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static int compare(double v1, double v2) {
        BigDecimal n1 = new BigDecimal(String.valueOf(v1));
        BigDecimal n2 = new BigDecimal(String.valueOf(v2));
        return n1.compareTo(n2);
    }
}
