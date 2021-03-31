package com.aimsphm.nuclear.executor.util;

import com.aimsphm.nuclear.executor.constant.JobFrequencyConstant;
import scala.Tuple2;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Date;

public class TimeCalculator {
    public static Tuple2<String, String> getStartAndEndTimestamp(Date currentDate, String frequency) {
        Tuple2<String, String> result = null;
        long currentTimeInMills = currentDate.getTime();
        long cleanTime = currentTimeInMills - currentTimeInMills % 3600000;

        LocalDateTime lo = DateUtil.DateToLocalDateTime(currentDate);

        int month = lo.get(ChronoField.MONTH_OF_YEAR);
        int day = lo.get(ChronoField.DAY_OF_MONTH);

        switch (frequency) {
            case JobFrequencyConstant.HOURLY: {
                long start = cleanTime - 3600000;
                long end = cleanTime;
                result = new Tuple2<>(start + "", end + "");
                break;
            }
            /**
             * hbase的scan是左闭右开的区间
             */
            case JobFrequencyConstant.DAILY: {
                long start = cleanTime - 3600000 * 24;
                long end = cleanTime;
                result = new Tuple2<>(start + "", end + "");
                break;
            }
            case JobFrequencyConstant.WEEKLY: {
                long start = cleanTime - 3600000 * 24 * 7;
                long end = cleanTime;
                result = new Tuple2<>(start + "", end + "");
                break;
            }
            case JobFrequencyConstant.HALF_MONTHLY: {
                if (day == 16) {
                    long start = cleanTime - 3600000 * 24 * 15;
                    long end = cleanTime;
                    result = new Tuple2<>(start + "", end + "");
                } else {
                    if (day == 1) {
                        LocalDateTime prev = lo.minusMonths(1).plusDays(15).withHour(0).withMinute(0).withSecond(0);
                        long start = DateUtil.localDateTime2Date(prev).getTime();
                        long end = cleanTime;
                        result = new Tuple2<>(start + "", end + "");
                    }
                }

                break;
            }
            case JobFrequencyConstant.MONTHLY: {
                LocalDateTime prevMonth = lo.minusMonths(1).withHour(0).withMinute(0).withSecond(0);
                long start = DateUtil.localDateTime2Date(prevMonth).getTime();
                long end = cleanTime;
                result = new Tuple2<>(start + "", end + "");
                break;
            }
        }


        return result;
    }

    public static Tuple2<String, String> getStartAndEndTimestampForMonth(long startTimeStamp) {
        Tuple2<String, String> result = null;
        long currentTimeInMills = startTimeStamp;
        long cleanTime = currentTimeInMills - currentTimeInMills % 3600000;
        Date currentDate = new Date(cleanTime);
        LocalDateTime lo = DateUtil.DateToLocalDateTime(currentDate);

        int month = lo.get(ChronoField.MONTH_OF_YEAR);
        int day = lo.get(ChronoField.DAY_OF_MONTH);
        LocalDateTime nextMonth = lo.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        long start = cleanTime;
        long end = DateUtil.localDateTime2Date(nextMonth).getTime();
        result = new Tuple2<>(start + "", end + "");


        return result;
    }
}
