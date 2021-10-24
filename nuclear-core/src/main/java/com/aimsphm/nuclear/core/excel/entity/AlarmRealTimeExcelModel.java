package com.aimsphm.nuclear.core.excel.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;


@Data
public class AlarmRealTimeExcelModel {
    @ExcelProperty(value = "序号", index = 0)
    private int seq;
    @ExcelProperty(value = "报警时间", index = 1)
    @ColumnWidth(25)
    private Date alarmTime;
    @ExcelIgnore
    private String alarmCode;
    @ExcelIgnore
    private Integer alarmType;
    @ExcelProperty(value = "报警类型", index = 2)
    private String alarmTypeContent;
    @ExcelIgnore
    private Long modelId;
    @ExcelIgnore
    private Long deviceId;
    @ExcelProperty(value = "所属设备", index = 5)
    @ColumnWidth(30)
    private String deviceName;
    @ExcelIgnore
    private Boolean isAlgorithmAlarm;
    @ExcelProperty(value = "报警测点", index = 3)
    @ColumnWidth(30)
    private String sensorTagid;
    @ExcelProperty(value = "测点名称", index = 4)
    @ColumnWidth(30)
    private String sensorName;
    @ExcelIgnore
    private Long eventId;
    @ExcelIgnore
    private Integer trend;
    @ExcelProperty(value = "测点趋势", index = 6)
    private String trendContent;
    @ExcelIgnore
    private String evaluation;
    @ExcelProperty(value = "报警评价", index = 7)
    private String evaluationContent;

}
