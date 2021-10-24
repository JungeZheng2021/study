package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 功能描述:模型对应测点信息实体
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-01-05 14:30
 */
@Data
@TableName("algorithm_model_point")
@ApiModel(value = "模型对应测点信息实体")
public class AlgorithmModelPointDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -4688558069793862933L;

    @ApiModelProperty(value = "子系统id")
    private Long subSystemId;

    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    @ApiModelProperty(value = "模型id")
    private Long modelId;

    @ApiModelProperty(value = "算法id")
    private Long algorithmId;

    @ApiModelProperty(value = "测点id-真实id")
    private Long pointId;

}