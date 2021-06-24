package com.aimsphm.nuclear.history.util;

import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.history.entity.enums.TableNameEnum;

import java.util.Objects;

import static com.aimsphm.nuclear.history.constant.MillisecondValueConstant.*;

/**
 * @Package: com.aimsphm.nuclear.history.util
 * @Description: <获取表名工具类>
 * @Author: MILLA
 * @CreateDate: 2020/11/21 11:39
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/21 11:39
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class TableNameParser {
    /**
     * 查过3个小时的数据降采样
     */
    private static final Integer DOWN_SAMPLE_HOURS = 3;

    /**
     * 动态设置表格的名称
     *
     * @param startTimestamp 开始时间
     * @param endTimestamp   结束时间
     * @return 表格名称
     */
    public static String getTableName(long startTimestamp, long endTimestamp) {
        TableNameEnum tableName = getDataBase(startTimestamp, endTimestamp);
        return Objects.isNull(tableName) ? null : tableName.getValue();
    }

    private static TableNameEnum getDataBase(long startTimestamp, long endTimestamp) {
        long gap = endTimestamp - startTimestamp;
        //3小时以下是查询HBase数据库
        if (gap <= MILLISECOND_VALUE_OF_AN_HOUR * DOWN_SAMPLE_HOURS) {
            return null;
        }
        //天表 - 大于3小时小于等于24小时
        if (gap > MILLISECOND_VALUE_OF_AN_HOUR * DOWN_SAMPLE_HOURS && gap <= MILLISECOND_VALUE_OF_A_DAY) {
            return TableNameEnum.DAILY;
        }
        //周表 - 大于24小时小于等于7*24小时
        if (gap > MILLISECOND_VALUE_OF_A_DAY && gap <= MILLISECOND_VALUE_OF_A_WEEK) {
            return TableNameEnum.WEEKLY;
        }
        //半月表 - 大于7*24小时小于等于15*24小时
        if (gap > MILLISECOND_VALUE_OF_A_WEEK && gap <= MILLISECOND_VALUE_OF_A_HALF_MONTH) {
            return TableNameEnum.HALF_MONTHLY;
        }
        //月表 - 大于15*24小时小于等于30*24小时
        if (gap > MILLISECOND_VALUE_OF_A_HALF_MONTH && gap <= MILLISECOND_VALUE_OF_A_MONTH) {
            return TableNameEnum.MONTHLY;
        }
        //季度表 - 大于30*24小时小于等于3*30*24小时
        if (gap > MILLISECOND_VALUE_OF_A_MONTH && gap <= MILLISECOND_VALUE_OF_A_QUARTER) {
            return TableNameEnum.QUARTERLY;
        }
        //半年表 - 大于3*30*24小时小于等于6*30*24小时
        if (gap > MILLISECOND_VALUE_OF_A_QUARTER && gap <= MILLISECOND_VALUE_OF_A_HALF_YEAR) {
            return TableNameEnum.HALF_ANNUALLY;
        }
        //年表 - 大于6*30*24小时小于等于365*24小时
        if (gap > MILLISECOND_VALUE_OF_A_HALF_YEAR && gap <= MILLISECOND_VALUE_OF_A_YEAR) {
            return TableNameEnum.ANNUALLY;
        }
        //2年表 - 大于365*24小时小于等于2*365*24小时
        if (gap > MILLISECOND_VALUE_OF_A_YEAR && gap <= MILLISECOND_VALUE_OF_A_DOUBLE_YEAR) {
            return TableNameEnum.DOUBLE_ANNUALLY;
        }
        //3年表 - 大于2*365*24小时小于等于3*365*24小时
        if (gap > MILLISECOND_VALUE_OF_A_DOUBLE_YEAR && gap <= MILLISECOND_VALUE_OF_A_TRIPLE_YEAR) {
            return TableNameEnum.TRIPLE_ANNUALLY;
        }
        throw new CustomMessageException("不支持查询三年以上数据");
    }

    /**
     * 根据时间区间重置查询的开始时间和结束时间
     *
     * @param startTimestamp 开始时间
     * @param endTimestamp   结束时间
     * @return 时间对象
     */
    public static TimeRangeQueryBO getRangeTime(Long startTimestamp, Long endTimestamp) {
        TimeRangeQueryBO range = new TimeRangeQueryBO();
        TableNameEnum tableName = getDataBase(startTimestamp, endTimestamp);
        if (Objects.isNull(tableName)) {
            return null;
        }
        Long timeUnit = tableName.getTimeUnit();
        if (tableName.equals(TableNameEnum.DAILY)) {
            range.setStart(startTimestamp / timeUnit * timeUnit);
            range.setEnd(endTimestamp);
        } else if (tableName.equals(TableNameEnum.WEEKLY) || tableName.equals(TableNameEnum.HALF_MONTHLY) || tableName.equals(TableNameEnum.MONTHLY)) {
            range.setStart(DateUtils.getStartOfDay(startTimestamp));
            range.setEnd(DateUtils.getEndOfDay(endTimestamp));
        } else {
            range.setStart(startTimestamp - timeUnit);
            range.setEnd(endTimestamp + timeUnit);
        }
        range.setEnd(range.getEnd() > System.currentTimeMillis() ? System.currentTimeMillis() : range.getEnd());
        return range;
    }
}
