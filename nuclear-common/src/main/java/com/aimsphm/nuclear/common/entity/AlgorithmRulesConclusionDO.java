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
@TableName("algorithm_rules_conclusion")
@ApiModel(value = "实体")
public class AlgorithmRulesConclusionDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -7491098858982813582L;

    @ApiModelProperty(value = "", notes = "")
    private Integer ruleId;

    @ApiModelProperty(value = "诊断结论", notes = "")
    private String conclusion;

    @ApiModelProperty(value = "故障原因", notes = "")
    private String reason;

    @ApiModelProperty(value = "", notes = "")
    private String suggest;

}