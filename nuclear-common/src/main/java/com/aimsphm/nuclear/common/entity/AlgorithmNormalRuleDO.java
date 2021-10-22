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
@TableName("algorithm_normal_rule")
@ApiModel(value = "实体")
public class AlgorithmNormalRuleDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6104965913290725297L;

    @ApiModelProperty(value = "", notes = "")
    private Integer deviceType;

    @ApiModelProperty(value = "", notes = "")
    private Integer additionalType;

    @ApiModelProperty(value = "", notes = "")
    private String ruleName;

    @ApiModelProperty(value = "", notes = "")
    private String ruleDesc;

    @ApiModelProperty(value = "", notes = "")
    private Integer ruleType;

    @ApiModelProperty(value = "", notes = "")
    private String ruleCode;

    @ApiModelProperty(value = "", notes = "")
    private Long conclusionId;

    @ApiModelProperty(value = "", notes = "")
    private Long componentId;

    @ApiModelProperty(value = "规则完整性", notes = "")
    private Float ruleIntegrity;

}