package com.aimsphm.nuclear.algorithm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class FeatureExtractionResponseDTO {

    @ApiModelProperty(value = "特征值名称")
    private Double featValue;

    @ApiModelProperty(value = "特征值产生时间")
    private Long timestamp;
}
