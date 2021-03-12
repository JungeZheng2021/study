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
public class FaultReportMarkResponseDTO {

    @ApiModelProperty(value = "类型", notes = "")
    private String markType;

    @ApiModelProperty(value = "图表中的坐标")
    private List<List<Double>> coordinate;

    @ApiModelProperty(value = "注释", notes = "envelopeSpectrum：包络谱")
    private List<String> annotation;

}
