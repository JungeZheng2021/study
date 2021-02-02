package com.aimsphm.nuclear.algorithm.entity.dto;

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
public class FaultDiagnosisParamDTO {

    @ApiModelProperty(value = "规则集合", notes = "")
    private List<RuleParamDTO> rules;

    @ApiModelProperty(value = "是否需要自动报告数据", notes = " 1:表是输出 0:表示不输出")
    private Integer returnReport;
}
