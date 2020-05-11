package com.milla.report.util;


import com.github.abel533.echarts.code.Symbol;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Scatter;
import com.google.common.collect.Lists;
import com.milla.report.pojo.bo.ReportQueryBO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.util.ImageUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openqa.selenium.OutputType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

/**
 * @Package: com.milla.report.util
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/23 15:16
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/23 15:16
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Component
public class ReportUtils {
    @Autowired
    private ScreenshotUtils screenUtils;

    private static final String MS_FONT_FAMILY = "微软雅黑";
    //表头列表
    private static final List<String> titles = Lists.newArrayList("序号", "报警事件", "报警类型", "关联设备", "异常程度", "发生次数", "最近报警", "异常测点", "异常测点数");
    //图片缩放比例
    private static final double IMAGE_RATE = 0.58;
//
//
//    public static void main(String[] args) throws Exception {
//
//
//        final String returnurl = "C:\\Users\\milla\\Desktop\\1-new.docx";
////poi/test.docx”;
//        final String templateurl = "C:\\Users\\milla\\Desktop\\一号机组主泵性能监测报告-模板V6.docx";
////poi/onepage.docx”;
//        InputStream is = new FileInputStream(new File(templateurl));
//        XWPFDocument doc = new XWPFDocument(is);
//        new ReportUtils().operationPlaceholderInWord(doc, null);
////文件存在删除
//        try {
//            File file = new File(returnurl);
//            if (file.exists()) {
//                file.delete();
//            }
//            FileOutputStream fos = new FileOutputStream(returnurl);
//            doc.write(fos);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * @Description: 替换段落和表格中
     */
    public void operationPlaceholderInWord(XWPFDocument doc, ReportQueryBO query) throws Exception {
        /**----------------------------处理段落------------------------------------**/
        List<XWPFParagraph> paragraphList = doc.getParagraphs();
        if (Objects.isNull(paragraphList) || paragraphList.isEmpty()) {
            return;
        }
//TODO 获取数据        List<List<List<MeasurePointTimesScaleVO>>> pumpPieData = null ;//TODO 获取数据 service.getPumpPieData(query);
        ListIterator<XWPFParagraph> it = paragraphList.listIterator();
        for (; it.hasNext(); ) {
            XWPFParagraph paragraph = it.next();
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                if (text == null || text.length() == 0 || !text.startsWith("#")) {
                    continue;
                }
                System.out.println("-----------start---------");
                System.out.println(text);
                System.out.println("-----------end..---------");
                System.out.println();

                //开始时间
                if (text.contains("#start#")) {
                    run.setText(DateUtils.format(DateUtils.YEAR_MONTH_DAY_ZH, query.getStartTime()), 0);
                    continue;
                }
                //结束时间
                if (text.contains("#end#")) {
                    run.setText(DateUtils.format(DateUtils.YEAR_MONTH_DAY_ZH, query.getEndTime()), 0);
                    continue;
                }
                //主泵健康情况-增加图片
                if (text.contains("#pumpHealth#")) {
                    addPumpHealthPictures(run, query);
                    continue;
                }
                //报警趋势统计
                if (text.contains("#alarmTrend#")) {
                    addAlarmStatisticsPicture(run, query);
                    continue;
                }
                //报警类型
                if (text.contains("#alarmType1A#")) {
//                    addAlarmTypePictures(run, "1A", pumpPieData.get(0));
                    continue;
                }
                if (text.contains("#alarmType1B#")) {
//                    addAlarmTypePictures(run, "1B", pumpPieData.get(1));
                    continue;
                }
                if (text.contains("#alarmType2A#")) {
//                    addAlarmTypePictures(run, "2A", pumpPieData.get(2));
                    continue;
                }
                if (text.contains("#alarmType2B#")) {
//                    addAlarmTypePictures(run, "2B", pumpPieData.get(3));
                    continue;
                }
                //趋势图
                if (text.contains("#trendPower#")) {
                    addTrendPowerPictures(run, "10DDS-PPP24-J1", query);
                    continue;
                }
                //趋势图流量
                if (text.contains("#trendflow#")) {
                    addTrendPowerPictures(run, Lists.newArrayList("10ECS-EV-31A-OPWR", "10DDS-PPP24-J1"), query);
                    continue;
                }
                if (text.contains("#alarmEvent#")) {
                    addAlarmEventTable(run, paragraph, doc, query);
                }
            }
        }
    }

    private void addAlarmEventTable(XWPFRun run, XWPFParagraph paragraph, XWPFDocument doc, ReportQueryBO query) {
        run.setText("", 0);
        XmlCursor cursor = paragraph.getCTP().newCursor();
        XWPFTable table = doc.insertNewTbl(cursor);
        List<Object[]> options = null;// TODO  获取数据null ;//TODO 获取数据 service.getPumpAlarmEventData(query);
        initTable(table, titles, options.toArray(new Object[]{}));
    }

    private void initTable(XWPFTable table, List<String> heads, Object[] options) {
        initTableTHead(table, heads);
        for (int m = 0, size = options.length; m < size; m++) {
            Object[] data = (Object[]) options[m];
            XWPFTableRow row = table.createRow();
            for (int i = 0, len = heads.size(); i < len; i++) {
                XWPFTableCell cell;
                cell = row.getCell(i);
                if (i == 0) {
                    setCellStyle(cell, 0, "#000000", m % 2 == 0 ? "#D9E2F3" : "#B4C6E7", String.valueOf((m + 1)));
                    continue;
                }
                setCellStyle(cell, 0, "#000000", m % 2 == 0 ? "#D9E2F3" : "#B4C6E7", String.valueOf(data[i - 1]));
            }
        }

    }

    private void initTableTHead(XWPFTable table, List<String> heads) {
        //第一行
        XWPFTableRow row = table.getRow(0);
        for (int i = 0, len = heads.size(); i < len; i++) {
            XWPFTableCell cell;
            if (i == 0) {
                cell = row.getCell(0);
            } else {
                cell = row.createCell();
            }
            setCellStyle(cell, 1, "#ffffff", "#4472C4", heads.get(i));
        }
    }

    private void addTrendPowerPictures(XWPFRun run, String tag, ReportQueryBO query) throws Exception {
        run.setText("", 0);//将占位符隐藏
        Map<String, List<Object[]>> data = null;//TODO 获取数据 service.getPumpTrendLineData(Lists.newArrayList(tag), query);
        List<Object[]> objects = data.get(tag);
        String htmlPath = EChartsHtmlUtils.lineEChartsHtml(objects.toArray(new Object[]{}));//获取趋势图片
        File file = screenUtils.getScreenshotAs(htmlPath, OutputType.FILE, 800L);
        addPicture(run, file);
        run.setText(tag + "特征放大图", -1);
    }

    private void addTrendPowerPictures(XWPFRun run, List<String> tagList, ReportQueryBO query) throws Exception {
        run.setText("", 0);//将占位符隐藏
        Map<String, List<Object[]>> data = null;//TODO 获取数据 service.getPumpTrendLineData(tagList, query);
        if (MapUtils.isEmpty(data)) {
            return;
        }
        Set<Map.Entry<String, List<Object[]>>> entries = data.entrySet();
        Iterator<Map.Entry<String, List<Object[]>>> it = entries.iterator();
        List<Line> list = Lists.newArrayList();
        for (; it.hasNext(); ) {
            Map.Entry<String, List<Object[]>> next = it.next();
            String tag = next.getKey();
            List<Object[]> value = next.getValue();
            Line line = new Line(tag);
            line.symbol(Symbol.none).data(value.toArray(new Object[]{}));
            list.add(line);
        }
        String htmlPath = EChartsHtmlUtils.lineEChartsHtmlMultiterm(list);//获取趋势图片
        File file = screenUtils.getScreenshotAs(htmlPath, OutputType.FILE, 800L);
        addPicture(run, file);
        run.setText("特征放大图", -1);
    }


    /**
     * 添加报警趋势统计图片
     *
     * @param query 查询条件
     * @param run   段落
     * @throws Exception
     */
    private void addAlarmStatisticsPicture(XWPFRun run, ReportQueryBO query) throws Exception {
        List<Bar> options = null;//TODO 获取数据 service.getPumpBarData(query);
        if (CollectionUtils.isEmpty(options)) {
            return;
        }
        long days = DateUtils.until(query.getStartTime(), query.getEndTime(), ChronoUnit.DAYS);
        String htmlPath = EChartsHtmlUtils.barEChartsHtml(options, Lists.newArrayList("title1", "title2"));
        run.setText("", 0);//将占位符隐藏
        File file = screenUtils.getScreenshotAs(htmlPath, OutputType.FILE, 800L);
        addPicture(run, file);
        run.setText("1A主泵设备状态变化", -1);
    }

    /**
     * 添加主泵健康信息图片
     *
     * @param query
     * @param run
     * @throws Exception
     */
    private void addPumpHealthPictures(XWPFRun run, ReportQueryBO query) throws Exception {
        Map<String, List<Scatter>> scatterData = null;//TODO 获取数据 service.getPumpScatterData(query);
        if (MapUtils.isEmpty(scatterData)) {
            return;
        }
        run.setText("", 0);//将占位符隐藏
        Set<Map.Entry<String, List<Scatter>>> entries = scatterData.entrySet();
        Iterator<Map.Entry<String, List<Scatter>>> it = entries.iterator();
        for (; it.hasNext(); ) {
            Map.Entry<String, List<Scatter>> next = it.next();
            String deviceName = next.getKey();
            List<Scatter> value = next.getValue();
            if (StringUtils.isEmpty(deviceName) || CollectionUtils.isEmpty(value)) {
                continue;
            }
            String htmlPath = EChartsHtmlUtils.scatterEChartsHtml(value, "");
            File file = screenUtils.getScreenshotAs(htmlPath, OutputType.FILE, 800L);
            addPicture(run, file);
            run.setText(deviceName + "设备状态变化", -1);

        }
    }

    /**
     * 添加图片到指定位置
     *
     * @param run  指定段落
     * @param file 文件
     * @throws Exception
     */
    public void addPicture(XWPFRun run, File file) throws Exception {
        //获取图片的大小，在word中进行等比缩放
        Dimension dimension = ImageUtils.getImageDimension(new FileInputStream(file), 7);
        //添加图片到文档中
        run.addPicture(new FileInputStream(file), XWPFDocument.PICTURE_TYPE_PNG, file.getName(), Units.toEMU(dimension.width * IMAGE_RATE), Units.toEMU(dimension.getHeight() * IMAGE_RATE));
    }

    private static final BigDecimal bd2 = new BigDecimal("2");

    private static void setCellStyle(XWPFTableCell cell, int fontBold, String fontColor, String bgColor, String content) {
        setCellStyle(cell, MS_FONT_FAMILY, "9", fontBold, "left", "top", fontColor, bgColor, content);
    }

    private static void setCellStyle(XWPFTableCell cell, String fontName, String fontSize, int fontBold,
                                     String alignment, String vertical, String fontColor,
                                     String bgColor, String content) {

        //poi对字体大小设置特殊，不支持小数，但对原word字体大小做了乘2处理
        BigInteger bFontSize = new BigInteger("24");
        if (fontSize != null && !fontSize.equals("")) {
            //poi对字体大小设置特殊，不支持小数，但对原word字体大小做了乘2处理
            BigDecimal fontSizeBD = new BigDecimal(fontSize);
            fontSizeBD = bd2.multiply(fontSizeBD);
            fontSizeBD = fontSizeBD.setScale(0, BigDecimal.ROUND_HALF_UP);//这里取整
            bFontSize = new BigInteger(fontSizeBD.toString());// 字体大小
        }
        //=====获取单元格
        CTTc tc = cell.getCTTc();
        //====tcPr开始====》》》》
        CTTcPr tcPr = tc.getTcPr();//获取单元格里的<w:tcPr>
        if (tcPr == null) {//没有<w:tcPr>，创建
            tcPr = tc.addNewTcPr();
        }

        //  --vjc开始-->>
        CTVerticalJc vjc = tcPr.getVAlign();//获取<w:tcPr>  的<w:vAlign w:val="center"/>
        if (vjc == null) {//没有<w:w:vAlign/>，创建
            vjc = tcPr.addNewVAlign();
        }
        //设置单元格对齐方式
        vjc.setVal(vertical.equals("top") ? STVerticalJc.TOP : vertical.equals("bottom") ? STVerticalJc.BOTTOM : STVerticalJc.CENTER); //垂直对齐

        CTShd shd = tcPr.getShd();//获取<w:tcPr>里的<w:shd w:val="clear" w:color="auto" w:fill="C00000"/>
        if (shd == null) {//没有<w:shd>，创建
            shd = tcPr.addNewShd();
        }
        // 设置背景颜色
        shd.setFill(bgColor.substring(1));
        //《《《《====tcPr结束====

        //====p开始====》》》》
        CTP p = tc.getPList().get(0);//获取单元格里的<w:p w:rsidR="00C36068" w:rsidRPr="00B705A0" w:rsidRDefault="00C36068" w:rsidP="00C36068">

        //---ppr开始--->>>
        CTPPr ppr = p.getPPr();//获取<w:p>里的<w:pPr>
        if (ppr == null) {//没有<w:pPr>，创建
            ppr = p.addNewPPr();
        }
        //  --jc开始-->>
        CTJc jc = ppr.getJc();//获取<w:pPr>里的<w:jc w:val="left"/>
        if (jc == null) {//没有<w:jc/>，创建
            jc = ppr.addNewJc();
        }
        //设置单元格对齐方式
        jc.setVal(alignment.equals("left") ? STJc.LEFT : alignment.equals("right") ? STJc.RIGHT : STJc.CENTER); //水平对齐
        //  <<--jc结束--
        //  --pRpr开始-->>
        CTParaRPr pRpr = ppr.getRPr(); //获取<w:pPr>里的<w:rPr>
        if (pRpr == null) {//没有<w:rPr>，创建
            pRpr = ppr.addNewRPr();
        }
        CTFonts pFont = pRpr.getRFonts();//获取<w:rPr>里的<w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体"/>
        if (pFont == null) {//没有<w:rPr>，创建
            pFont = pRpr.addNewRFonts();
        }
        //设置字体
        pFont.setAscii(fontName);
        pFont.setEastAsia(fontName);
        pFont.setHAnsi(fontName);

        CTOnOff pb = pRpr.getB();//获取<w:rPr>里的<w:b/>
        if (pb == null) {//没有<w:b/>，创建
            pb = pRpr.addNewB();
        }
        //设置字体是否加粗
        pb.setVal(fontBold == 1 ? STOnOff.ON : STOnOff.OFF);

        CTHpsMeasure psz = pRpr.getSz();//获取<w:rPr>里的<w:sz w:val="32"/>
        if (psz == null) {//没有<w:sz w:val="32"/>，创建
            psz = pRpr.addNewSz();
        }
        // 设置单元格字体大小
        psz.setVal(bFontSize);
        CTHpsMeasure pszCs = pRpr.getSzCs();//获取<w:rPr>里的<w:szCs w:val="32"/>
        if (pszCs == null) {//没有<w:szCs w:val="32"/>，创建
            pszCs = pRpr.addNewSzCs();
        }
        // 设置单元格字体大小
        pszCs.setVal(bFontSize);
        //  <<--pRpr结束--
        //<<<---ppr结束---

        //---r开始--->>>
        List<CTR> rlist = p.getRList(); //获取<w:p>里的<w:r w:rsidRPr="00B705A0">
        CTR r = null;
        if (rlist != null && rlist.size() > 0) {//获取第一个<w:r>
            r = rlist.get(0);
        } else {//没有<w:r>，创建
            r = p.addNewR();
        }
        //--rpr开始-->>
        CTRPr rpr = r.getRPr();//获取<w:r w:rsidRPr="00B705A0">里的<w:rPr>
        if (rpr == null) {//没有<w:rPr>，创建
            rpr = r.addNewRPr();
        }
        //->-
        CTFonts font = rpr.getRFonts();//获取<w:rPr>里的<w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
        if (font == null) {//没有<w:rFonts>，创建
            font = rpr.addNewRFonts();
        }
        //设置字体
        font.setAscii(fontName);
        font.setEastAsia(fontName);
        font.setHAnsi(fontName);

        CTOnOff b = rpr.getB();//获取<w:rPr>里的<w:b/>
        if (b == null) {//没有<w:b/>，创建
            b = rpr.addNewB();
        }
        //设置字体是否加粗
        b.setVal(fontBold == 1 ? STOnOff.ON : STOnOff.OFF);
        CTColor color = rpr.getColor();//获取<w:rPr>里的<w:color w:val="FFFFFF" w:themeColor="background1"/>
        if (color == null) {//没有<w:color>，创建
            color = rpr.addNewColor();
        }
        // 设置字体颜色
        if (content.contains("↓")) {
            color.setVal("43CD80");
        } else if (content.contains("↑")) {
            color.setVal("943634");
        } else {
            color.setVal(fontColor.substring(1));
        }
        CTHpsMeasure sz = rpr.getSz();
        if (sz == null) {
            sz = rpr.addNewSz();
        }
        sz.setVal(bFontSize);
        CTHpsMeasure szCs = rpr.getSzCs();
        if (szCs == null) {
            szCs = rpr.addNewSz();
        }
        szCs.setVal(bFontSize);
        //<<--rpr结束--
        List<CTText> tList = r.getTList();
        CTText t = null;
        if (tList != null && tList.size() > 0) {//获取第一个<w:r>
            t = tList.get(0);
        } else {//没有<w:r>，创建
            t = r.addNewT();
        }
        t.setStringValue(content);
        //<<<---r结束---
    }
}
