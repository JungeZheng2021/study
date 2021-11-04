package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.SparkDownSample;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleWithFeatureBO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 功能描述:服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-12-14 14:30
 */
public interface SparkDownSampleService extends IService<SparkDownSample> {

    /**
     * 获取 数据
     *
     * @param pointIds 测点结合
     * @param start    开始时间
     * @param end      结束时间
     * @return 结果集
     */
    Map<String, List<SparkDownSample>> listDataFromDailyTable(List<String> pointIds, Long start, Long end);

    /**
     * 获取数据
     *
     * @param pointInfo 测点信息
     * @return 结果集
     */
    List<List<Object>> listDataFromDailyData(HistoryQuerySingleWithFeatureBO pointInfo);

    /**
     * 根据条件获取数据
     *
     * @param pointIds 测点集合
     * @param start    开始时间
     * @param end      结束时间
     * @return 结果集
     */
    Map<String, List<SparkDownSample>> listDataByRangeTime(List<String> pointIds, Long start, Long end);
}
