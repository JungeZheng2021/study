package com.aimsphm.nuclear.algorithm.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Package: com.aimsphm.nuclear.algorithm.entity.dto
 * Description: <故障推理>
 *
 * @author milla
 * CreateDate 2020/6/28 10:54
 * UpdateUser: MILLA
 * UpdateDate: 2020/6/28 10:54
 * UpdateRemark: <>
 * Version: 1.0
 */
@Data
public class FaultReasoningResponseDTO {


    private List<ReasonResult> reasonResultList;

    @Data
    public static class ReasonResult {
        @ApiModelProperty(value = "推荐比率")
        private Double recommend;

        @ApiModelProperty(value = "故障对象")
        private FaultInfo faultInfo;
    }

    @Data
    public static class FaultInfo {
        @ApiModelProperty(value = "故障主键")
        private Long faultId;

        @ApiModelProperty(value = "机理模型")
        private Integer mechanismCode;

        @ApiModelProperty(value = "征兆集合")
        private List<FaultReasoningParamVO.SymptomVO> symSet;
    }
}
