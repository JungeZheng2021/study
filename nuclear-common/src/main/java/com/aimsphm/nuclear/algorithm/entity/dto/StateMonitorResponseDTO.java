package com.aimsphm.nuclear.algorithm.entity.dto;

import com.aimsphm.nuclear.algorithm.entity.bo.EstimateResponseDataBO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 功能描述:滑动平均值结构体
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/12/22 14:28
 */
@Data
public class StateMonitorResponseDTO {

    @ApiModelProperty(value = "子系统id")
    private Long subSystemId;

    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    @ApiModelProperty(value = "设备code")
    private String deviceCode;

    @ApiModelProperty(value = "设备id")
    private String deviceName;

    @ApiModelProperty(value = "健康状态")
    private Integer healthStatus;

    @ApiModelProperty(value = "运行状态")
    private Integer operationCondition;

    @ApiModelProperty(value = "运行状态时间")
    private Long timestamp;

    @ApiModelProperty(value = "预测结果")
    private List<EstimateResponseDataBO> modelEstimateResult;

    @ApiModelProperty(value = "报警事件结果")
    private List<AlarmEventDTO> txAlarmEvent;
}
