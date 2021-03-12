package com.aimsphm.nuclear.algorithm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

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
public class FaultDiagnosisResponseDTO {

    @ApiModelProperty(value = "被激活的规则", notes = "")
    private List<Integer> activation;

    @ApiModelProperty(value = "自动报告需要的数据", notes = "")
    private Map<String, List<FaultReportResponseDTO>> reportFigure;
}
