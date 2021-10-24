package com.aimsphm.nuclear.report.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.Map;

/**
 * <p>
 * 功能描述:报告生成服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/07/17 17:29
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
