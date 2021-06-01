package com.aimsphm.nuclear.algorithm.entity.dto;

import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.entity.JobAlarmProcessRecordDO;
import com.aimsphm.nuclear.common.entity.JobAlarmRealtimeDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.entity.dto
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/24 15:27
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/24 15:27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class AlarmEventDTO extends JobAlarmEventDO {

    @ApiModelProperty(value = "时时报警", notes = "")
    private List<JobAlarmRealtimeDO> realTimeAlarms;

    @ApiModelProperty(value = "报警记录", notes = "")
    private JobAlarmProcessRecordDO processRecord;
}
