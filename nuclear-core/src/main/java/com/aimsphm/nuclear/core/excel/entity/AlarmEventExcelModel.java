package com.aimsphm.nuclear.core.excel.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

@Data
//// 头背景设置成红色 IndexedColors.RED.getIndex()
//@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 10)
//// 头字体设置成20
//@HeadFontStyle(fontHeightInPoints = 20)
//// 内容的背景设置成绿色 IndexedColors.GREEN.getIndex()
//@ContentStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 17)
//// 内容字体设置成20
//@ContentFontStyle(fontHeightInPoints = 20)
public class AlarmEventExcelModel {
    //“序号” ，“报警事件（名称）”，“报警类型”，“事件分类”，“关联设备”，
// “关联测点（清单）”，“备注（内容）”，“异常程度”，“发生次数”，“处理状态”，“最近一次报警（时间）”，“活动状态”。
    @ExcelProperty(value = "序号", index = 0)
    private int seq;
    @ExcelIgnore
    private String alarmCode;
    @ExcelIgnore
    private Long modelId;
    @ExcelIgnore
    private Long deviceId;
    @ExcelProperty(value = "关联设备", index = 4)
    private String deviceName;
    @ColumnWidth(20)
    @ExcelProperty(value = "关联设备code", index = 5)
    private String deviceCode;
    @ExcelIgnore
    private Integer alarmLevel;
    @ExcelProperty(value = "异常程度", index = 8)
    private String alarmLevelContent;
    @ColumnWidth(25)
    @ExcelProperty(value = "最近一次报警（时间）", index = 11)
    private Date lastAlarm;
    @ExcelIgnore
    private Date firstAlarm;
    @ColumnWidth(20)
    @ExcelProperty(value = "报警事件（名称）", index = 1)
    private String alarmContent;
    @ExcelIgnore
    private Integer alarmStatus;
    @ExcelProperty(value = "处理状态", index = 10)
    private String alarmStatusContent;
    @ExcelIgnore
    private Boolean stopFlag;
    @ExcelProperty(value = "活动状态", index = 12)
    private String stopFlagConetent;
    @ExcelProperty(value = "发生次数", index = 9)
    private Integer alarmCount;
    @ColumnWidth(30)
    @ExcelProperty(value = "关联测点", index = 6)
    private String sensorTagids;
    @ExcelIgnore
    private Boolean isAlgorithmAlarm;
    @ExcelIgnore
    private Double alarmFrequency;
    @ExcelIgnore
    private Integer alarmType;
    @ExcelProperty(value = "报警类型", index = 2)
    private String alarmTypeConent;
    @ExcelIgnore
    private String alarmReason;
    @ExcelProperty(value = "事件分类", index = 3)
    private String alarmReasonContent;
    @ExcelProperty(value = "备注内容", index = 7)
    private String remark;
}
