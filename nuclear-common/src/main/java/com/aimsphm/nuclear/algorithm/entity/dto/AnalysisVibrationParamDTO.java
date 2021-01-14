package com.aimsphm.nuclear.algorithm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.entity.dto
 * @Description: <振动分析实体>
 * @Author: MILLA
 * @CreateDate: 2021/01/14 13:36
 * @UpdateUser: MILLA
 * @UpdateDate: 2021/01/14 13:36
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class AnalysisVibrationParamDTO {
    @ApiModelProperty(value = "原始数据", notes = "")
    private Double[] signal;

    @ApiModelProperty(value = "采样频率", notes = "")
    private Integer fs;

    @ApiModelProperty(value = "信号下限频率", notes = "")
    private Integer minFrequency;

    @ApiModelProperty(value = "信号上限频率", notes = "")
    private Integer maxFrequency;

    @ApiModelProperty(value = "信号类型", notes = "加速度为3，速度为2，位移1，包络0")
    private Integer signalType;

    @ApiModelProperty(value = "小波分解最大层数", notes = "默认设置为3")
    private Integer maxLevel;

    @ApiModelProperty(value = "算法类型")
    private String type;


}
