package com.aimsphm.nuclear.algorithm.service;

import com.aimsphm.nuclear.common.entity.AlgorithmPrognosticFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.CommonComponentDO;

import java.util.List;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/07/15 16:09
 */
public interface PrognosticForecastService {

    /**
     * 征兆预测定时任务
     */
    void prognosticForecast();

    /**
     * 根据部件id
     *
     * @param componentId
     */
    void prognosticForecastByComponentId(Long componentId);

}
