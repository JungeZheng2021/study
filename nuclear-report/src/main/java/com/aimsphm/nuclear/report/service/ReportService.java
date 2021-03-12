package com.aimsphm.nuclear.report.service;

import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;

import java.io.FileNotFoundException;

/**
 * @Package: com.aimsphm.nuclear.report.service
 * @Description: <报告生成服务类>
 * @Author: MILLA
 * @CreateDate: 2020/5/11 11:00
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/11 11:00
 * @UpdateRemark: <>
 * @Version: 1.0
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
