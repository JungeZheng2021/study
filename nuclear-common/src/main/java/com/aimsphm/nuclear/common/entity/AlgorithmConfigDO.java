package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 功能描述:算法配置实体
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-02-04 14:30
 */
@Data
@TableName("algorithm_config")
@ApiModel(value = "算法配置实体")
public class AlgorithmConfigDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -5360204367689511954L;

    @ApiModelProperty(value = "算法名称")
    private String algorithmName;

    @ApiModelProperty(value = "算法类型")
    private String algorithmType;

}