package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 功能描述:查询参数
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/11/21 17:33
 */
@Data
public class HModelEstimateQueryBO {

    @ApiModelProperty(value = "模型id")
    private String modelId;
    @ApiModelProperty(value = "结束行")
    private Long startTime;
    @ApiModelProperty(value = "开始行")
    private Long endTime;
    @ApiModelProperty(value = "指定值")
    private String tagId;
}
