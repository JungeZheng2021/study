package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <模型对应测点信息实体>
 * @Author: MILLA
 * @CreateDate: 2021-01-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("algorithm_model_point")
@ApiModel(value = "模型对应测点信息实体")
public class AlgorithmModelPointDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -4688558069793862933L;

    @ApiModelProperty(value = "子系统id", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "模型id", notes = "")
    private Long modelId;

    @ApiModelProperty(value = "算法id", notes = "")
    private Long algorithmId;

    @ApiModelProperty(value = "测点id-真实id", notes = "")
    private Long pointId;

}