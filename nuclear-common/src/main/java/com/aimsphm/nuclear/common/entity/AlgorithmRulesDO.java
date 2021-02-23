package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <实体>
 * @Author: MILLA
 * @CreateDate: 2021-01-29
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("algorithm_rules")
@ApiModel(value = "实体")
public class AlgorithmRulesDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6598535114259004287L;

    @ApiModelProperty(value = "", notes = "")
    private String sensorCode;

    @ApiModelProperty(value = "诊断规则", notes = "")
    private String rule;

    @ApiModelProperty(value = "规则描述", notes = "")
    private String description;

}