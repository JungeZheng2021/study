package com.aimsphm.nuclear.report.util;

import lombok.extern.slf4j.Slf4j;
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
import java.util.Objects;

/**
 * <p>
 * 功能描述:word工具类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/6/11 18:22
 */
@Slf4j
public final class WordUtils {
    /**
     * 图片缩放比例
     */

    public static final double IMAGE_RATE = 0.18;

    public static final BigDecimal bd2 = new BigDecimal("2");

    public static final String MS_FONT_FAMILY = "宋体";

    /**
     * 添加图片到指定位置
     *
     * @param run  指定段落
     * @param file 文件
     * @throws Exception
     */
    public static void addPicture(XWPFRun run, File file, String title) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            //将占位符隐藏
            run.setText("", 0);
            //获取图片的大小，在word中进行等比缩放
            Dimension dimension = ImageUtils.getImageDimension(new FileInputStream(file), 7);
            //添加图片到文档中
            run.addPicture(inputStream, XWPFDocument.PICTURE_TYPE_PNG, file.getName(), Units.toEMU(dimension.width * IMAGE_RATE), Units.toEMU(dimension.getHeight() * IMAGE_RATE));
            //换行
            run.addCarriageReturn();
            if (Objects.nonNull(title)) {
                //将占位符隐藏
                run.setText(title, -1);
            }
        } catch (Exception e) {
            log.error("add image failed....");
        } finally {
            if (Objects.nonNull(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
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

    public static void setCellStyle(XWPFTableCell cell, String fontSize, String text) {
        setCellStyle(cell, MS_FONT_FAMILY, fontSize, 0, "left", null, "#ffffff", null, text);
    }

    public static void setCellStyle(XWPFTableCell cell, String fontName, String fontSize, int fontBold,
                                    String alignment, String vertical, String fontColor, String bgColor, String content) {

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
        CTTcPr tcPr = Objects.isNull(tc.getTcPr()) ? tc.addNewTcPr() : tc.getTcPr();

        CTVerticalJc vjc = Objects.isNull(tcPr.getVAlign()) ? tcPr.addNewVAlign() : tcPr.getVAlign();
        //设置单元格对齐方式 //垂直对齐
        vjc.setVal("top".equals(vertical) ? STVerticalJc.TOP : "bottom".equals(vertical) ? STVerticalJc.BOTTOM : STVerticalJc.CENTER);

        CTShd shd = Objects.isNull(tcPr.getShd()) ? tcPr.addNewShd() : tcPr.getShd();
        // 设置背景颜色
        if (Objects.nonNull(bgColor)) {
            shd.setFill(bgColor.substring(1));
        }

        List<CTP> pList = tc.getPList();
        if (pList.size() == 0) {
            cell.setText(content);
            return;
        }
        CTP p = tc.getPList().get(0);

        //获取<w:p>里的<w:pPr>
        CTPPr ppr = Objects.isNull(p.getPPr()) ? p.addNewPPr() : p.getPPr();
        //  --jc开始-->>
        CTJc jc = Objects.isNull(ppr.getJc()) ? ppr.addNewJc() : ppr.getJc();
        //设置单元格对齐方式//水平对齐
        jc.setVal(alignment.equals("left") ? STJc.LEFT : (alignment.equals("right") ? STJc.RIGHT : STJc.CENTER));
        //  <<--jc结束--
        //  --pRpr开始-->>
        CTParaRPr pRpr = Objects.isNull(ppr.getRPr()) ? ppr.addNewRPr() : ppr.getRPr();
        CTFonts pFont = Objects.isNull(pRpr.getRFonts()) ? pRpr.addNewRFonts() : pRpr.getRFonts();
        //设置字体
        pFont.setAscii(fontName);
        pFont.setEastAsia(fontName);
        pFont.setHAnsi(fontName);

        CTOnOff pb = Objects.isNull(pRpr.getB()) ? pRpr.addNewB() : pRpr.getB();
        //设置字体是否加粗
        pb.setVal(fontBold == 1 ? STOnOff.ON : STOnOff.OFF);

        CTHpsMeasure psz = Objects.isNull(pRpr.getSz()) ? pRpr.addNewSz() : pRpr.getSz();
        // 设置单元格字体大小
        psz.setVal(bFontSize);
        CTHpsMeasure pszCs = Objects.isNull(pRpr.getSzCs()) ? pRpr.addNewSzCs() : pRpr.getSzCs();
        // 设置单元格字体大小
        pszCs.setVal(bFontSize);
        List<CTR> rList = p.getRList();
        CTR r = rList != null && !rList.isEmpty() ? rList.get(0) : p.addNewR();
        //--rpr开始-->>
        CTRPr rpr = Objects.isNull(r.getRPr()) ? r.addNewRPr() : r.getRPr();

        CTFonts font = Objects.isNull(rpr.getRFonts()) ? rpr.addNewRFonts() : rpr.getRFonts();
        //设置字体
        font.setAscii(fontName);
        font.setEastAsia(fontName);
        font.setHAnsi(fontName);

        CTOnOff b = Objects.isNull(rpr.getB()) ? rpr.addNewB() : rpr.getB();
        //设置字体是否加粗
        b.setVal(fontBold == 1 ? STOnOff.ON : STOnOff.OFF);
        CTColor color = Objects.isNull(rpr.getColor()) ? rpr.addNewColor() : rpr.getColor();
        // 设置字体颜色
        if (content.contains("↓")) {
            color.setVal("43CD80");
        } else if (content.contains("↑")) {
            color.setVal("943634");
        } else if (Objects.nonNull(fontColor)) {
            color.setVal(fontColor.substring(1));
        }

        CTHpsMeasure sz = Objects.isNull(rpr.getSz()) ? rpr.addNewSz() : rpr.getSz();
        sz.setVal(bFontSize);
        CTHpsMeasure szCs = Objects.isNull(rpr.getSzCs()) ? rpr.addNewSz() : rpr.getSzCs();
        szCs.setVal(bFontSize);
        List<CTText> tList = r.getTList();
        CTText t = tList != null && !tList.isEmpty() ? tList.get(0) : r.addNewT();
        t.setStringValue(content);
    }
}


