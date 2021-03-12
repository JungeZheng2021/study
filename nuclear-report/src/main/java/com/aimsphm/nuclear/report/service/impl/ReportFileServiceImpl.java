package com.aimsphm.nuclear.report.service.impl;

import com.aimsphm.nuclear.common.entity.BizReportConfigDO;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.report.service.ReportFileService;
import com.aimsphm.nuclear.report.util.ScreenshotUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @Package: com.aimsphm.nuclear.report.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/6/12 14:47
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/12 14:47
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service
public class ReportFileServiceImpl implements ReportFileService {

    private ScreenshotUtils screenUtils;

    public ReportFileServiceImpl(ScreenshotUtils screenUtils) {
        this.screenUtils = screenUtils;
    }

    /**
     * 获取图片文件
     *
     * @param config    配置信息
     * @param data      要展示的数据 List<List<List<Object>>> 折线图  List<List<Double>> 柱状图
     * @param point     需要展示的阈值等信息
     * @param sleepTime 截图需要的等待时间
     * @return
     * @throws InterruptedException
     */
    @Override
    public File getImageFileWithData(BizReportConfigDO config, Object data, CommonMeasurePointDO point, long sleepTime) throws InterruptedException {
        String htmlPath = exportToHtml(config, data, point);
        return screenUtils.getScreenshotAs(htmlPath, OutputType.FILE, sleepTime);
    }

    public static void main(String[] args) throws InterruptedException {
        BizReportConfigDO config = new BizReportConfigDO();
        config.setColor("#ff0000");
        config.setXAxisData("一天,七天,一个月,三个月");
        config.setYUnit("次");
        config.setCategory(2);
        config.setTitle("我是测试");
        ReportFileServiceImpl service = new ReportFileServiceImpl(null);
        service.getImageFileWithData(config, Lists.newArrayList(1, 23, 34, 21, 2), null);
    }
}
