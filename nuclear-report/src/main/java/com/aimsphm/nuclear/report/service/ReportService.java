package com.aimsphm.nuclear.report.service;

import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;

/**
 * <p>
 * 功能描述:报告生成服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/28 17:49
 */
public interface ReportService {

    /**
     * 手动生成报告
     *
     * @param query 查询条件
     */
    void saveManualReport(ReportQueryBO query);

    /**
     * 自动生成报告
     *
     * @param query 查询条件
     */
    void saveAutoReport(ReportQueryBO query);
}
