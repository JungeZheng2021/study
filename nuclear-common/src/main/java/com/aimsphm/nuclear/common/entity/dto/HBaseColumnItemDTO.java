package com.aimsphm.nuclear.common.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

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
@ToString(callSuper = true)
public class HBaseColumnItemDTO extends HBaseParamDTO {
    @ApiModelProperty(value = "指定列名", required = true)
    private Object qualifier;
    @ApiModelProperty(value = "指定列对应的值", required = true)
    private Double value;
}
