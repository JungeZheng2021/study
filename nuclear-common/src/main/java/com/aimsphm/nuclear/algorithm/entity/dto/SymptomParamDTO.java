package com.aimsphm.nuclear.algorithm.entity.dto;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalFaultFeatureDO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Package: com.aimsphm.nuclear.algorithm.entity.dto
 * Description: <征兆入参>
 *
 * @author milla
 * CreateDate 2020/6/28 10:54
 * UpdateUser: MILLA
 * UpdateDate: 2020/6/28 10:54
 * UpdateRemark: <>
 * Version: 1.0
 */
@Data
public class SymptomParamDTO {

    private Long invokingTime;

    private List<AlgorithmNormalFaultFeatureDO> featureInfo;

    private Map<String, List<List>> featureValue;

    public SymptomParamDTO() {
        invokingTime = System.currentTimeMillis();
    }
}
