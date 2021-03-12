package com.aimsphm.nuclear.report.service.impl;

import com.aimsphm.nuclear.report.service.WordOperationService;
import com.aimsphm.nuclear.report.util.FanWordUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.report.service.impl
 * @Description: <汽机文档操作累>
 * @Author: milla
 * @CreateDate: 2020/07/17 17:30
 * @UpdateUser: milla
 * @UpdateDate: 2020/07/17 17:30
 * @UpdateRemark: <>
 * @Version: 1.0
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
