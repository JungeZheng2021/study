package com.aimsphm.nuclear.common.entity.bo;

import com.aimsphm.nuclear.common.entity.BaseDO;
import com.aimsphm.nuclear.common.enums.EventStatusEnum;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <报警事件信息实体>
 * @Author: MILLA
 * @CreateDate: 2020-12-28
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-28
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@ApiModel(value = "报警事件信息实体")
public class JobAlarmEventBO {

    @ExcelProperty(value = "序号", index = 0)
    private Long id;
    @ColumnWidth(25)
    @ExcelProperty(value = "报警事件", index = 1)
    private String eventName;
    @ColumnWidth(20)
    @ExcelProperty(value = "设备名称", index = 2)
    private String deviceName;
    @ColumnWidth(25)
    @ExcelProperty(value = "报警测点", index = 3)
    private String pointIds;
    @ColumnWidth(15)
    @ExcelProperty(value = "报警发生次数", index = 4)
    private Integer alarmCount;
    @ColumnWidth(10)
    @ExcelProperty(value = "报警级别", index = 5)
    private Integer alarmLevel;
    @ColumnWidth(10)
    @ExcelProperty(value = "报警状态", index = 6)
    private String eventStatus;
    @ColumnWidth(18)
    @ExcelProperty(value = "首次报警", index = 7)
    private Date gmtFirstAlarm;
    @ColumnWidth(18)
    @ExcelProperty(value = "最近报警", index = 8)
    private Date gmtLastAlarm;

    public void setEventStatus(Integer eventStatus) {
        this.eventStatus = EventStatusEnum.getDesc(eventStatus);
    }
}