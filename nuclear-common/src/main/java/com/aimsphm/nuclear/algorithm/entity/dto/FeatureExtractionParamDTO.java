package com.aimsphm.nuclear.algorithm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Package: com.aimsphm.nuclear.algorithm.entity.dto
 * Description: <特征提取入参>
 *
 * @author milla
 * CreateDate 2020/6/28 10:54
 * UpdateUser: MILLA
 * UpdateDate: 2020/6/28 10:54
 * UpdateRemark: <>
 * Version: 1.0
 */
@Data
public class FeatureExtractionParamDTO {
    @ApiModelProperty(value = "信号redis中的key")
    private String signalKey;

    @ApiModelProperty(value = "sensorCode")
    private String sensorCode;

    @ApiModelProperty(value = "特征类型")
    private String type;

    @ApiModelProperty(value = "转频")
    private Double rotationSpeed;

    @ApiModelProperty(value = "啮合频率")
    private Double meshFrequency;

    @ApiModelProperty(value = "特征值名称")
    private String featName;

    @ApiModelProperty(value = "一小时内的数据", notes = "峰值")
    private List<Double> peakList;

    @ApiModelProperty(value = "一小时内的数据", notes = "油液")
    private List<Double> particles;

    @ApiModelProperty(value = "阈值上限", notes = "油液")
    private Double threshold;

}
