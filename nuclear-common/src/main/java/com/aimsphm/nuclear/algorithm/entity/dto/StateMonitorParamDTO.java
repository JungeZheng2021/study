package com.aimsphm.nuclear.algorithm.entity.dto;

import com.aimsphm.nuclear.algorithm.entity.bo.EstimateParamDataBO;
import com.aimsphm.nuclear.algorithm.entity.bo.PointDataBO;
import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
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
public class StateMonitorParamDTO {

    private Long invokingTime;

    private Integer algorithmPeriod;

    @ApiModelProperty(value = "只输出设备运行状态", notes = "1")
    private Integer onlyCondition;

    @ApiModelProperty(value = "设备模型id集合")
    private List<Long> modelIds;

    private Long subSystemId;

    private Long deviceId;

    private String deviceName;
    
    private String deviceCode;

    @ApiModelProperty(value = "最新一次工况信息")
    private HBaseTimeSeriesDataDTO lastCondition;

    @ApiModelProperty(value = "历史数据")
    private List<PointDataBO> sensorData;

    @ApiModelProperty(value = "残差数据")
    private List<EstimateParamDataBO> modelEstimateResult;

    @ApiModelProperty(value = "报警列表")
    private List<JobAlarmEventDO> txAlarmEvent;
}
