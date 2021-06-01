package com.aimsphm.nuclear.algorithm.entity.dto;

import com.aimsphm.nuclear.algorithm.entity.bo.EstimateResponseDataBO;
import com.aimsphm.nuclear.common.entity.JobAlarmRealtimeDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.entity.bo
 * @Description: <滑动平均值结构体>
 * @Author: MILLA
 * @CreateDate: 2020/12/22 14:28
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/22 14:28
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class StateMonitorResponseDTO {

    @ApiModelProperty(value = "子系统id", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "设备id", notes = "")
    private String deviceCode;

    @ApiModelProperty(value = "设备id", notes = "")
    private String deviceName;

    @ApiModelProperty(value = "健康状态", notes = "")
    private Integer healthStatus;

    @ApiModelProperty(value = "运行状态", notes = "")
    private Integer operationCondition;

    @ApiModelProperty(value = "运行状态时间", notes = "")
    private Long timestamp;

    @ApiModelProperty(value = "预测结果", notes = "")
    private List<EstimateResponseDataBO> modelEstimateResult;

    @ApiModelProperty(value = "报警事件结果", notes = "")
    private List<AlarmEventDTO> txAlarmEvent;
}
