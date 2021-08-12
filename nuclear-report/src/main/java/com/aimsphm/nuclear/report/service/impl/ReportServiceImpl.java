package com.aimsphm.nuclear.report.service.impl;

import com.aimsphm.nuclear.common.constant.ReportConstant;
import com.aimsphm.nuclear.common.entity.BizReportDO;
import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;
import com.aimsphm.nuclear.common.enums.DataStatusEnum;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.service.BizReportService;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.report.service.ReportDataService;
import com.aimsphm.nuclear.report.service.ReportService;
import com.aimsphm.nuclear.report.service.WordOperationService;
import com.aimsphm.nuclear.report.util.UUIDUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.aimsphm.nuclear.common.constant.ReportConstant.PATH_DEVICE;
import static com.aimsphm.nuclear.common.constant.ReportConstant.PATH_SUB_SYSTEM;

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
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Resource
    private BizReportService reportService;
    @Resource
    private CommonDeviceService deviceService;

    @Resource
    private Map<String, ReportDataService> dataService;
    @Resource
    private Map<String, WordOperationService> wordOperationService;
    /**
     * 数据服务类后缀
     */
    public static final String DATA_SERVICE_SUFFIX = "Data";
    /**
     * 文档服务类后缀
     */
    public static final String WORD_SERVICE_SUFFIX = "Word";


    @Override
    @Async
    public void saveManualReport(ReportQueryBO query) {
        Assert.notNull(query.getSubSystemId(), "The subSystemId can not be null");
        Assert.notNull(query.getStartTime(), "The startTime can not be null");
        Assert.notNull(query.getEndTime(), "The endTime can not be null");
        Assert.isTrue(query.getEndTime() > query.getStartTime(), "The end time must be greater than the start time");
        CommonDeviceDO byId = deviceService.getById(query.getDeviceId());
        if (Objects.isNull(byId)) {
            return;
        }
        query.setDeviceName(byId.getDeviceName());
        new Thread(() -> operationWord(query, 2)).start();
    }

    @Override
    public void saveAutoReport(ReportQueryBO query) {
        operationWord(query, 1);
    }

    String operationWord(ReportQueryBO query, Integer type) {
        if (Objects.isNull(query.getEndTime())) {
            query.setEndTime(System.currentTimeMillis());
            query.setStartTime(System.currentTimeMillis() - 30 * 86400000);
        }
        log.info("报告生成中....参数：{}", query);
        final String docName = (Objects.nonNull(query.getDeviceId()) ? PATH_DEVICE + query.getDeviceId() : PATH_SUB_SYSTEM + query.getSubSystemId()) + File.separator + UUIDUtils.randomUUID() + ReportConstant.DOC_SUFFIX;
        FileOutputStream fos = null;
        Long reportId = null;
        try {
            BizReportDO find = findReport(query);
            if (Objects.nonNull(find)) {
                reportId = saveReport(query, docName, type);
                throw new CustomMessageException("当前有正在运行的报告");
            }
            reportId = saveReport(query, docName, type);
            if (Objects.isNull(reportId)) {
                throw new CustomMessageException("报告生成失败");
            }
            String templatePath = query.getTemplatePath();
//                        加载模板
            URL resource = this.getClass().getResource(templatePath);
            InputStream is;
            //上线要去除
            if (resource == null) {
                is = new FileInputStream("D:\\Java\\workspace\\nuclear_power\\nuclear-report\\src\\main\\resources" + templatePath);
            } else {
                is = resource.openStream();
            }
            XWPFDocument doc = new XWPFDocument(is);
            ReportDataService reportDataService = dataService.get(query.getReportCategory() + DATA_SERVICE_SUFFIX);
            WordOperationService wordService = this.wordOperationService.get(query.getReportCategory() + WORD_SERVICE_SUFFIX);
            if (Objects.nonNull(wordService) && Objects.nonNull(reportDataService)) {
                //获取所有的数据
                Map<String, Object> allReportData = reportDataService.getAllReportData(query);
                if (Objects.nonNull(allReportData) && !allReportData.isEmpty()) {
                    //操作文档
                    wordService.operationPlaceholderInWord(doc, query.getTagPre(), allReportData);
                }
            }
            final String docPath = ReportConstant.DOC_TEMP_DIR_PRE;
            File file = new File(docPath, docName);
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                parentFile.mkdirs();
            }
            fos = new FileOutputStream(file);
            doc.write(fos);
            reportService.updateReportStatus(reportId, DataStatusEnum.SUCCESS.getValue(), null);
            log.info("报告生成中....成功...");
            return docName;
        } catch (Exception e) {
            log.error("报告生成失败：{}", e);
            reportService.updateReportStatus(reportId, DataStatusEnum.FAILED.getValue(), e.getMessage());
            throw new CustomMessageException("报告生成失败");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存报告生成记录
     *
     * @param query 生成参数
     * @param path  文档路径
     * @param type  生成方式
     * @return
     */
    private Long saveReport(ReportQueryBO query, String path, Integer type) {
        BizReportDO report = new BizReportDO();
        long range = DateUtils.until(query.getStartTime(), query.getEndTime(), ChronoUnit.DAYS) + 1;
        setReportPeriod(report, type, (int) range);
        //生成方式
        report.setReportType(type);
        report.setSubSystemId(query.getSubSystemId());
        if (Objects.nonNull(query.getDeviceId())) {
            report.setDeviceId(query.getDeviceId());
        }
        report.setReportPath(path);
        report.setReportTime(new Date());
        report.setDeviceName(query.getDeviceName());
        report.setReportEndTime(new Date(query.getEndTime()));
        report.setReportStartTime(new Date(query.getStartTime()));
        report.setStatus(DataStatusEnum.RUNNING.getValue());
        report.setReportName(StringUtils.hasText(query.getReportName()) ? query.getReportName() : range + "天自定义报告");
        reportService.save(report);
        return report.getId();
    }

    /**
     * 设置生成周期
     *
     * @param report 报告实体
     * @param type   报告类型
     * @param range  时间段
     */
    private void setReportPeriod(BizReportDO report, Integer type, Integer range) {
        if (type == 2) {
            //自定义
            report.setReportPeriod(3);
            return;
        }
        List<Integer> month = Lists.newArrayList(28, 29, 30, 31);
        if (month.contains(range)) {
            //按照月
            report.setReportPeriod(1);
            return;
        }
        //按照季度
        report.setReportPeriod(2);
    }

    private BizReportDO findReport(ReportQueryBO query) {
        return reportService.getReport(query);
    }
}
