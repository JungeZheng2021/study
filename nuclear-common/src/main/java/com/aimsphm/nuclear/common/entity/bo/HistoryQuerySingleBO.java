package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 功能描述:单个测点查询
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/11/21 17:33
 */
@Data
@ApiModel(value = "历史查询单个测点")
public class HistoryQuerySingleBO extends TimeRangeQueryBO {
    @ApiModelProperty(value = "测点编号", notes = "如果是自装传感器的话包含特征")
    private String pointId;
}
