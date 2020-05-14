package com.milla.study.netbase.expert.poi;

/**
 * @Package: com.milla.study.netbase.expert
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/23 16:01
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/23 16:01
 * @UpdateRemark: <>
 * @Version: 1.0
 */


import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;


/**
 * poi生成word的工具类
 * 针对于模板中的图表是静态的，也就是模板中的图表长什么样子不会根据数据而改变
 */
public class PoiWordTools {

    private static final BigDecimal bd2 = new BigDecimal("2");


    /**
     * 调用替换柱状图数据
     */
    public static void replaceBarCharts(POIXMLDocumentPart poixmlDocumentPart,
                                        List<String> titleArr, List<String> fldNameArr, List<Map<String, String>> listItemsByType) {
        XWPFChart chart = (XWPFChart) poixmlDocumentPart;
        chart.getCTChart();

        //根据属性第一列名称切换数据类型
        CTChart ctChart = chart.getCTChart();
        CTPlotArea plotArea = ctChart.getPlotArea();

        CTBarChart barChart = plotArea.getBarChartArray(0);
        List<CTBarSer> BarSerList = barChart.getSerList();  // 获取柱状图单位

        //刷新内置excel数据
        new PoiWordTools().refreshExcel(chart, listItemsByType, fldNameArr, titleArr);
        //刷新页面显示数据
        refreshBarStrGraphContent(barChart, BarSerList, listItemsByType, fldNameArr, 1);
    }


    /**
     * 调用替换折线图数据
     */
    public static void replaceLineCharts(POIXMLDocumentPart poixmlDocumentPart,
                                         List<String> titleArr, List<String> fldNameArr, List<Map<String, String>> listItemsByType) {
        XWPFChart chart = (XWPFChart) poixmlDocumentPart;
        chart.getCTChart();

        //根据属性第一列名称切换数据类型
        CTChart ctChart = chart.getCTChart();
        CTPlotArea plotArea = ctChart.getPlotArea();

        CTLineChart lineChart = plotArea.getLineChartArray(0);
        List<CTLineSer> lineSerList = lineChart.getSerList();   // 获取折线图单位

        //刷新内置excel数据
        new PoiWordTools().refreshExcel(chart, listItemsByType, fldNameArr, titleArr);
        //刷新页面显示数据
        new PoiWordTools().refreshLineStrGraphContent(lineChart, lineSerList, listItemsByType, fldNameArr, 1);

    }


    /**
     * 调用替换饼图数据
     */
    public static void replacePieCharts(POIXMLDocumentPart poixmlDocumentPart,
                                        List<String> titleArr, List<String> fldNameArr, List<Map<String, String>> listItemsByType) {
        XWPFChart chart = (XWPFChart) poixmlDocumentPart;
        chart.getCTChart();

        //根据属性第一列名称切换数据类型
        CTChart ctChart = chart.getCTChart();
        CTPlotArea plotArea = ctChart.getPlotArea();

        CTPieChart pieChart = plotArea.getPieChartArray(0);
        List<CTPieSer> pieSerList = pieChart.getSerList();  // 获取饼图单位

        //刷新内置excel数据
        new PoiWordTools().refreshExcel(chart, listItemsByType, fldNameArr, titleArr);
        //刷新页面显示数据
        new PoiWordTools().refreshPieStrGraphContent(pieChart, pieSerList, listItemsByType, fldNameArr, 1);

    }


    /**
     * 调用替换柱状图、折线图组合数据
     */
    public static void replaceCombinationCharts(POIXMLDocumentPart poixmlDocumentPart,
                                                List<String> titleArr, List<String> fldNameArr, List<Map<String, String>> listItemsByType) {
        XWPFChart chart = (XWPFChart) poixmlDocumentPart;
        chart.getCTChart();

        //根据属性第一列名称切换数据类型
        CTChart ctChart = chart.getCTChart();
        CTPlotArea plotArea = ctChart.getPlotArea();


        CTBarChart barChart = plotArea.getBarChartArray(0);
        List<CTBarSer> barSerList = barChart.getSerList();  // 获取柱状图单位
        //刷新内置excel数据
        new PoiWordTools().refreshExcel(chart, listItemsByType, fldNameArr, titleArr);
        //刷新页面显示数据   数据中下标1开始的是柱状图数据，所以这个是1
        refreshBarStrGraphContent(barChart, barSerList, listItemsByType, fldNameArr, 1);


        CTLineChart lineChart = plotArea.getLineChartArray(0);
        List<CTLineSer> lineSerList = lineChart.getSerList();   // 获取折线图单位
        //刷新内置excel数据   有一个就可以了    有一个就可以了    有一个就可以了
        //new PoiWordTools().refreshExcel(chart, listItemsByType, fldNameArr, titleArr);
        //刷新页面显示数据   数据中下标3开始的是折线图的数据，所以这个是3
        new PoiWordTools().refreshLineStrGraphContent(lineChart, lineSerList, listItemsByType, fldNameArr, 3);

    }


    /**
     * 刷新折线图数据方法
     *
     * @param typeChart
     * @param serList
     * @param dataList
     * @param fldNameArr
     * @param position
     * @return
     */
    public static boolean refreshLineStrGraphContent(Object typeChart,
                                                     List<?> serList, List<Map<String, String>> dataList, List<String> fldNameArr, int position) {

        boolean result = true;
        //更新数据区域
        for (int i = 0; i < serList.size(); i++) {
            //CTSerTx tx=null;
            CTAxDataSource cat = null;
            CTNumDataSource val = null;
            CTLineSer ser = ((CTLineChart) typeChart).getSerArray(i);
            //tx= ser.getTx();
            // Category Axis Data
            cat = ser.getCat();
            // 获取图表的值
            val = ser.getVal();
            // strData.set
            CTStrData strData = cat.getStrRef().getStrCache();
            CTNumData numData = val.getNumRef().getNumCache();
            strData.setPtArray((CTStrVal[]) null); // unset old axis text
            numData.setPtArray((CTNumVal[]) null); // unset old values

            // set model
            long idx = 0;
            for (int j = 0; j < dataList.size(); j++) {
                //判断获取的值是否为空
                String value = "0";
                if (new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))) != null) {
                    value = new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))).toString();
                }
                if (!"0".equals(value)) {
                    CTNumVal numVal = numData.addNewPt();//序列值
                    numVal.setIdx(idx);
                    numVal.setV(value);
                }
                CTStrVal sVal = strData.addNewPt();//序列名称
                sVal.setIdx(idx);
                sVal.setV(dataList.get(j).get(fldNameArr.get(0)));
                idx++;
            }
            numData.getPtCount().setVal(idx);
            strData.getPtCount().setVal(idx);


            //赋值横坐标数据区域
            String axisDataRange = new CellRangeAddress(1, dataList.size(), 0, 0)
                    .formatAsString("Sheet1", false);
            cat.getStrRef().setF(axisDataRange);

            //数据区域
            String numDataRange = new CellRangeAddress(1, dataList.size(), i + position, i + position)
                    .formatAsString("Sheet1", false);
            val.getNumRef().setF(numDataRange);

            // 设置系列生成方向


        }
        return result;
    }


    /**
     * 刷新柱状图数据方法
     *
     * @param typeChart
     * @param serList
     * @param dataList
     * @param fldNameArr
     * @param position
     * @return
     */
    public static boolean refreshBarStrGraphContent(Object typeChart,
                                                    List<?> serList, List<Map<String, String>> dataList, List<String> fldNameArr, int position) {
        boolean result = true;
        //更新数据区域
        for (int i = 0; i < serList.size(); i++) {
//            CTSerTx tx=null;
            CTAxDataSource cat = null;
            CTNumDataSource val = null;
            CTBarSer ser = ((CTBarChart) typeChart).getSerArray(i);
//            tx= ser.getTx();

            // Category Axis Data
            cat = ser.getCat();
            // 获取图表的值
            val = ser.getVal();
            // strData.set
            CTStrData strData = cat.getStrRef().getStrCache();
            CTNumData numData = val.getNumRef().getNumCache();
            strData.setPtArray((CTStrVal[]) null); // unset old axis text
            numData.setPtArray((CTNumVal[]) null); // unset old values

            // set model
            long idx = 0;
            for (int j = 0; j < dataList.size(); j++) {
                //判断获取的值是否为空
                String value = "0";
                if (new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))) != null) {
                    value = new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))).toString();
                }
                if (!"0".equals(value)) {
                    CTNumVal numVal = numData.addNewPt();//序列值
                    numVal.setIdx(idx);
                    numVal.setV(value);
                }
                CTStrVal sVal = strData.addNewPt();//序列名称
                sVal.setIdx(idx);
                sVal.setV(dataList.get(j).get(fldNameArr.get(0)));
                idx++;
            }
            numData.getPtCount().setVal(idx);
            strData.getPtCount().setVal(idx);


            //赋值横坐标数据区域
            String axisDataRange = new CellRangeAddress(1, dataList.size(), 0, 0)
                    .formatAsString("Sheet1", true);
            cat.getStrRef().setF(axisDataRange);

            //数据区域
            String numDataRange = new CellRangeAddress(1, dataList.size(), i + position, i + position)
                    .formatAsString("Sheet1", true);
            val.getNumRef().setF(numDataRange);

        }
        return result;
    }


    /**
     * 刷新饼图数据方法
     *
     * @param typeChart
     * @param serList
     * @param dataList
     * @param fldNameArr
     * @param position
     * @return
     */
    public static boolean refreshPieStrGraphContent(Object typeChart,
                                                    List<?> serList, List<Map<String, String>> dataList, List<String> fldNameArr, int position) {

        boolean result = true;
        //更新数据区域
        for (int i = 0; i < serList.size(); i++) {
            //CTSerTx tx=null;
            CTAxDataSource cat = null;
            CTNumDataSource val = null;
            CTPieSer ser = ((CTPieChart) typeChart).getSerArray(i);

            //tx= ser.getTx();
            // Category Axis Data
            cat = ser.getCat();
            // 获取图表的值
            val = ser.getVal();
            // strData.set
            CTStrData strData = cat.getStrRef().getStrCache();
            CTNumData numData = val.getNumRef().getNumCache();
            strData.setPtArray((CTStrVal[]) null); // unset old axis text
            numData.setPtArray((CTNumVal[]) null); // unset old values

            // set model
            long idx = 0;
            for (int j = 0; j < dataList.size(); j++) {
                //判断获取的值是否为空
                String value = "0";
                if (new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))) != null) {
                    value = new BigDecimal(dataList.get(j).get(fldNameArr.get(i + position))).toString();
                }
                if (!"0".equals(value)) {
                    CTNumVal numVal = numData.addNewPt();//序列值
                    numVal.setIdx(idx);
                    numVal.setV(value);
                }
                CTStrVal sVal = strData.addNewPt();//序列名称
                sVal.setIdx(idx);
                sVal.setV(dataList.get(j).get(fldNameArr.get(0)));
                idx++;
            }
            numData.getPtCount().setVal(idx);
            strData.getPtCount().setVal(idx);


            //赋值横坐标数据区域
            String axisDataRange = new CellRangeAddress(1, dataList.size(), 0, 0)
                    .formatAsString("Sheet1", true);
            cat.getStrRef().setF(axisDataRange);

            //数据区域
            String numDataRange = new CellRangeAddress(1, dataList.size(), i + position, i + position)
                    .formatAsString("Sheet1", true);
            val.getNumRef().setF(numDataRange);
        }
        return result;
    }


    /**
     * 刷新内置excel数据
     *
     * @param chart
     * @param dataList
     * @param fldNameArr
     * @param titleArr
     * @return
     */
    public static boolean refreshExcel(XWPFChart chart,
                                       List<Map<String, String>> dataList, List<String> fldNameArr, List<String> titleArr) {
        boolean result = true;
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        //根据数据创建excel第一行标题行
        for (int i = 0; i < titleArr.size(); i++) {
            if (sheet.getRow(0) == null) {
                sheet.createRow(0).createCell(i).setCellValue(titleArr.get(i) == null ? "" : titleArr.get(i));
            } else {
                sheet.getRow(0).createCell(i).setCellValue(titleArr.get(i) == null ? "" : titleArr.get(i));
            }
        }

        //遍历数据行
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> baseFormMap = dataList.get(i);//数据行
            //fldNameArr字段属性
            for (int j = 0; j < fldNameArr.size(); j++) {
                if (sheet.getRow(i + 1) == null) {
                    if (j == 0) {
                        try {
                            sheet.createRow(i + 1).createCell(j).setCellValue(baseFormMap.get(fldNameArr.get(j)) == null ? "" : baseFormMap.get(fldNameArr.get(j)));
                        } catch (Exception e) {
                            if (baseFormMap.get(fldNameArr.get(j)) == null) {
                                sheet.createRow(i + 1).createCell(j).setCellValue("");
                            } else {
                                sheet.createRow(i + 1).createCell(j).setCellValue(baseFormMap.get(fldNameArr.get(j)));
                            }
                        }
                    }
                } else {
                    BigDecimal b = new BigDecimal(baseFormMap.get(fldNameArr.get(j)));
                    double value = 0d;
                    if (b != null) {
                        value = b.doubleValue();
                    }
                    if (value == 0) {
                        sheet.getRow(i + 1).createCell(j);
                    } else {
                        sheet.getRow(i + 1).createCell(j).setCellValue(b.doubleValue());
                    }
                }
            }

        }
        // 更新嵌入的workbook
        POIXMLDocumentPart xlsPart = chart.getRelations().get(0);
        OutputStream xlsOut = xlsPart.getPackagePart().getOutputStream();

        try {
            wb.write(xlsOut);
            xlsOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    result = false;
                }
            }
        }
        return result;
    }


    /**
     * 设置表格样式
     *
     * @param cell
     * @param fontName
     * @param fontSize
     * @param fontBlod
     * @param alignment
     * @param vertical
     * @param fontColor
     * @param bgColor
     * @param cellWidth
     * @param content
     */
    public static void setWordCellSelfStyle(XWPFTableCell cell, String fontName, String fontSize, int fontBlod,
                                            String alignment, String vertical, String fontColor,
                                            String bgColor, String cellWidth, String content) {

        //poi对字体大小设置特殊，不支持小数，但对原word字体大小做了乘2处理
        BigInteger bFontSize = new BigInteger("24");
        if (fontSize != null && !fontSize.equals("")) {
            //poi对字体大小设置特殊，不支持小数，但对原word字体大小做了乘2处理
            BigDecimal fontSizeBD = new BigDecimal(fontSize);
            fontSizeBD = bd2.multiply(fontSizeBD);
            fontSizeBD = fontSizeBD.setScale(0, BigDecimal.ROUND_HALF_UP);//这里取整
            bFontSize = new BigInteger(fontSizeBD.toString());// 字体大小
        }

        // 设置单元格宽度
        cell.setWidth(cellWidth);

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
        CTFonts pfont = pRpr.getRFonts();//获取<w:rPr>里的<w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体"/>
        if (pfont == null) {//没有<w:rPr>，创建
            pfont = pRpr.addNewRFonts();
        }
        //设置字体
        pfont.setAscii(fontName);
        pfont.setEastAsia(fontName);
        pfont.setHAnsi(fontName);

        CTOnOff pb = pRpr.getB();//获取<w:rPr>里的<w:b/>
        if (pb == null) {//没有<w:b/>，创建
            pb = pRpr.addNewB();
        }
        //设置字体是否加粗
        pb.setVal(fontBlod == 1 ? STOnOff.ON : STOnOff.OFF);

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
        b.setVal(fontBlod == 1 ? STOnOff.ON : STOnOff.OFF);
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
        //-<-
        //<<--rpr结束--
        List<CTText> tlist = r.getTList();
        CTText t = null;
        if (tlist != null && tlist.size() > 0) {//获取第一个<w:r>
            t = tlist.get(0);
        } else {//没有<w:r>，创建
            t = r.addNewT();
        }
        t.setStringValue(content);
        //<<<---r结束---
    }


    /**
     * 获取内置表格数据，拿到第一行第一列格子数据
     * 有时候模板设计太复杂，对于图表不能精准定位，可以通过设置图表表格数据的第一行第一列格子数据来区分，这个数据不影响图表显示，所以用来区分每个图表
     */
    public static String getZeroData(POIXMLDocumentPart poixmlDocumentPart) {
        String text = "";
        try {
            XWPFChart chart = (XWPFChart) poixmlDocumentPart;
            chart.getCTChart();

            POIXMLDocumentPart xlsPart = chart.getRelations().get(0);
            InputStream xlsin = xlsPart.getPackagePart().getInputStream();

            Workbook workbook = new XSSFWorkbook(xlsin);
            // 获取第一个sheet
            Sheet sheet = workbook.getSheetAt(0);
            // 第一行
            Row row = sheet.getRow(0);
            // 第一列
            Cell cell = row.getCell(0);

            cell.setCellType(CellType.STRING);  // 设置一下格子类型为字符串，不然如果是数字或者时间的话，获取很麻烦
            text = cell.getStringCellValue();   // 获取格子内容

            System.out.println("(0,0)格子值：" + text);

            // 关闭流
            xlsin.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return text;
    }


}
