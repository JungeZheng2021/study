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
 * @CreateDate: 2021-05-25
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-05-25
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("job_alarm_process_record")
@ApiModel(value = "实体")
public class JobAlarmProcessRecordDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6709584931489071966L;

    @ApiModelProperty(value = "子系统id", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "报警测点", notes = "")
    private String pointIds;

    @ApiModelProperty(value = "报警时间", notes = "")
    private Date gmtEventTime;

    @ApiModelProperty(value = "事件id", notes = "")
    private Long eventId;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "报警级别", notes = "1：1级报警、2：2级报警、3：3级报警、4：4级报警、5：5级报警")
    private Integer alarmLevel;

}