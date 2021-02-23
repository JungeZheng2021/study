package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <算法配置实体>
 * @Author: MILLA
 * @CreateDate: 2021-02-04
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("algorithm_config")
@ApiModel(value = "算法配置实体")
public class AlgorithmConfigDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -5360204367689511954L;

    @ApiModelProperty(value = "", notes = "")
    private String algorithmName;

    @ApiModelProperty(value = "", notes = "")
    private String algorithmType;

}