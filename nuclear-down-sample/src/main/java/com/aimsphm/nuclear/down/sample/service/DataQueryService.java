package com.aimsphm.nuclear.down.sample.service;

import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleWithFeatureBO;

import java.util.List;

/**
 * <p>
 * 功能描述:数据查询接口
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/11/01 09:47
 */
public interface DataQueryService {
    /**
     * 获取数据
     *
     * @param single 测点
     * @return 结果集
     */
    List<List<Object>> listHoursData(HistoryQuerySingleWithFeatureBO single);
}
