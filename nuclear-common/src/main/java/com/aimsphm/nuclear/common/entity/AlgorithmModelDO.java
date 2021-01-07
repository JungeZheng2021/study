package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <算法模型信息实体>
 * @Author: MILLA
 * @CreateDate: 2021-01-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("algorithm_model")
@ApiModel(value = "算法模型信息实体")
public class AlgorithmModelDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -5328195471307949083L;

    @ApiModelProperty(value = "子系统id", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "算法id", notes = "")
    private Long algorithmId;

    @ApiModelProperty(value = "", notes = "")
    private Long deviceModelId;

    @ApiModelProperty(value = "", notes = "")
    private String modelName;

    @ApiModelProperty(value = "", notes = "")
    private Integer modelType;

    @ApiModelProperty(value = "", notes = "")
    private String remark;

}