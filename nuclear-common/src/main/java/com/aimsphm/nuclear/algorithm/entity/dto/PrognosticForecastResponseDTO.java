package com.aimsphm.nuclear.algorithm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 功能描述:征兆预测
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/07/15 17:51
 */
@Data
public class PrognosticForecastResponseDTO extends SymptomResponseDTO {
    @ApiModelProperty(value = "预测时间")
    private Long predTime;

    @ApiModelProperty(value = "预测时长")
    private String predRange;

    @ApiModelProperty(value = "预测数据")
    private List<PrognosticForecastItemResponseDTO> predData;
}
