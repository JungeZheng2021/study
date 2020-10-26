package com.aimsphm.nuclear.core.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MdSensorDaqConfigBO {

    @ApiModelProperty(value = "通频值YT:Min")
    private Double ytMin;
    @ApiModelProperty(value = "通频值YT:Max")
    private Double ytMax;

    @ApiModelProperty(value = "采样频率",required = true)
    private Integer daqFrequency;
    @ApiModelProperty(value = "采样间隔",required = true)
    private Integer sleepTime;
    @ApiModelProperty(value = "采样时长",required = true)
    private Integer daqTime;
    @ApiModelProperty(value = "fmin",required = true)
    private Double fmin;
    @ApiModelProperty(value = "fmax",required = true)
    private Double fmax;
    @ApiModelProperty(value = "采样配置方式：0自动 1手动",required = true)
    private Integer mode;

    @ApiModelProperty(value = "采样配置所有相关传感器id",required = true)
    private List<Long> sensorIds;

}
