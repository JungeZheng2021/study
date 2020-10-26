package com.aimsphm.nuclear.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 振动分析用户备注
 *
 * @author lu.yi
 * @since 2020-08-14
 */
@Data
public class MdVibrationAnalysisFavoriteRemark extends ModelBase {

    /**
     * 收藏id
     */
    @ApiModelProperty(value = "收藏id")
    private Long favoriteId;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String remark;
}