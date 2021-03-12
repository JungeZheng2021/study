package com.aimsphm.nuclear.report.service;

import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.report.service
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/07/17 17:29
 * @UpdateUser: milla
 * @UpdateDate: 2020/07/17 17:29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface WordOperationService {

    /**
     * 操作word文档
     *
     * @param doc           文档
     * @param tagPre        前缀
     * @param allReportData 所有的数据
     * @throws Exception
     */
    void operationPlaceholderInWord(XWPFDocument doc, String tagPre, Map<String, Object> allReportData) throws Exception;
}
