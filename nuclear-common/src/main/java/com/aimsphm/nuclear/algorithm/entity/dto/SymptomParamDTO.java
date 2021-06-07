package com.aimsphm.nuclear.algorithm.entity.dto;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalFaultFeatureDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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

    private List<AlgorithmNormalFaultFeatureDO> featureInfo;

    private List<List<Double>> featureValue;
}
