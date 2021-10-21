package com.aimsphm.nuclear.algorithm.service;

import com.aimsphm.nuclear.common.enums.PointTypeEnum;

/**
 * <p>
 * 功能描述:特征提取接口
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/06/03 12:43
 */
public interface FeatureExtractionOperationService {
    /**
     * 操作特征数据
     *
     * @param calculate
     */
    void operationFeatureExtractionData(PointTypeEnum calculate);
}
