package com.aimsphm.nuclear.common.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 功能描述:性能预测结果
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/07/16 18:07
 */
@Data
public class JobForecastResultVO {
    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "部件id", notes = "")
    private Long componentId;

    @ApiModelProperty(value = "推理结果", notes = "")
    private String symptomIds;

    @ApiModelProperty(value = "预测时间", notes = "")
    private Date gmtForecast;

    @ApiModelProperty(value = "预测周期", notes = "")
    private String forecastRange;

    @ApiModelProperty(value = "预测结果")
    List<FaultReasoningVO> forecastList;

    @ApiModelProperty(value = "数据结果")
    List<ForecastDataVO> dataList;

    @Data
    public static class ForecastDataVO extends HistoryDataWithThresholdVO {
        @ApiModelProperty(value = "测点id", notes = "")
        private String pointId;

        @ApiModelProperty(value = "历史实测值", notes = "")
        private List<List> historyData;

        @ApiModelProperty(value = "滑动平均数据", notes = "")
        private List<List> trendData;

        @ApiModelProperty(value = "预测数据", notes = "")
        private List<List> forecastData;
    }
}
