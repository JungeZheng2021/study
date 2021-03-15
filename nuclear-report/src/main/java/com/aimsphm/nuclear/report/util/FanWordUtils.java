package com.aimsphm.nuclear.report.util;

import com.aimsphm.nuclear.common.constant.ReportConstant;
import com.aimsphm.nuclear.common.entity.AlgorithmRulesConclusionDO;
import com.aimsphm.nuclear.common.entity.BizReportConfigDO;
import com.aimsphm.nuclear.report.entity.vo.GraphDataItemVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

import static com.aimsphm.nuclear.common.constant.ReportConstant.BLANK;
import static com.aimsphm.nuclear.common.constant.ReportConstant.WORD_BLANK;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.HASH;


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
@Component
public class FanWordUtils {

    public static void main(String[] args) throws Exception {
        FileInputStream is = new FileInputStream(new File("D:\\Java\\workspace\\nuclear_power\\nuclear-report\\src\\main\\resources\\templates\\template-rcv.docx"));
//        FileInputStream is = new FileInputStream(new File("D:\\Desktop\\demo2.docx"));
//        FileInputStream is = new FileInputStream(new File("D:\\Desktop\\template-turbine1.docx"));
        File image = new File("D:\\Desktop\\work\\2021年2月26日 184224田湾部署\\test.png");
        XWPFDocument doc = new XWPFDocument(is);
        HashMap<String, Object> data = Maps.newHashMap();
        data.put("10MTS-ZT013A1_S", 12);
        data.put("10MTS-ZT013A1_E", 15);
        data.put("10MTS-ZT010A1_S", 152);
        data.put("10MTS-ZT010A1_E", 159);
        data.put("10MTS-ZT010A2_S", 252);
        data.put("10MTS-ZT010A2_E", 359);
        BizReportConfigDO configDO = new BizReportConfigDO();
        configDO.setPlaceholder("#picRunningStatus#");
        configDO.setImage(image);
        data.put("#picRunningStatus#", configDO);
        new FanWordUtils().operationPlaceholderInWord(doc, data);
        final String docPath = ReportConstant.DOC_TEMP_DIR_PRE;
        final String docName = "device/2" + File.separator + UUIDUtils.randomUUID() + ReportConstant.DOC_SUFFIX;
        File file = new File(docPath, docName);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            parentFile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(file);
        doc.write(fos);
    }

    public void operationPlaceholderInWord(XWPFDocument doc, Map<String, Object> data) throws Exception {
        operationPlaceholderInWordTable(doc, data);
        operationPlaceholderInWordParagraph(doc, data);
    }

    /**
     * 操作word中的表格
     *
     * @param doc  文档对象
     * @param data 数据集合
     * @throws Exception
     */
    private void operationPlaceholderInWordTable(XWPFDocument doc, Map<String, Object> data) {
        Iterator<XWPFTable> tableList = doc.getTablesIterator();
        if (Objects.isNull(tableList)) {
            return;
        }
        for (; tableList.hasNext(); ) {
            XWPFTable table = tableList.next();
            List<XWPFTableRow> rows = table.getRows();
            for (XWPFTableRow row : rows) {
                List<XWPFTableCell> tableCells = row.getTableCells();
                for (XWPFTableCell cell : tableCells) {
                    String text = cell.getText();
                    if (text == null || text.length() == 0) {
                        continue;
                    }
                    boolean isNeedWrite = text.startsWith(HASH) && text.endsWith(HASH);
                    //设置均值
                    if (isNeedWrite && data.containsKey(text)) {
                        //删除第一个位置的文字
                        cell.removeParagraph(0);
                        cell.setText(MapUtils.getString(data, text));
                    } else if (isNeedWrite) {
                        //删除第一个位置的文字
                        cell.removeParagraph(0);
                        cell.setText(WORD_BLANK);
                    }
                    WordUtils.setAlignmentCenter(cell);
                }
            }
        }
    }

    /**
     * 操作文档的段落部分
     *
     * @param doc  文档对象
     * @param data 数据集合
     * @throws Exception
     */
    private void operationPlaceholderInWordParagraph(XWPFDocument doc, Map<String, Object> data) throws Exception {

        List<XWPFParagraph> paragraphList = doc.getParagraphs();
        if (Objects.isNull(paragraphList) || paragraphList.isEmpty()) {
            return;
        }
        ListIterator<XWPFParagraph> it = paragraphList.listIterator();
        for (; it.hasNext(); ) {
            XWPFParagraph paragraph = it.next();
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                if (text == null || text.length() == 0) {
                    continue;
                }
                boolean isNeedWrite = text.startsWith("#") && text.lastIndexOf("#") != 0;
                if (text.startsWith("#pic") && text.endsWith("#")) {
                    BizReportConfigDO config = (BizReportConfigDO) data.get(text);
                    if (Objects.isNull(config) || Objects.isNull(config.getImage())) {
                        run.setText("暂无数据", 0);
                        continue;
                    }
                    //设置图片
                    WordUtils.addPicture(run, config.getImage(), config.getTitle());
//                    最值替换
                } else if (text.startsWith("#table")) {
                    addAlarmEventTable(run, paragraph, doc, (List<Object[]>) data.get(text));
                } else if (text.startsWith("#paragraph")) {
                    //在段落中添加文字或者是图片
                    List paragraphData = (List) data.get(text);
                    filParagraphWithData(run, paragraphData);
                } else {
                    //设置段落中的文本
                    if (isNeedWrite && data.containsKey(text)) {
                        run.setText(MapUtils.getString(data, text), 0);
                    } else if (text.startsWith("#") && text.contains("_max_tag#")) {
                        run.setText(WORD_BLANK, 0);
                    } else if (isNeedWrite) {
                        run.setText(WORD_BLANK, 0);
                    }
                }
            }
        }
    }

    private void filParagraphWithData(XWPFRun run, List data) {
        if (CollectionUtils.isEmpty(data)) {
            run.setText("无数据", 0);
            return;
        }
        run.setText("", 0);
        data.stream().forEach(x -> {
            if (x instanceof GraphDataItemVO) {
                GraphDataItemVO item = (GraphDataItemVO) x;
                run.setText(item.getTitle(), -1);
                run.addBreak();
                run.setText(item.getDesc(), -1);
                run.addBreak();
                WordUtils.addPicture(run, item.getImage(), BLANK);
            }
            if (x instanceof AlgorithmRulesConclusionDO) {
                AlgorithmRulesConclusionDO item = (AlgorithmRulesConclusionDO) x;
                run.setText(item.getConclusion(), -1);
                run.addBreak();
                run.setText("常见原因：", -1);
                run.addBreak();
                run.setText(item.getReason(), -1);
                run.addBreak();
                run.setText("维修建议：", -1);
                run.addBreak();
                run.setText(item.getSuggest(), -1);
                run.addBreak();
            }
        });
    }

    /**
     * 表头列表
     */
    private static final List<String> titles = Lists.newArrayList("报警名称", "测点编号", "报警原因", "报警级别", "开始报警时间");

    private void addAlarmEventTable(XWPFRun run, XWPFParagraph paragraph, XWPFDocument doc, List<Object[]> options) {
        run.setText("", 0);
        if (CollectionUtils.isEmpty(options)) {
            return;
        }
        XmlCursor cursor = paragraph.getCTP().newCursor();
        XWPFTable table = doc.insertNewTbl(cursor);
        WordUtils.initTable(table, titles, options.toArray(new Object[]{}));
    }
}


