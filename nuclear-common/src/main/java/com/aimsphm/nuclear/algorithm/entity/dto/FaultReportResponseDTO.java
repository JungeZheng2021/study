package com.aimsphm.nuclear.algorithm.entity.dto;

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
public class FaultReportResponseDTO {

    @ApiModelProperty(value = "传感器编号", notes = "")
    private String sensorCode;

    @ApiModelProperty(value = "类型", notes = "envelopeSpectrum：包络谱")
    private String figureType;

    @ApiModelProperty(value = "图标数据")
    private List<List<Double>> curve;

    @ApiModelProperty(value = "类型", notes = "envelopeSpectrum：包络谱")
    private List<FaultReportMarkResponseDTO> mark;

}
