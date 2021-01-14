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
public class AnalysisVibrationResponseDTO {

    @ApiModelProperty(value = "算法返回值", notes = "支持一幅图和多幅图")
    private List<List<List<Object>>> curve;
    @ApiModelProperty(value = "算法返回值容量", notes = "支持一幅图和多幅图")
    private Integer curveCount;
}
