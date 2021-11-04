package com.aimsphm.nuclear.down.sample.service;

import com.aimsphm.nuclear.common.entity.bo.HistoryQueryFilledBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;

import java.util.List;

/**
 * <p>
 * 功能描述:降采样服务
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/11/01 09:49
 */
public interface DownSampleService {


    /**
     * 执行降采样
     *
     * @param type     降采样类型
     * @param rate     降采率
     * @param rangTime 起止时间
     */
    void executeDownSample(String type, Integer rate, TimeRangeQueryBO rangTime);

    /**
     * 执行全部时间
     *
     * @param rangeTime 起止时间
     */
    void allData(TimeRangeQueryBO rangeTime);

    /**
     * 为历史查询进行补点，不做保存
     *
     * @param bo 补点实体
     * @return 最终的补点数据
     */
    List<List<Object>> executeDownSample4HistoryData(HistoryQueryFilledBO bo);
}
