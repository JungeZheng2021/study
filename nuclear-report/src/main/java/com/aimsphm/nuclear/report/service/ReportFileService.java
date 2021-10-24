package com.aimsphm.nuclear.report.service;

import com.aimsphm.nuclear.common.constant.ReportConstant;
import com.aimsphm.nuclear.common.entity.BizReportConfigDO;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.vo.EventDataVO;
import com.aimsphm.nuclear.report.enums.ReportCategoryEnum;
import com.aimsphm.nuclear.report.service.impl.ReportFileServiceImpl;
import com.aimsphm.nuclear.report.util.UUIDUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.aimsphm.nuclear.common.constant.ReportConstant.*;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/6/12 14:46
 */
public interface ReportFileService {
    Logger log = LoggerFactory.getLogger(ReportFileService.class);

    ThreadLocal<String> markLinesData = new ThreadLocal<>();

    /**
     * 导出到指定文件
     *
     * @param config 测点配置
     * @param data   数据
     * @param point
     * @return返回html路径
     */
    default String exportToHtml(BizReportConfigDO config, Object data, CommonMeasurePointDO point) {
        List<String> lines = readLines(config, data, point);
        if (CollectionUtils.isEmpty(lines)) {
            return null;
        }
        //写入文件
        File html = new File(ECHARTS_TEMP_DIR + UUIDUtils.randomUUID() + ECHARTS_HTML_SUFFIX);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(html), StandardCharsets.UTF_8);) {

            for (String l : lines) {
                writer.write(l + "\n");
            }
        } catch (Exception e) {
            log.error("生成html失败：{}", e);
        }
        //处理
        try {
            return html.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 替换文本
     *
     * @param config 测点配置
     * @param data   数据
     * @param point
     * @return 返回替换占位符之后的文本
     */
    default List<String> readLines(BizReportConfigDO config, Object data, CommonMeasurePointDO point) {
        InputStream is = null;
        InputStreamReader iReader = null;
        BufferedReader bufferedReader = null;
        List<String> lines = new ArrayList<>();
        String line;
        try {
            is = selectInputStreamByCategory(config.getCategory());
            boolean isChangeXAxisType = org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase(config.getCategory() + BLANK
                    , ReportCategoryEnum.LINE_WAVEFORM.getCategory() + BLANK
                    , ReportCategoryEnum.LINE_SPECTRUM.getCategory() + BLANK);
            iReader = new InputStreamReader(is, "UTF-8");
            bufferedReader = new BufferedReader(iReader);
            while ((line = bufferedReader.readLine()) != null) {
                if (!StringUtils.hasText(line)) {
                    continue;
                }
                //名称
                if (line.contains(HTML_TITLE)) {
                    line = line.replace(HTML_TITLE, StringUtils.hasText(config.getTitle()) ? config.getTitle() : BLANK);
                }
                //y轴单位
                if (line.contains(HTML_Y_UNIT)) {
                    line = line.replace(HTML_Y_UNIT, StringUtils.hasText(config.getYUnit()) ? config.getYUnit() : "%");
                }
                //x坐标
                if (line.contains(HTML_X_AXIS_DATA)) {
                    line = line.replace(HTML_X_AXIS_DATA, StringUtils.hasText(config.getXAxisData()) ? JSON.toJSONString(config.getXAxisData().split(",")) : "[]");
                }
                //颜色表
                if (line.contains(HTML_COLOR)) {
                    line = line.replace(HTML_COLOR, StringUtils.hasText(config.getColor()) ? JSON.toJSONString(config.getColor().split(",")) : "[]");
                }
                //图例
                if (line.contains(HTML_SERIES_DATA_NAME)) {
                    line = line.replace(HTML_SERIES_DATA_NAME, StringUtils.hasText(config.getLegend()) ? JSON.toJSONString(config.getLegend().split(",")) : "[]");
                }
                //数据
                if (line.contains(HTML_SERIES_DATA)) {
                    line = line.replace(HTML_SERIES_DATA, JSON.toJSONString(data));
                }
                //阈值线
                if (line.contains(HTML_MARK_LINES_DATA)) {
                    String markLines = markLinesData.get();
                    if (Objects.isNull(point) && StringUtils.hasText(markLines)) {
                        line = line.replace(HTML_MARK_LINES_DATA, markLines);
                    } else {
                        line = line.replace(HTML_MARK_LINES_DATA, Objects.nonNull(point) ? JSON.toJSONString(Lists.newArrayList(point.getThresholdLower(), point.getThresholdLow(), point.getThresholdHigh(), point.getThresholdHigher())) : "[]");
                    }
                }
                //发电机动态阈值图数据
                if (line.contains(HTML_OBJECT_DATA) || line.contains(HTML_ALARM_TYPE)) {
                    EventDataVO vo = (EventDataVO) data;
                    line = line.replace(HTML_ALARM_TYPE, Objects.isNull(vo) || Objects.isNull(vo.getAlarmType()) ? "5" : vo.getAlarmType() + BLANK);
                    line = line.replace(HTML_OBJECT_DATA, JSON.toJSONString(data));
                }
                //修改单位
                if (line.contains("type: 'time',") && isChangeXAxisType) {
                    line = line.replace("type: 'time',", BLANK);
                }
                lines.add(line);
            }
            return lines;
        } catch (Exception e) {
            log.error("替换占位符失败：{}", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("资源关闭失败：{}", e);
                }
            }
        }
        return null;
    }

    /**
     * 根据类型获取对应的html文件
     *
     * @param category 配置建
     * @return
     * @throws FileNotFoundException
     */
    default InputStream selectInputStreamByCategory(Integer category) throws FileNotFoundException {
        String root = "D:\\Java\\workspace\\nuclear_power\\nuclear-report\\src\\main\\resources\\templates\\";
        String dir = ReportConstant.PROJECT_TEMPLATES_ROOT_DIR;
        //折线图
        if (org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase(category + BLANK
                , ReportCategoryEnum.LINE.getCategory() + BLANK
                , ReportCategoryEnum.LINE_NONE_PI.getCategory() + BLANK,
                ReportCategoryEnum.LINE_WAVEFORM.getCategory() + BLANK, ReportCategoryEnum.LINE_SPECTRUM.getCategory() + BLANK)) {
            InputStream is = ReportFileServiceImpl.class.getResourceAsStream(dir + ReportConstant.TEMPLATE_LINE_HTML_NAME);
            if (is == null) {
                return new FileInputStream(new File(root + ReportConstant.TEMPLATE_LINE_HTML_NAME));
            }
            return is;
        }
        //多Y轴
        if (ReportCategoryEnum.LINE_MULTI_Y.getCategory().equals(category)) {
            InputStream is = ReportFileServiceImpl.class.getResourceAsStream(dir + ReportConstant.TEMPLATE_LINE_MULTI_Y_HTML_NAME);
            if (is == null) {
                return new FileInputStream(new File(root + ReportConstant.TEMPLATE_LINE_MULTI_Y_HTML_NAME));
            }
            return is;
        }
        //柱状图
        if (ReportCategoryEnum.BAR.getCategory().equals(category)) {
            InputStream is = ReportFileServiceImpl.class.getResourceAsStream(dir + ReportConstant.TEMPLATE_BAR_HTML_NAME);
            if (is == null) {
                return new FileInputStream(new File(root + ReportConstant.TEMPLATE_BAR_HTML_NAME));
            }
            return is;
        }
        //饼状图
        if (ReportCategoryEnum.PIE.getCategory().equals(category)) {
            InputStream is = ReportFileServiceImpl.class.getResourceAsStream(dir + ReportConstant.TEMPLATE_PIE_HTML_NAME);
            if (is == null) {
                return new FileInputStream(new File(root + ReportConstant.TEMPLATE_PIE_HTML_NAME));
            }
            return is;
        }
        //动态阈值图
        if (ReportCategoryEnum.LINE_DYNAMIC_THRESHOLD.getCategory().equals(category)) {
            InputStream is = ReportFileServiceImpl.class.getResourceAsStream(dir + ReportConstant.TEMPLATE_LINE_DYNAMIC_THRESHOLD);
            if (is == null) {
                return new FileInputStream(root + ReportConstant.TEMPLATE_LINE_DYNAMIC_THRESHOLD);
            }
            return is;
        }
        return null;
    }

    /**
     * 根据数据获取截图信息
     *
     * @param config    配置信息
     * @param data      数据
     * @param point     配置点
     * @param sleepTime 休眠时间
     * @return
     * @throws InterruptedException
     */
    File getImageFileWithData(BizReportConfigDO config, Object data, CommonMeasurePointDO point, long sleepTime) throws InterruptedException;

    /**
     * 根据数据获取截图信息
     *
     * @param config 配置信息
     * @param data   数据
     * @param point  配置点
     * @return
     * @throws InterruptedException
     */
    default File getImageFileWithData(BizReportConfigDO config, Object data, CommonMeasurePointDO point) throws InterruptedException {
        return getImageFileWithData(config, data, point, 800L);
    }
}
