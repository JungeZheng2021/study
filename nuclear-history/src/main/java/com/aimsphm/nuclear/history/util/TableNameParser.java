package com.aimsphm.nuclear.history.util;

import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.history.entity.enums.TableNameEnum;

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

    public static String getTableName(long startTimestamp, long endTimestamp) {
        long gap = endTimestamp - startTimestamp;
        //3小时以下是查询HBase数据库
        if (gap <= MILLISECOND_VALUE_OF_AN_HOUR * 3) {
            return null;
        }
        if (gap > MILLISECOND_VALUE_OF_AN_HOUR * 3 && gap <= MILLISECOND_VALUE_OF_A_DAY) {
            return TableNameEnum.DAILY.getValue();
        }
        if (gap > MILLISECOND_VALUE_OF_A_DAY && gap <= MILLISECOND_VALUE_OF_A_WEEK) {
            return TableNameEnum.WEEKLY.getValue();
        }
        if (gap > MILLISECOND_VALUE_OF_A_WEEK && gap <= MILLISECOND_VALUE_OF_A_HALF_MONTH) {
            return TableNameEnum.HALF_MONTHLY.getValue();
        }
        if (gap > MILLISECOND_VALUE_OF_A_HALF_MONTH && gap <= MILLISECOND_VALUE_OF_A_MONTH) {
            return TableNameEnum.MONTHLY.getValue();
        }
        if (gap > MILLISECOND_VALUE_OF_A_MONTH && gap <= MILLISECOND_VALUE_OF_A_QUARTER) {
            return TableNameEnum.QUARTERLY.getValue();
        }
        if (gap > MILLISECOND_VALUE_OF_A_QUARTER && gap <= MILLISECOND_VALUE_OF_A_HALF_YEAR) {
            return TableNameEnum.HALF_ANNUALLY.getValue();
        }
        if (gap > MILLISECOND_VALUE_OF_A_HALF_YEAR && gap <= MILLISECOND_VALUE_OF_A_YEAR) {
            return TableNameEnum.ANNUALLY.getValue();
        }
        if (gap > MILLISECOND_VALUE_OF_A_YEAR && gap <= MILLISECOND_VALUE_OF_A_double_year) {
            return TableNameEnum.DOUBLE_ANNUALLY.getValue();
        }
        if (gap > MILLISECOND_VALUE_OF_A_double_year && gap <= MILLISECOND_VALUE_OF_A_TRIPLE_YEAR) {
            return TableNameEnum.TRIPLE_ANNUALLY.getValue();
        }
        throw new CustomMessageException("不支持查询三年以上数据");
    }
}
