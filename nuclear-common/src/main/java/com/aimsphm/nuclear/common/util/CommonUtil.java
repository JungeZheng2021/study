package com.aimsphm.nuclear.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aimsphm.nuclear.common.constant.RedisKeyConstant;
import com.aimsphm.nuclear.common.entity.bo.HColumnQueryExtendBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class CommonUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String getLogDate(Date date) {
        return sdf.format(date);
    }

    public static QueryWrapper initQueryWrapper(QueryBO dto) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(dto.getEntity());
        return queryWrapper;
    }

    public static HColumnQueryExtendBO buildQueryBo(String tagId, Long start, Long end) {
        HColumnQueryExtendBO queryBO = new HColumnQueryExtendBO();
        queryBO.setStartTime(start);
        queryBO.setEndTime(end);
        queryBO.setTableName(RedisKeyConstant.HBASE_TABLE_NAME);
        queryBO.setFamily(RedisKeyConstant.HBASE_CF);
        queryBO.setTag(tagId);
        return queryBO;
    }

    public static Double genPercentile(int percentile, double[] vlaueArray) {
        if (vlaueArray.length == 0) return null;
        int len = vlaueArray.length;
        int idx = len * percentile / 100;
        return new Double(vlaueArray[idx]);
    }

    public static Double calculateQuantileByValue(Double target, double[] valueArray) {
        Double returnValue = 0D;
        int len = valueArray.length;
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (valueArray[i] > target) {
                count++;
            }
        }
        returnValue = (new Double(count) / len) * 100;
        return returnValue;
    }

    public static String getDateBetweenDesc(Long start, Long end, Integer interval) {
        long between = (end - start) / interval / 1000;
        if (between < 3600) {
            return between / 60 + "分钟" ;
        } else if (between < 86400 && between >= 3600) {
            return between / 3600 + "小时" ;
        } else if (between >= 86400) {
            return between / 86400 + "天" ;
        }
        return "" ;
    }
}
