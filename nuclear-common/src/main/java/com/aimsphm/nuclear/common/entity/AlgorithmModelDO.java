package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 功能描述:算法模型信息实体
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-01-20 14:30
 */
@Data
@TableName("algorithm_model")
@ApiModel(value = "算法模型信息实体")
public class AlgorithmModelDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -5328195471307949083L;

    @ApiModelProperty(value = "子系统id")
    private Long subSystemId;

    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    @ApiModelProperty(value = "算法id")
    private Long algorithmId;

    @ApiModelProperty(value = "模型名称")
    private String modelName;

    @ApiModelProperty(value = "类型")
    private Integer modelType;

    @ApiModelProperty(value = "备注")
    private String remark;

}