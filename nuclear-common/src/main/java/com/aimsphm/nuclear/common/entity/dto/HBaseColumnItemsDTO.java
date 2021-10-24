package com.aimsphm.nuclear.common.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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
public class HBaseColumnItemsDTO extends HBaseParamDTO {
    @ApiModelProperty(value = "指定列集合", required = true)
    private List<HBaseColumnDoubleDTO> qualifiers;
}
