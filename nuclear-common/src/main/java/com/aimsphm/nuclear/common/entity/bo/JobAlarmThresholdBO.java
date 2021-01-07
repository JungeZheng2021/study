package com.aimsphm.nuclear.common.entity.bo;

import com.aimsphm.nuclear.common.enums.AlarmMessageEnum;
import com.aimsphm.nuclear.common.enums.ThresholdAlarmStatusEnum;
import com.aimsphm.nuclear.common.enums.ThresholdStatusEnum;
import com.aimsphm.nuclear.common.util.BigDecimalUtils;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

import static com.aimsphm.nuclear.common.constant.SymbolConstant.COLON;

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
public class JobAlarmThresholdBO {

    @ExcelProperty(value = "序号", index = 0)
    private Long id;

    @ColumnWidth(20)
    @ExcelProperty(value = "报警设备", index = 1)
    private String deviceName;

    @ColumnWidth(25)
    @ExcelProperty(value = "报警测点", index = 2)
    private String pointId;

    @ColumnWidth(10)
    @ExcelProperty(value = "报警类型", index = 3)
    private String alarmLevel;

    @ColumnWidth(18)
    @ExcelProperty(value = "首次报警", index = 4)
    private Date gmtFirstAlarm;

    @ColumnWidth(18)
    @ExcelProperty(value = "最近报警", index = 5)
    private Date gmtLastAlarm;

    @ColumnWidth(10)
    @ExcelProperty(value = "持续时间", index = 6)
    private String duration;

    @ColumnWidth(10)
    @ExcelProperty(value = "处理状态", index = 7)
    private String operateStatus;

    @ColumnWidth(10)
    @ExcelProperty(value = "活动状态", index = 8)
    private String alarmStatus;


    public void setAlarmLevel(Integer alarmLevel) {
        String desc = AlarmMessageEnum.getDescByLevel(alarmLevel);
        if (Objects.nonNull(desc) && desc.contains(COLON)) {
            this.alarmLevel = desc.substring(0, desc.length() - 1);
        } else {
            this.alarmLevel = AlarmMessageEnum.getDescByLevel(alarmLevel);
        }
    }

    public void setOperateStatus(Integer operateStatus) {
        this.operateStatus = ThresholdStatusEnum.getDesc(operateStatus);
    }

    public void setAlarmStatus(Integer alarmStatus) {
        this.alarmStatus = ThresholdAlarmStatusEnum.getDesc(alarmStatus);
    }
}