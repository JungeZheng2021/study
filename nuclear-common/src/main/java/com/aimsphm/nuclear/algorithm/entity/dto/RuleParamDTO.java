package com.aimsphm.nuclear.algorithm.entity.dto;

import com.aimsphm.nuclear.common.entity.AlgorithmRulesParameterDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.entity.dto
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/24 15:27
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/24 15:27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class RuleParamDTO {
    @ApiModelProperty(value = "规则id")
    private Integer ruleId;

    @ApiModelProperty(value = "传感器编号")
    private String sensorCode;

    @ApiModelProperty(value = "具体规则名称")
    private String rule;

    @ApiModelProperty(value = "参数详情")
    private List<AlgorithmRulesParameterDO> parameter;


}
