package com.aimsphm.nuclear.report.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.ImageUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.report.util
 * @Description: <主泵中使用的word工具类>
 * @Author: MILLA
 * @CreateDate: 2020/6/11 18:22
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/6/11 18:22
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class WordUtils {
    /**
     * 图片缩放比例
     */

    public static final double IMAGE_RATE = 0.18;

    public static final BigDecimal bd2 = new BigDecimal("2");

    public static final String MS_FONT_FAMILY = "微软雅黑";

    /**
     * 添加图片到指定位置
     *
     * @param run  指定段落
     * @param file 文件
     * @throws Exception
     */
    public static void addPicture(XWPFRun run, File file, String title) {
        try {
            //将占位符隐藏
            run.setText("", 0);
            //获取图片的大小，在word中进行等比缩放
            Dimension dimension = ImageUtils.getImageDimension(new FileInputStream(file), 7);
            //添加图片到文档中
            run.addPicture(new FileInputStream(file), XWPFDocument.PICTURE_TYPE_PNG, file.getName(), Units.toEMU(dimension.width * IMAGE_RATE), Units.toEMU(dimension.getHeight() * IMAGE_RATE));
            //换行
            run.addBreak();
            //将占位符隐藏
            run.setText(title, -1);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置单元格水平居中
     *
     * @param cell
     */
    public static void setAlignmentCenter(XWPFTableCell cell) {
        CTTc cttc = cell.getCTTc();
        CTP ctp = cttc.getPList().get(0);
        CTPPr ctppr = ctp.getPPr();
        if (ctppr == null) {
            ctppr = ctp.addNewPPr();
        }
        CTJc ctjc = ctppr.getJc();
        if (ctjc == null) {
            ctjc = ctppr.addNewJc();
        }
        //水平居中
        ctjc.setVal(STJc.CENTER);
    }

    public static void initTable(XWPFTable table, List<String> heads, Object[] options) {
        initTableTHead(table, heads);
        for (int m = 0, size = options.length; m < size; m++) {
            Object[] data = (Object[]) options[m];
            XWPFTableRow row = table.createRow();
            for (int i = 0, len = heads.size(); i < len; i++) {
                XWPFTableCell cell;
                cell = row.getCell(i);
//                if (i == 0) {
//                    setCellStyle(cell, 0, "#000000", m % 2 == 0 ? "#D9E2F3" : "#B4C6E7", String.valueOf((m + 1)));
//                    continue;
//                }
                setCellStyle(cell, 0, "#000000", m % 2 == 0 ? "#D9E2F3" : "#B4C6E7", String.valueOf(data[i]));
            }
        }

    }

    public static void initTableTHead(XWPFTable table, List<String> heads) {
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

    public static void setCellStyle(XWPFTableCell cell, int fontBold, String fontColor, String bgColor, String content) {
        setCellStyle(cell, MS_FONT_FAMILY, "9", fontBold, "left", "top", fontColor, bgColor, content);
    }

    public static void setCellStyle(XWPFTableCell cell, String fontName, String fontSize, int fontBold,
                                    String alignment, String vertical, String fontColor,
                                    String bgColor, String content) {

        //poi对字体大小设置特殊，不支持小数，但对原word字体大小做了乘2处理
        BigInteger bFontSize = new BigInteger("24");
        if (fontSize != null && !fontSize.equals("")) {
            //poi对字体大小设置特殊，不支持小数，但对原word字体大小做了乘2处理
            BigDecimal fontSizeBD = new BigDecimal(fontSize);
            fontSizeBD = bd2.multiply(fontSizeBD);
            //这里取整
            fontSizeBD = fontSizeBD.setScale(0, BigDecimal.ROUND_HALF_UP);
            // 字体大小
            bFontSize = new BigInteger(fontSizeBD.toString());
        }
        //=====获取单元格
        CTTc tc = cell.getCTTc();
        //获取单元格里的<w:tcPr>
        CTTcPr tcPr = tc.getTcPr();
        //没有<w:tcPr>，创建
        if (tcPr == null) {
            tcPr = tc.addNewTcPr();
        }

        //  --vjc开始-->>
        //获取<w:tcPr>  的<w:vAlign w:val="center"/>
        CTVerticalJc vjc = tcPr.getVAlign();
        //没有<w:w:vAlign/>，创建
        if (vjc == null) {
            vjc = tcPr.addNewVAlign();
        }
        //设置单元格对齐方式 //垂直对齐
        vjc.setVal(vertical.equals("top") ? STVerticalJc.TOP : vertical.equals("bottom") ? STVerticalJc.BOTTOM : STVerticalJc.CENTER);

        CTShd shd = tcPr.getShd();
        //没有<w:shd>，创建
        if (shd == null) {
            shd = tcPr.addNewShd();
        }
        // 设置背景颜色
        shd.setFill(bgColor.substring(1));
        //《《《《====tcPr结束====

        //====p开始====》》》》
        CTP p = tc.getPList().get(0);

        //---ppr开始--->>>
        //获取<w:p>里的<w:pPr>
        CTPPr ppr = p.getPPr();
        //没有<w:pPr>，创建
        if (ppr == null) {
            ppr = p.addNewPPr();
        }
        //  --jc开始-->>
        CTJc jc = ppr.getJc();
        if (jc == null) {
            jc = ppr.addNewJc();
        }
        //设置单元格对齐方式//水平对齐
        jc.setVal(alignment.equals("left") ? STJc.LEFT : alignment.equals("right") ? STJc.RIGHT : STJc.CENTER);
        //  <<--jc结束--
        //  --pRpr开始-->>
        CTParaRPr pRpr = ppr.getRPr();
        if (pRpr == null) {
            pRpr = ppr.addNewRPr();
        }
        CTFonts pFont = pRpr.getRFonts();
        if (pFont == null) {
            pFont = pRpr.addNewRFonts();
        }
        //设置字体
        pFont.setAscii(fontName);
        pFont.setEastAsia(fontName);
        pFont.setHAnsi(fontName);

        CTOnOff pb = pRpr.getB();
        if (pb == null) {
            pb = pRpr.addNewB();
        }
        //设置字体是否加粗
        pb.setVal(fontBold == 1 ? STOnOff.ON : STOnOff.OFF);

        CTHpsMeasure psz = pRpr.getSz();
        if (psz == null) {
            psz = pRpr.addNewSz();
        }
        // 设置单元格字体大小
        psz.setVal(bFontSize);
        CTHpsMeasure pszCs = pRpr.getSzCs();
        if (pszCs == null) {
            pszCs = pRpr.addNewSzCs();
        }
        // 设置单元格字体大小
        pszCs.setVal(bFontSize);
        //  <<--pRpr结束--
        //<<<---ppr结束---

        //---r开始--->>>
        List<CTR> rlist = p.getRList();
        CTR r = null;
        if (rlist != null && rlist.size() > 0) {
            r = rlist.get(0);
        } else {//没有<w:r>，创建
            r = p.addNewR();
        }
        //--rpr开始-->>
        CTRPr rpr = r.getRPr();
        if (rpr == null) {
            rpr = r.addNewRPr();
        }
        //->-
        CTFonts font = rpr.getRFonts();
        if (font == null) {
            font = rpr.addNewRFonts();
        }
        //设置字体
        font.setAscii(fontName);
        font.setEastAsia(fontName);
        font.setHAnsi(fontName);

        CTOnOff b = rpr.getB();
        if (b == null) {
            b = rpr.addNewB();
        }
        //设置字体是否加粗
        b.setVal(fontBold == 1 ? STOnOff.ON : STOnOff.OFF);
        CTColor color = rpr.getColor();
        if (color == null) {
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
        if (tList != null && tList.size() > 0) {
            t = tList.get(0);
        } else {//没有<w:r>，创建
            t = r.addNewT();
        }
        t.setStringValue(content);
        //<<<---r结束---
    }
}


