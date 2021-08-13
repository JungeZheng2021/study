package com.aimsphm.nuclear.report.util;

import com.aimsphm.nuclear.common.constant.ReportConstant;
import com.aimsphm.nuclear.common.entity.AlgorithmRulesConclusionDO;
import com.aimsphm.nuclear.common.entity.BizReportConfigDO;
import com.aimsphm.nuclear.common.entity.vo.AlgorithmNormalFaultFeatureVO;
import com.aimsphm.nuclear.common.entity.vo.FaultReasoningVO;
import com.aimsphm.nuclear.common.entity.vo.ReportAlarmEventVO;
import com.aimsphm.nuclear.common.entity.vo.ReportFaultReasoningVO;
import com.aimsphm.nuclear.common.enums.FeatureTypeEnum;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.report.entity.vo.GraphDataItemVO;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.mortbay.log.Log;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty;
import org.springframework.stereotype.Component;

import java.beans.DesignMode;
import java.io.*;
import java.util.*;

import static com.aimsphm.nuclear.common.constant.ReportConstant.BLANK;
import static com.aimsphm.nuclear.common.constant.ReportConstant.WORD_BLANK;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.HASH;
import static com.aimsphm.nuclear.report.constant.PlaceholderConstant.PARAGRAPH_DIAGNOSIS_RESULTS;
import static org.apache.poi.xwpf.usermodel.BreakType.*;


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
@Slf4j
@Component
public class FanWordUtils {

    public static void main(String[] args) throws Exception {
//        boolean flag = demo();
//        if (flag) {
//            return;
//        }
        FileInputStream is = new FileInputStream(new File("D:\\Java\\workspace\\nuclear_power\\nuclear-report\\src\\main\\resources\\templates\\template-rcv.docx"));
//        FileInputStream is = new FileInputStream(new File("D:\\Desktop\\demo2.docx"));
//        FileInputStream is = new FileInputStream(new File("D:\\Desktop\\template-turbine1.docx"));
//        File image = new File("D:\\Desktop\\work\\2021年2月26日 184224田湾部署\\test.png");
        XWPFDocument doc = new XWPFDocument(is);
        HashMap<String, Object> data = Maps.newHashMap();
        data.put("#stopTimes#", "5次");
        data.put("#status#", "预警");
        data.put("#startTime#", DateUtils.formatCurrentDateTime());
        data.put("#continueTime#", "123.23天");
        data.put("#totalTime#", "200.09天");
        data.put("#ana-dielectricConstant#", "3.1415");
        data.put("#ana-temperature-th#", "高报:3.1415");
        data.put("#ana-humidity-decide#", "正常");


        test(data);
        BizReportConfigDO configDO = new BizReportConfigDO();
        configDO.setPlaceholder("#picRunningStatus#");
//        configDO.setImage(image);
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

    private static boolean demo() throws IOException {
        FileInputStream is = new FileInputStream(new File("D:\\Java\\workspace\\nuclear_power\\nuclear-report\\src\\main\\resources\\templates\\template-rcv.docx"));
        XWPFDocument doc = new XWPFDocument(is);
        List<XWPFParagraph> paragraphs = doc.getParagraphs();
        ListIterator<XWPFParagraph> it = paragraphs.listIterator();
        for (; it.hasNext(); ) {
        }
        return true;
    }

    private static void test(Map<String, Object> data) {
        String s = "[{\"eventName\":\"泵驱动端\",\"reasoningList\":[" +
                "{\"conclusion\":{\"componentId\":28,\"conclusion\":\"滑动轴承故障\",\"conclusionCode\":\"CONC_17\",\"deleted\":false,\"deviceType\":0,\"faultType\":1,\"id\":17,\"reason\":\"1.润滑油参数改变；\\n2.油管、滤网堵塞；\\n3.轴瓦安装间隙不当；\\n4.负载或其他外力造成转子失稳\",\"solution\":\"1.检查油品变质，油液杂质\\n2.检查油管堵塞、滤网\\n3.检查、更换轴瓦\\n4.检查工艺参数，调整工艺\"},\"faultInfo\":{\"componentId\":28,\"conclusionId\":17,\"deleted\":false,\"deviceType\":0,\"id\":17,\"ruleCode\":\"RFC_17\",\"ruleDesc\":\"泵驱动端滑动轴承故障\",\"ruleIntegrity\":0.8,\"ruleName\":\"滑动轴承故障\",\"ruleType\":0},\"features\":[{\"componentDesc\":\"2号上充泵泵驱动端\",\"componentId\":28,\"componentName\":\"泵驱动端\",\"deleted\":false,\"deviceType\":0,\"featureHi\":5.6,\"featureName\":\"泵驱动端轴承振动-N_通频值\",\"featureType\":5,\"gmtModified\":1627553252000,\"id\":211,\"sampleMethod\":\"median\",\"sensorCode\":\"5M2RCV240MV-N\",\"sensorDesc\":\"5M2RCV240MV-N-vec-Rms\",\"timeGap\":\"10m\",\"timeRange\":\"1h\"},{\"componentDesc\":\"2号上充泵泵驱动端\",\"componentId\":28,\"componentName\":\"泵驱动端\",\"deleted\":false,\"deviceType\":0,\"featureHi\":10.0,\"featureName\":\"润滑油油质-N_油液粘度变化（40°C下）\",\"featureType\":5,\"id\":216,\"sampleMethod\":\"median\",\"sensorCode\":\"5M2RCV002ML-N\",\"sensorDesc\":\"5M2RCV002ML-N-ana-viscosity-40-vary\",\"timeGap\":\"10m\",\"timeRange\":\"1h\"},{\"componentDesc\":\"2号上充泵泵驱动端\",\"componentId\":28,\"componentName\":\"泵驱动端\",\"deleted\":false,\"deviceType\":0,\"featureHi\":0.5,\"featureName\":\"润滑油油质-N_微粒数总数\",\"featureType\":1,\"id\":220,\"sampleMethod\":\"max\",\"sensorCode\":\"5M2RCV002ML-N\",\"sensorDesc\":\"5M2RCV002ML-N-abr-total\",\"timeGap\":\"1m\",\"timeRange\":\"1h\"},{\"componentDesc\":\"2号上充泵泵驱动端\",\"componentId\":28,\"componentName\":\"泵驱动端\",\"deleted\":false,\"deviceType\":0,\"featureHi\":4.0,\"featureName\":\"泵驱动端轴承声强-N声波强度\",\"featureType\":5,\"gmtModified\":1628142008000,\"id\":549,\"sampleMethod\":\"median\",\"sensorCode\":\"5M2RCV240MS-N\",\"sensorDesc\":\"5M2RCV240MS-N-raw-stressWaveStrength\",\"timeGap\":\"10m\",\"timeRange\":\"2h\"}],\"recommend\":0.201,\"ruleDesc\":\"泵驱动端滑动轴承故障\"}" +
                "]},{\"eventName\":\"泵非驱动端\",\"reasoningList\":[" +
                "{\"conclusion\":{\"componentId\":29,\"conclusion\":\"滑动轴承故障\",\"conclusionCode\":\"CONC_16\",\"deleted\":false,\"deviceType\":0,\"faultType\":1,\"id\":16,\"reason\":\"1.润滑油参数改变；\\n2.油管、滤网堵塞；\\n3.轴瓦安装间隙不当；\\n4.负载或其他外力造成转子失稳\",\"solution\":\"1.检查油品变质，油液杂质\\n2.检查油管堵塞、滤网\\n3.检查、更换轴瓦\\n4.检查工艺参数，调整工艺\"},\"faultInfo\":{\"componentId\":29,\"conclusionId\":16,\"deleted\":false,\"deviceType\":0,\"id\":16,\"ruleCode\":\"RFC_16\",\"ruleDesc\":\"泵非驱动端滑动轴承故障\",\"ruleIntegrity\":0.8,\"ruleName\":\"滑动轴承故障\",\"ruleType\":0},\"features\":[{\"componentDesc\":\"2号上充泵泵非驱动端\",\"componentId\":29,\"componentName\":\"泵非驱动端\",\"deleted\":false,\"deviceType\":0,\"featureHi\":5.6,\"featureName\":\"泵非驱动端轴承振动-N_通频值\",\"featureType\":5,\"gmtModified\":1627553252000,\"id\":228,\"sampleMethod\":\"median\",\"sensorCode\":\"5M2RCV241MV-N\",\"sensorDesc\":\"5M2RCV241MV-N-vec-Rms\",\"timeGap\":\"10m\",\"timeRange\":\"1h\"},{\"componentDesc\":\"2号上充泵泵非驱动端\",\"componentId\":29,\"componentName\":\"泵非驱动端\",\"deleted\":false,\"deviceType\":0,\"featureHi\":10.0,\"featureName\":\"润滑油油质-N_油液粘度变化（40°C下）\",\"featureType\":5,\"id\":233,\"sampleMethod\":\"median\",\"sensorCode\":\"5M2RCV002ML-N\",\"sensorDesc\":\"5M2RCV002ML-N-ana-viscosity-40-vary\",\"timeGap\":\"10m\",\"timeRange\":\"1h\"},{\"componentDesc\":\"2号上充泵泵非驱动端\",\"componentId\":29,\"componentName\":\"泵非驱动端\",\"deleted\":false,\"deviceType\":0,\"featureHi\":0.5,\"featureName\":\"润滑油油质-N_微粒数总数\",\"featureType\":1,\"id\":237,\"sampleMethod\":\"max\",\"sensorCode\":\"5M2RCV002ML-N\",\"sensorDesc\":\"5M2RCV002ML-N-abr-total\",\"timeGap\":\"1m\",\"timeRange\":\"1h\"}],\"recommend\":0.227,\"ruleDesc\":\"泵非驱动端滑动轴承故障\"}," +
                "{\"conclusion\":{\"componentId\":29,\"conclusion\":\"滑动轴承故障\",\"conclusionCode\":\"CONC_16\",\"deleted\":false,\"deviceType\":0,\"faultType\":1,\"id\":16,\"reason\":\"1.润滑油参数改变；\\n2.油管、滤网堵塞；\\n3.轴瓦安装间隙不当；\\n4.负载或其他外力造成转子失稳\",\"solution\":\"1.检查油品变质，油液杂质\\n2.检查油管堵塞、滤网\\n3.检查、更换轴瓦\\n4.检查工艺参数，调整工艺\"},\"faultInfo\":{\"componentId\":29,\"conclusionId\":16,\"deleted\":false,\"deviceType\":0,\"id\":16,\"ruleCode\":\"RFC_16\",\"ruleDesc\":\"泵非驱动端滑动轴承故障\",\"ruleIntegrity\":0.8,\"ruleName\":\"滑动轴承故障\",\"ruleType\":0},\"features\":[{\"componentDesc\":\"2号上充泵泵非驱动端\",\"componentId\":29,\"componentName\":\"泵非驱动端\",\"deleted\":false,\"deviceType\":0,\"featureHi\":5.6,\"featureName\":\"泵非驱动端轴承振动-N_通频值\",\"featureType\":5,\"gmtModified\":1627553252000,\"id\":228,\"sampleMethod\":\"median\",\"sensorCode\":\"5M2RCV241MV-N\",\"sensorDesc\":\"5M2RCV241MV-N-vec-Rms\",\"timeGap\":\"10m\",\"timeRange\":\"1h\"},{\"componentDesc\":\"2号上充泵泵非驱动端\",\"componentId\":29,\"componentName\":\"泵非驱动端\",\"deleted\":false,\"deviceType\":0,\"featureHi\":10.0,\"featureName\":\"润滑油油质-N_油液粘度变化（40°C下）\",\"featureType\":5,\"id\":233,\"sampleMethod\":\"median\",\"sensorCode\":\"5M2RCV002ML-N\",\"sensorDesc\":\"5M2RCV002ML-N-ana-viscosity-40-vary\",\"timeGap\":\"10m\",\"timeRange\":\"1h\"},{\"componentDesc\":\"2号上充泵泵非驱动端\",\"componentId\":29,\"componentName\":\"泵非驱动端\",\"deleted\":false,\"deviceType\":0,\"featureHi\":0.5,\"featureName\":\"润滑油油质-N_微粒数总数\",\"featureType\":1,\"id\":237,\"sampleMethod\":\"max\",\"sensorCode\":\"5M2RCV002ML-N\",\"sensorDesc\":\"5M2RCV002ML-N-abr-total\",\"timeGap\":\"1m\",\"timeRange\":\"1h\"}],\"recommend\":0.227,\"ruleDesc\":\"泵非驱动端滑动轴承故障\"}" +
                "]}]";
        System.out.println(s);
        List<ReportFaultReasoningVO> reportFaultReasoningVOS = JSON.parseArray(s, ReportFaultReasoningVO.class);
        data.put(PARAGRAPH_DIAGNOSIS_RESULTS, reportFaultReasoningVOS);
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
                    if (log.isDebugEnabled() && text.contains("#")) {
                        log.debug("{}", text);
                    }
                    boolean isNeedWrite = text.startsWith(HASH) && text.endsWith(HASH);
                    //设置均值
                    if (isNeedWrite && data.containsKey(text)) {
                        String string = MapUtils.getString(data, text);
                        List<XWPFParagraph> paragraphs = cell.getParagraphs();
                        XWPFParagraph paragraph = paragraphs.get(0);
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (int i = 0; i < runs.size(); i++) {
                            XWPFRun r = runs.get(i);
                            XWPFRun run = runs.get(i);
                            run.setFontSize(11);
                            r.setText(i == 0 ? string : BLANK, 0);
                        }
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
                if (log.isDebugEnabled() && text.contains("#")) {
                    log.debug("{}", text);
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
        run.setText(BLANK, 0);
        for (int i = 0; i < data.size(); i++) {
            Object x = data.get(i);
            if (x instanceof ReportAlarmEventVO) {
                fillReportAlarmEventVOImages(run, (ReportAlarmEventVO) x);
            }
            if (x instanceof ReportFaultReasoningVO) {
                fillReportFaultReasoning(i + 1, run, (ReportFaultReasoningVO) x);
            }
        }
    }

    /**
     * 生成故障推理
     *
     * @param index
     * @param run   段落
     * @param vo    推列内容
     */
    private void fillReportFaultReasoning(int index, XWPFRun run, ReportFaultReasoningVO vo) {

        List<FaultReasoningVO> reasoningList = vo.getReasoningList();
        if (CollectionUtils.isEmpty(reasoningList)) {
            return;
        }
        if (index == 1) {
            run.addCarriageReturn();
        }
        run.addTab();
        run.setText(String.format("（%s）%s", index, vo.getEventName()), -1);
        run.addCarriageReturn();
        int tabSize = 2;
        for (FaultReasoningVO reasoning : reasoningList) {
            run.setFontSize(11);
            addTab(run, tabSize - 1);
            run.setText(" ■ ", -1);
            run.setText("故障结论：", -1);
            run.setText(reasoning.getConclusion().getConclusion(), -1);
            run.addCarriageReturn();

            addTab(run, tabSize);
            run.setText("故障推荐度：", -1);
            run.setText(reasoning.getRecommend() + "", -1);
            run.addCarriageReturn();

            addTab(run, tabSize);
            run.setText("故障征兆：", -1);
            run.addCarriageReturn();
            List<AlgorithmNormalFaultFeatureVO> features = reasoning.getFeatures();
            if (CollectionUtils.isNotEmpty(features)) {
                for (int i = 0; i < features.size(); i++) {
                    AlgorithmNormalFaultFeatureVO featureVO = features.get(i);
                    addTab(run, tabSize + 1);
                    run.setText(i + 1 + ". [" + featureVO.getComponentName() + "] " + featureVO.getFeatureName() + FeatureTypeEnum.getDescByValue(featureVO.getFeatureType()), -1);
                    run.addCarriageReturn();
                }
            }
            addTab(run, tabSize);
            run.setText("故障原因：", -1);
            run.addCarriageReturn();
            Arrays.stream(reasoning.getConclusion().getReason().split("\n")).forEach(x -> {
                addTab(run, tabSize + 1);
                run.setText(x, -1);
                run.addCarriageReturn();
            });

            addTab(run, tabSize);
            run.setText("维修建议：", -1);
            run.addCarriageReturn();
            Arrays.stream(reasoning.getConclusion().getSolution().split("\n")).forEach(x -> {
                addTab(run, tabSize + 1);
                run.setText(x, -1);
                run.addCarriageReturn();
            });
        }
        run.getParagraph().setSpacingBetween(1.5D);
    }

    private void addTab(XWPFRun run, int i) {
        for (int j = 0; j < i; j++) {
            run.addTab();
        }
    }


    /**
     * 生成动态阈值报警图片
     *
     * @param run
     * @param item
     */
    private void fillReportAlarmEventVOImages(XWPFRun run, ReportAlarmEventVO item) {
        List<Map<String, List<BizReportConfigDO>>> images = item.getImages();
        for (int i = 0, len = images.size(); i < len; i++) {
            Map<String, List<BizReportConfigDO>> map = images.get(i);
            Set<Map.Entry<String, List<BizReportConfigDO>>> entries = map.entrySet();
            for (Iterator<Map.Entry<String, List<BizReportConfigDO>>> it = entries.iterator(); it.hasNext(); ) {
                Map.Entry<String, List<BizReportConfigDO>> next = it.next();
                String key = next.getKey();
                List<BizReportConfigDO> value = next.getValue();
                run.setText("1.4." + (i + 1) + key, -1);
                run.addCarriageReturn();
                for (int j = 0; j < value.size(); j++) {
                    BizReportConfigDO configDO = value.get(j);
                    if (j == 0) {
                        String alias = Objects.isNull(configDO.getRemark()) ? BLANK : configDO.getRemark();
                        String pointId = Objects.isNull(configDO.getPointIds()) ? BLANK : configDO.getPointIds();
                        run.setText(String.format("●（%s）%s(%s)", (j + 1), pointId, alias), -1);
                    }
                    run.addCarriageReturn();
                    WordUtils.addPicture(run, configDO.getImage(), null);
                }
            }
        }
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


