package com.milla.report.service.impl;

import com.milla.report.constant.ReportConstant;
import com.milla.report.pojo.bo.ReportQueryBO;
import com.milla.report.service.ReportService;
import com.milla.report.util.ReportUtils;
import com.milla.report.util.UUIDUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @Package: com.aimsphm.nuclear.report.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/5/11 11:01
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/11 11:01
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportUtils reportUtils;

    @Override
    public void manualReport(ReportQueryBO query) throws Exception {
        try {
            //加载模板
            URL resource = this.getClass().getResource(ReportConstant.PROJECT_TEMPLATES_ROOT_DIR + ReportConstant.TEMPLATE_DOC_NAME);

            InputStream is = resource.openStream();

            XWPFDocument doc = new XWPFDocument(is);
            reportUtils.operationPlaceholderInWord(doc, query);
            final String docPath = ReportConstant.DOC_TEMP_DIR_PRE;
            final String docName = query.getSubSystemId() + File.separator + UUIDUtils.randomUUID() + ReportConstant.DOC_SUFFIX;
            File file = new File(docPath, docName);
            if (!file.exists()) {
                //先得到文件的上级目录，并创建上级目录，在创建文件
                file.getParentFile().mkdir();
                try {
                    //创建文件
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            doc.write(fos);
            fos.close();
            //保存报告记录
        } catch (Exception e) {
            throw e;
        }

    }
}
