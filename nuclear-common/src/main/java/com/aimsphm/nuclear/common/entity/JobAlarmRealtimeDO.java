package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <实体>
 * @Author: MILLA
 * @CreateDate: 2021-01-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("job_alarm_realtime")
@ApiModel(value = "实体")
public class JobAlarmRealtimeDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -7067557058325651204L;

    @ApiModelProperty(value = "报警编码", notes = "")
    private String alarmCode;

    @ApiModelProperty(value = "模型编号", notes = "")
    private Long modelId;

    @ApiModelProperty(value = "子系统id", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "报警测点", notes = "")
    private String pointId;

    @ApiModelProperty(value = "报警时间", notes = "")
    private Date gmtAlarmTime;

    @ApiModelProperty(value = "事件id", notes = "")
    private Long eventId;

    @ApiModelProperty(value = "报警评价", notes = "")
    private String evaluation;

    @ApiModelProperty(value = "报警级别", notes = "1：1级报警、2：2级报警、3：3级报警、4：4级报警、5：5级报警")
    private Integer alarmLevel;

    @ApiModelProperty(value = "报警类型:", notes = "-1其他、1阈值、2波动、3尖峰、4阶跃、5算法")
    private Integer alarmType;

    @ApiModelProperty(value = "趋势", notes = "")
    private Integer trend;

}