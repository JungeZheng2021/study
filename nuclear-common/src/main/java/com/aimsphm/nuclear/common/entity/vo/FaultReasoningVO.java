package com.aimsphm.nuclear.common.entity.vo;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalFaultConclusionDO;
import com.aimsphm.nuclear.common.entity.AlgorithmNormalRuleDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.entity.vo
 * @Description: <报警事件记录vo>
 * @Author: MILLA
 * @CreateDate: 2020/5/9 9:38
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/9 9:38
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class FaultReasoningVO {
    @ApiModelProperty(value = "推荐度")
    private Double recommend;

    @ApiModelProperty(value = "故障描述")
    private String ruleDesc;

    @ApiModelProperty(value = "详细信息")
    private AlgorithmNormalRuleDO faultInfo;

    @ApiModelProperty(value = "故障特征")
    private List<AlgorithmNormalFaultFeatureVO> features;

    @ApiModelProperty(value = "故障结论")
    private AlgorithmNormalFaultConclusionDO conclusion;
}
