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
public class PrognosticForecastItemResponseDTO extends SymptomResponseDTO {
    @ApiModelProperty(value = "测点id")
    private String pointId;

    @ApiModelProperty(value = "滑动平均")
    private List<List<Object>> history;

    @ApiModelProperty(value = "预测数据")
    private List<List<Object>> pred;
}
