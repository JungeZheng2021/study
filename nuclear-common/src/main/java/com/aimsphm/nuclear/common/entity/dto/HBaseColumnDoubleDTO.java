package com.aimsphm.nuclear.common.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/3/6 10:04
 */
@Data
public class HBaseColumnDoubleDTO extends HBaseTimeSeriesDataDTO {
    @ApiModelProperty(value = "指定列名", required = true)
    private Object qualifier;
}
