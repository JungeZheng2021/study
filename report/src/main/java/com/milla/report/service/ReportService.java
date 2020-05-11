package com.milla.report.service;


import com.milla.report.pojo.bo.ReportQueryBO;

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
    void manualReport(ReportQueryBO query) throws Exception;
}
