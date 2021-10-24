package com.aimsphm.nuclear.report.service.impl;

import com.aimsphm.nuclear.report.service.WordOperationService;
import com.aimsphm.nuclear.report.util.FanWordUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 功能描述:rcv文档操作类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/07/17 17:30
 */
@Service("RcvWord")
public class FanWordOperationServiceImpl implements WordOperationService {

    @Resource
    private FanWordUtils wordUtils;

    @Override
    public void operationPlaceholderInWord(XWPFDocument doc, String tagPre, Map<String, Object> allReportData) throws Exception {
        wordUtils.operationPlaceholderInWord(doc, allReportData);
    }
}
