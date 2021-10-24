package com.aimsphm.nuclear.report.service;

import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;

import java.util.Map;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/5/12 19:39
 */
public interface ReportDataService {

    /**
     * 根据条件获取所有的需要的数据
     * 1.测点趋势图
     * 2.测点趋势详情
     * 3.测点阈值
     * 4.占位符的替换数据
     *
     * @param query
     * @return
     */
    Map<String, Object> getAllReportData(ReportQueryBO query);

    /**
     * 获取油液数据
     *
     * @param deviceId
     * @param data
     */
    void storeOilPointValue(Long deviceId, Map<String, Object> data);
}
