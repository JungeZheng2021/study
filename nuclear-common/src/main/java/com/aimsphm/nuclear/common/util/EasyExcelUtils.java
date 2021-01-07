package com.aimsphm.nuclear.common.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Package: com.aimsphm.common
 * @Description: <excel导出工具类>
 * @Author: MILLA
 * @CreateDate: 2018/5/4 16:02
 * @UpdateUser: MILLA
 * @UpdateDate: 2018/5/4 16:02
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class EasyExcelUtils {

    public static void Write2Website(HttpServletResponse response, List list, Class clazz, String sheetName, String filename) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(filename, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
//        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
//        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
//        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        // 字体策略
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteCellStyle.setWriteFont(contentWriteFont);

        //设置 自动换行
        contentWriteCellStyle.setWrapped(true);
        //设置 垂直居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        //设置 水平居中
//        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
//        //设置边框样式
//        contentWriteCellStyle.setBorderLeft(DASHED);
//        contentWriteCellStyle.setBorderTop(DASHED);
//        contentWriteCellStyle.setBorderRight(DASHED);
//        contentWriteCellStyle.setBorderBottom(DASHED);

        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).registerWriteHandler(horizontalCellStyleStrategy).doWrite(list);
    }

    public static void WriteExcel(HttpServletResponse response, List[] lists, Class[] clazzs, String filename, String[] sheetName) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
// 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(filename) + sdf.format(new Date()) + ".xlsx");
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
//        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
//        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
//        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        // 字体策略
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteCellStyle.setWriteFont(contentWriteFont);

        //设置 自动换行
        contentWriteCellStyle.setWrapped(true);
        //设置 垂直居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        //设置 水平居中
//        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
//        //设置边框样式
//        contentWriteCellStyle.setBorderLeft(DASHED);
//        contentWriteCellStyle.setBorderTop(DASHED);
//        contentWriteCellStyle.setBorderRight(DASHED);
//        contentWriteCellStyle.setBorderBottom(DASHED);

        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).registerWriteHandler(horizontalCellStyleStrategy).build();

        for (int i = 0; i < lists.length; i++) {

            WriteSheet writeSheet = EasyExcel.writerSheet(i, sheetName[i]).head(clazzs[i]).build();

            excelWriter.write(lists[i], writeSheet);
        }
        excelWriter.finish();
    }
}
