package com.aimsphm.nuclear.algorithm.service;

import com.aimsphm.nuclear.algorithm.entity.dto.SymptomResponseDTO;
import com.aimsphm.nuclear.common.enums.PointTypeEnum;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.service
 * @Description: <特征提取接口>
 * @Author: milla
 * @CreateDate: 2021/06/03 12:43
 * @UpdateUser: milla
 * @UpdateDate: 2021/06/03 12:43
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface FeatureExtractionOperationService {
    /**
     * 操作特征数据
     *
     * @param calculate
     */
    void operationFeatureExtractionData(PointTypeEnum calculate);

    /**
     * 征兆判断
     *
     * @return
     * @param pointIds
     */
    SymptomResponseDTO symptomJudgment(List<String> pointIds);
}
