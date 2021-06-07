package com.aimsphm.nuclear.algorithm.entity.dto;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.AlgorithmNormalRuleDO;
import com.aimsphm.nuclear.common.entity.vo.SymptomCorrelationVO;
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
public class FaultReasoningParamDTO {

    private Integer deviceType;

    private List<FaultReasoningParamVO.Symptom> symSet;

    private List<AlgorithmNormalRuleDO> refRuleSet;

    private List<SymptomCorrelationVO> symCorr;

    private List<AlgorithmNormalFaultFeatureDO> symInfoSet;

    @ApiModelProperty(value = "是否推荐试验的标志", notes = "")
    private Boolean examRec;

    private Boolean reasonCase;

    public FaultReasoningParamDTO() {
        init();
    }

    private void init() {
        this.examRec = false;
        this.reasonCase = false;
    }
}
