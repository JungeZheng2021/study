package com.aimsphm.nuclear.algorithm.entity.dto;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalRuleDO;
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
public class FaultReasoningParamVO {
    @ApiModelProperty(value = "征兆集合")
    private List<SymptomVO> symSet;

    @ApiModelProperty(value = "关联规则集合")
    private List<AlgorithmNormalRuleDO> refRuleSet;

    @Data
    public static class SymptomVO {
        @ApiModelProperty(value = "征兆主键", notes = "1表示发生，0表示未知，-1已知未发生")
        private Long symId;

        @ApiModelProperty(value = "")
        private Integer symCorr;

        public SymptomVO(Long symId) {
            this.symId = symId;
            init();
        }

        private void init() {
            this.symCorr = 1;
        }
    }
}
