package com.milla.study.netbase.expert;


import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * @Package: com.aimphm.nuclear.opc
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/23 15:16
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/23 15:16
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class PoiDemoWordTable {
    public static void main(String[] args) throws Exception {


        final String returnurl = "C:\\Users\\milla\\Desktop\\1-new.docx";
//poi/test.docx”;
        final String templateurl = "C:\\Users\\milla\\Desktop\\1.docx";
//poi/onepage.docx”;
        InputStream is = new FileInputStream(new File(templateurl));
        XWPFDocument doc = new XWPFDocument(is);
        replaceAll(doc);
//文件存在删除
        try {
            File file = new File(returnurl);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(returnurl);
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 替换段落和表格中
     */
    public static void replaceAll(XWPFDocument doc) throws InvalidFormatException, FileNotFoundException, IOException {
        /**----------------------------处理段落------------------------------------**/
        List<XWPFParagraph> paragraphList = doc.getParagraphs();
        if (Objects.isNull(paragraphList) || paragraphList.isEmpty()) {
            return;
        }
        for (XWPFParagraph paragraph : paragraphList) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                if (text == null || text.length() == 0) {
                    continue;
                }
                if (text.contains("${table}")) {
                    run.setText("", 0);
                    XmlCursor cursor = paragraph.getCTP().newCursor();
                    XWPFTable tableOne = doc.insertNewTbl(cursor);// ---这个是关键
                    XWPFTableRow tableOneRowOne = tableOne.getRow(0);//行
                    setWordCellSelfStyle(tableOneRowOne.getCell(0), "微软雅黑", "9", 1, "left", "top", "#ffffff", "#4472C4", 1, "序号");
                    XWPFTableCell cell12 = tableOneRowOne.createCell();
                    setWordCellSelfStyle(cell12, "微软雅黑", "9", 1, "left", "top", "#ffffff", "#4472C4", 1, "公司名称(英文)");
                    XWPFTableCell cell13 = tableOneRowOne.createCell();
                    setWordCellSelfStyle(cell13, "微软雅黑", "9", 1, "left", "top", "#ffffff", "#4472C4", 1, "公司名称(中文)");

                    XWPFTableRow tableOneRowTwo = tableOne.createRow();//行
                    tableOneRowTwo.getCell(0).setText("第二行第一列");
                    setWordCellSelfStyle(tableOneRowTwo.getCell(0), "微软雅黑", "9", 0, "left", "top", "#000000", "#B4C6E7", 1, "一行一列");
                    tableOneRowTwo.getCell(1).setText("第二行第二列");
                    setWordCellSelfStyle(tableOneRowTwo.getCell(1), "微软雅黑", "9", 0, "left", "top", "#000000", "#B4C6E7", 1, "一行一列");
                    tableOneRowTwo.getCell(2).setText("第二行第二列");
                    setWordCellSelfStyle(tableOneRowTwo.getCell(2), "微软雅黑", "9", 0, "left", "top", "#000000", "#B4C6E7", 1, "一行一列");
                    XWPFTableRow tableOneRowThree = tableOne.createRow();//行
                    tableOneRowThree.getCell(0).setText("第三行第一列");
                    setWordCellSelfStyle(tableOneRowThree.getCell(0), "微软雅黑", "9", 0, "left", "top", "#000000", "#D9E2F3", 1, "一行一列");
                    tableOneRowThree.getCell(1).setText("第三行第二列");
                    setWordCellSelfStyle(tableOneRowThree.getCell(1), "微软雅黑", "9", 0, "left", "top", "#000000", "#D9E2F3", 1, "一行一列");
                    tableOneRowThree.getCell(2).setText("第三行第二列");
                    setWordCellSelfStyle(tableOneRowThree.getCell(2), "微软雅黑", "9", 0, "left", "top", "#000000", "#D9E2F3", 1, "一行一列");
                } else {//动态图表
                    List<String> showtailArr = new ArrayList<String>();
                    showtailArr.add("0");
                    showtailArr.add("2");
                    List<String> ispercentArr = new ArrayList<String>();
                    ispercentArr.add("0");
                    ispercentArr.add("0");
                    List<String> titleArr = new ArrayList<String>();// 标题
                    titleArr.add("行业类别");
                    titleArr.add("占基金资产净值比例(%)");
                    List<String> fldNameArr = new ArrayList<String>();// 字段名
                    fldNameArr.add("item1");
                    fldNameArr.add("item3");
                    List<Map<String, String>> listItemsByType = new ArrayList<Map<String, String>>();
                    Map<String, String> base1 = new HashMap<String, String>();//相当于HashMap
                    base1.put("item1", "材料费用");
                    base1.put("item3", "500.10");
                    Map<String, String> base2 = new HashMap<String, String>();//相当于HashMap
                    base2.put("item1", "出差费用");
                    base2.put("item3", "300.10");
                    listItemsByType.add(base1);
                    listItemsByType.add(base2);
                    //动态刷新图表
                    List<POIXMLDocumentPart> relations = doc.getRelations();
                    for (POIXMLDocumentPart poixmlDocumentPart : relations) {
                        if (poixmlDocumentPart instanceof XWPFChart) {
                            XWPFChart chart = (XWPFChart) poixmlDocumentPart;
                            chart.getCTChart();
                            //根据属性第一列名称切换数据类型
                            CTChart ctChart = chart.getCTChart();
                            CTPlotArea plotArea = ctChart.getPlotArea();
                            CTBarChart barChart = plotArea.getBarChartArray(0);
                            List<CTBarSer> barSerList = barChart.getSerList();
                            //刷新内置excel数据
                            refreshExcel(chart, listItemsByType, fldNameArr, titleArr, showtailArr, ispercentArr);
                            //刷新页面显示数
                            refreshStrGraphContent(barChart, barSerList, listItemsByType, fldNameArr, titleArr, showtailArr, ispercentArr, 1);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param typeChart:传递的图形类型
     * @param serList:传递的serList(数据)
     * @param dataList:显示的数据
     * @param fldNameArr:属性列
     * @param position:取数的列
     * @return boolean
     * @Description:刷新数据方法(str进行分类)
     * @author nieds
     * @date 2019年6月11日上午10:36:56
     */
    public static boolean refreshStrGraphContent(Object typeChart,
                                                 List<?> serList, List<Map<String, String>> dataList, List<String> fldNameArr, List<String> titleArr, List<String> showtailArr, List<String> ispercentArr, int position) {

        boolean result = true;
        //更新数据区域
        for (int i = 0; i < serList.size(); i++) {
            //CTSerTx tx=null;
            CTAxDataSource cat = null;
            CTNumDataSource val = null;
            CTBarSer ser = ((CTBarChart) typeChart).getSerArray(i);
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
            if ("1".equals(ispercentArr.get(i + position))) {//是否设置百分比
                // 设置Y轴的数字为百分比样式显示
                StringBuilder sb = new StringBuilder();

                if ("0".equals(showtailArr.get(i + position))) {//保留几位小数
                    sb.append("0");
                    if ("1".equals(ispercentArr.get(i + position))) {//是否百分比
                        sb.append("%");
                    }
                } else {
                    sb.append("0.");
                    for (int k = 0; k < Integer.parseInt(showtailArr.get(i + position)); k++) {
                        sb.append("0");
                    }
                    if ("1".equals(ispercentArr.get(i + position))) {//是否百分比
                        sb.append("%");
                    }
                }
                val.getNumRef().getNumCache().setFormatCode(sb.toString());
            } else {
                //是否设置百分比
                // 设置Y轴的数字为百分比样式显示
                StringBuilder sb = new StringBuilder();

                if ("0".equals(showtailArr.get(i + position))) {//保留几位小数
                    sb.append("0");
                } else {
                    sb.append("0.");
                    for (int k = 0; k < Integer.parseInt(showtailArr.get(i + position)); k++) {
                        sb.append("0");
                    }
                }
                val.getNumRef().getNumCache().setFormatCode(sb.toString());
            }

        }
        return result;
    }

    public static boolean refreshExcel(XWPFChart chart,
                                       List<Map<String, String>> dataList, List<String> fldNameArr, List<String> titleArr,
                                       List<String> showtailArr, List<String> ispercentArr) {
        boolean result = true;
        org.apache.poi.ss.usermodel.Workbook wb = new XSSFWorkbook();
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
                    if ("1".equals(ispercentArr.get(j))) {//是否设置百分比
                        // 设置Y轴的数字为百分比样式显示
                        StringBuilder sb = new StringBuilder();

                        if ("0".equals(showtailArr.get(j))) {//保留几位小数
                            sb.append("0");
                            if ("1".equals(ispercentArr.get(j))) {//是否百分比
                                sb.append("%");
                            }
                        } else {
                            sb.append("0.");
                            for (int k = 0; k < Integer.parseInt(showtailArr.get(j)); k++) {
                                sb.append("0");
                            }
                            if ("1".equals(ispercentArr.get(j))) {//是否百分比
                                sb.append("%");
                            }
                        }
                        CellStyle cellStyle = wb.createCellStyle();
                        cellStyle.setDataFormat(wb.createDataFormat().getFormat(sb.toString()));
                        sheet.getRow(i + 1).getCell(j).setCellStyle(cellStyle);
                    } else {
                        //是否设置百分比
                        // 设置Y轴的数字为百分比样式显示
                        StringBuilder sb = new StringBuilder();

                        if ("0".equals(showtailArr.get(j))) {//保留几位小数
                            sb.append("0");
                        } else {
                            sb.append("0.");
                            for (int k = 0; k < Integer.parseInt(showtailArr.get(j)); k++) {
                                sb.append("0");
                            }
                        }
                        CellStyle cellStyle = wb.createCellStyle();
                        cellStyle.setDataFormat(wb.createDataFormat().getFormat(sb.toString()));
                        sheet.getRow(i + 1).getCell(j).setCellStyle(cellStyle);
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

    private static final BigDecimal bd2 = new BigDecimal("2");

    private static void setWordCellSelfStyle(XWPFTableCell cell, String fontName, String fontSize, int fontBlod,
                                             String alignment, String vertical, String fontColor,
                                             String bgColor, long cellWidth, String content) {

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

}
