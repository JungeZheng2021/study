package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <实体>
 * @Author: MILLA
 * @CreateDate: 2021-06-04
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-06-04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("algorithm_normal_rule_feature")
@ApiModel(value = "实体")
public class AlgorithmNormalRuleFeatureDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -7518128054768613970L;

    @ApiModelProperty(value = "", notes = "")
    private Long ruleId;

    @ApiModelProperty(value = "", notes = "")
    private Long featureId;

    @ApiModelProperty(value = "", notes = "")
    private Integer correlationLevel;

    @ApiModelProperty(value = "", notes = "")
    private Float correlation;

}