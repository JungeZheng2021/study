package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 功能描述:条件查询实体
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/09/04 14:42
 */
@Data
public class ConditionsQueryBO extends TimeRangeQueryBO {
    @ApiModelProperty(value = "查询关键字")
    private String keyword;
}
