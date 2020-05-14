package com.milla.study.netbase.expert.poi;

/**
 * @Package: com.milla.study.netbase.expert.poi
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/23 16:04
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/23 16:04
 * @UpdateRemark: <>
 * @Version: 1.0
 */


import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POIWordTable {

    public static void main(String[] args) throws Exception {

        final String returnurl = "C:\\Users\\milla\\Desktop\\111-new.docx";  // 结果文件

        final String templateurl = "C:\\Users\\milla\\Desktop\\1.docx";  // 模板文件

        InputStream is = new FileInputStream(new File(templateurl));
        XWPFDocument doc = new XWPFDocument(is);

        // 替换word模板数据
        replaceAll(doc);

        // 保存结果文件
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
    public static void replaceAll(XWPFDocument doc) throws InvalidFormatException, IOException {
        doParagraphs(doc); // 处理段落文字数据，包括文字和表格、图片
        doCharts(doc);  // 处理图表数据，柱状图、折线图、饼图啊之类的
    }


    /**
     * 处理段落文字
     *
     * @param doc
     * @throws InvalidFormatException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void doParagraphs(XWPFDocument doc) throws InvalidFormatException, IOException {

        // 文本数据
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("var", "我是被替换的文本内容");

        // 图片数据
        Map<String, String> imgMap = new HashMap<String, String>();
        imgMap.put("img", "C:\\Users\\milla\\Desktop\\111.jpg");


        /**----------------------------处理段落------------------------------------**/
        List<XWPFParagraph> paragraphList = doc.getParagraphs();
        if (paragraphList != null && paragraphList.size() > 0) {
            for (XWPFParagraph paragraph : paragraphList) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    String text = run.getText(0);
                    List<XWPFPicture> embeddedPictures = run.getEmbeddedPictures();
                    if (text != null) {

                        // 替换文本信息
                        String tempText = text;
                        String key = tempText.replaceAll("\\{\\{", "").replaceAll("}}", "");
                        if (!StringUtils.isEmpty(textMap.get(key))) {
                            run.setText(textMap.get(key), 0);
                        }

                        // 替换图片内容 参考：https://blog.csdn.net/a909301740/article/details/84984445
                        String tempImgText = text;
                        String imgkey = tempImgText.replaceAll("\\{\\{@", "").replaceAll("}}", "");
                        if (!StringUtils.isEmpty(imgMap.get(imgkey)) && text.contains("${image}")) {
                            String imgPath = imgMap.get(imgkey);
                            try {
                                run.setText("", 0);
                                run.addPicture(new FileInputStream(imgPath), Document.PICTURE_TYPE_PNG, "img.png", Units.toEMU(200), Units.toEMU(200));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        // 动态表格
                        if (text.contains("${table}")) {
                            run.setText("", 0);
                            XmlCursor cursor = paragraph.getCTP().newCursor();
                            XWPFTable tableOne = doc.insertNewTbl(cursor);// ---这个是关键

                            // 设置表格宽度，第一行宽度就可以了，这个值的单位，目前我也还不清楚，还没来得及研究
                            tableOne.setWidth(8500);

                            // 表格第一行，对于每个列，必须使用createCell()，而不是getCell()，因为第一行嘛，肯定是属于创建的，没有create哪里来的get呢
                            XWPFTableRow tableOneRowOne = tableOne.getRow(0);//行
                            new PoiWordTools().setWordCellSelfStyle(tableOneRowOne.getCell(0), "微软雅黑", "9", 0, "left", "top", "#000000", "#B4C6E7", "10%", "序号");
                            new PoiWordTools().setWordCellSelfStyle(tableOneRowOne.createCell(), "微软雅黑", "9", 0, "left", "top", "#000000", "#B4C6E7", "45%", "公司名称(英文)");
                            new PoiWordTools().setWordCellSelfStyle(tableOneRowOne.createCell(), "微软雅黑", "9", 0, "left", "top", "#000000", "#B4C6E7", "45%", "公司名称(中文)");

                            // 表格第二行
                            XWPFTableRow tableOneRowTwo = tableOne.createRow();//行
                            new PoiWordTools().setWordCellSelfStyle(tableOneRowTwo.getCell(0), "微软雅黑", "9", 0, "left", "top", "#000000", "#B4C6E7", "10%", "一行一列");
                            new PoiWordTools().setWordCellSelfStyle(tableOneRowTwo.getCell(1), "微软雅黑", "9", 0, "left", "top", "#000000", "#B4C6E7", "45%", "一行一列");
                            new PoiWordTools().setWordCellSelfStyle(tableOneRowTwo.getCell(2), "微软雅黑", "9", 0, "left", "top", "#000000", "#B4C6E7", "45%", "一行一列");


                            // ....... 可动态添加表格
                        }
                    }
                    //替换图片
                    if (!embeddedPictures.isEmpty()) {
                        String imgPath = imgMap.get("img");
                        try {
                            run.setText("", 0);
                            run.addPicture(new FileInputStream(imgPath), Document.PICTURE_TYPE_JPEG, "img.png", Units.toEMU(200), Units.toEMU(200));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    /**
     * 处理图表
     *
     * @param doc
     * @throws FileNotFoundException
     */
    public static void doCharts(XWPFDocument doc) throws FileNotFoundException {
        /**----------------------------处理图表------------------------------------**/

        // 数据准备
        List<String> titleArr = new ArrayList<String>();// 标题
        titleArr.add("title");
        titleArr.add("金额");

        List<String> fldNameArr = new ArrayList<String>();// 字段名
        fldNameArr.add("item1");
        fldNameArr.add("item2");

        // 数据集合
        List<Map<String, String>> listItemsByType = new ArrayList<Map<String, String>>();

        // 第一行数据
        Map<String, String> base1 = new HashMap<String, String>();
        base1.put("item1", "材料费用");
        base1.put("item2", "500");

        // 第二行数据
        Map<String, String> base2 = new HashMap<String, String>();
        base2.put("item1", "出差费用");
        base2.put("item2", "300");

        // 第三行数据
        Map<String, String> base3 = new HashMap<String, String>();
        base3.put("item1", "住宿费用");
        base3.put("item2", "300");

        listItemsByType.add(base1);
        listItemsByType.add(base2);
        listItemsByType.add(base3);


        // 获取word模板中的所有图表元素，用map存放
        // 为什么不用list保存：查看doc.getRelations()的源码可知，源码中使用了hashMap读取文档图表元素，
        // 对relations变量进行打印后发现，图表顺序和文档中的顺序不一致，也就是说relations的图表顺序不是文档中从上到下的顺序
        Map<String, POIXMLDocumentPart> chartsMap = new HashMap<String, POIXMLDocumentPart>();
        //动态刷新图表
        List<POIXMLDocumentPart> relations = doc.getRelations();
        for (POIXMLDocumentPart poixmlDocumentPart : relations) {
            if (poixmlDocumentPart instanceof XWPFChart) {  // 如果是图表元素
                String str = poixmlDocumentPart.toString();
                System.out.println("str：" + str);
                String key = str.replaceAll("Name: ", "")
                        .replaceAll(" - Content Type: application/vnd\\.openxmlformats-officedocument\\.drawingml\\.chart\\+xml", "").trim();
                System.out.println("key：" + key);

                chartsMap.put(key, poixmlDocumentPart);
            }
        }

        System.out.println("\n图表数量：" + chartsMap.size() + "\n");


        // 第一个图表-条形图
//        POIXMLDocumentPart poixmlDocumentPart0 = chartsMap.get("/word/charts/chart1.xml");
//        new PoiWordTools().replaceBarCharts(poixmlDocumentPart0, titleArr, fldNameArr, listItemsByType);

        // 第二个-柱状图
//        POIXMLDocumentPart poixmlDocumentPart1 = chartsMap.get("/word/charts/chart2.xml");
//        new PoiWordTools().replaceBarCharts(poixmlDocumentPart1, titleArr, fldNameArr, listItemsByType);

        // 第三个图表-多列柱状图
//        doCharts3(chartsMap);

        // 第四个图表-折线图
        doCharts4(chartsMap);

        // 第五个图表-饼图
//        POIXMLDocumentPart poixmlDocumentPart4 = chartsMap.get("/word/charts/chart5.xml");
//        new PoiWordTools().replacePieCharts(poixmlDocumentPart4, titleArr, fldNameArr, listItemsByType);


//        doCharts6(chartsMap);
    }


    public static void doCharts3(Map<String, POIXMLDocumentPart> chartsMap) {
        // 数据准备
        List<String> titleArr = new ArrayList<String>();// 标题
        titleArr.add("姓名");
        titleArr.add("欠款");
        titleArr.add("存款");

        List<String> fldNameArr = new ArrayList<String>();// 字段名
        fldNameArr.add("item1");
        fldNameArr.add("item2");
        fldNameArr.add("item3");

        // 数据集合
        List<Map<String, String>> listItemsByType = new ArrayList<Map<String, String>>();

        // 第一行数据
        Map<String, String> base1 = new HashMap<String, String>();
        base1.put("item1", "老张");
        base1.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base1.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第二行数据
        Map<String, String> base2 = new HashMap<String, String>();
        base2.put("item1", "老李");
        base2.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base2.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第三行数据
        Map<String, String> base3 = new HashMap<String, String>();
        base3.put("item1", "老刘");
        base3.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base3.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");


        listItemsByType.add(base1);
        listItemsByType.add(base2);
        listItemsByType.add(base3);

        POIXMLDocumentPart poixmlDocumentPart2 = chartsMap.get("/word/charts/chart3.xml");
        new PoiWordTools().replaceBarCharts(poixmlDocumentPart2, titleArr, fldNameArr, listItemsByType);
    }


    public static void doCharts4(Map<String, POIXMLDocumentPart> chartsMap) {
        // 数据准备
        List<String> titleArr = new ArrayList<String>();// 标题
        titleArr.add("title");
        titleArr.add("占基金资产净值比例22222（%）");
        titleArr.add("额外的（%）");
        titleArr.add("额外的（%）");

        List<String> fldNameArr = new ArrayList<String>();// 字段名
        fldNameArr.add("item1");
        fldNameArr.add("item2");
        fldNameArr.add("item3");
        fldNameArr.add("item4");

        // 数据集合
        List<Map<String, String>> listItemsByType = new ArrayList<Map<String, String>>();

        // 第一行数据
        Map<String, String> base1 = new HashMap<String, String>();
        base1.put("item1", "材料费用");
        base1.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base1.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base1.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第二行数据
        Map<String, String> base2 = new HashMap<String, String>();
        base2.put("item1", "出差费用");
        base2.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base2.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base2.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第三行数据
        Map<String, String> base3 = new HashMap<String, String>();
        base3.put("item1", "住宿费用");
        base3.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base3.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base3.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");


        listItemsByType.add(base1);
        listItemsByType.add(base2);
        listItemsByType.add(base3);

        POIXMLDocumentPart poixmlDocumentPart2 = chartsMap.get("/word/charts/chart4.xml");
        new PoiWordTools().replaceLineCharts(poixmlDocumentPart2, titleArr, fldNameArr, listItemsByType);
    }


    /**
     * 对应文档中的第6个图表（预处理—分公司情况）
     */
    public static void doCharts6(Map<String, POIXMLDocumentPart> chartsMap) {
        // 数据准备
        List<String> titleArr = new ArrayList<String>();// 标题
        titleArr.add("title");
        titleArr.add("投诉受理量（次）");
        titleArr.add("预处理拦截工单量（次）");
        titleArr.add("拦截率");

        List<String> fldNameArr = new ArrayList<String>();// 字段名
        fldNameArr.add("item1");
        fldNameArr.add("item2");
        fldNameArr.add("item3");
        fldNameArr.add("item4");

        // 数据集合
        List<Map<String, String>> listItemsByType = new ArrayList<Map<String, String>>();

        // 第一行数据
        Map<String, String> base1 = new HashMap<String, String>();
        base1.put("item1", "通辽");
        base1.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base1.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base1.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第二行数据
        Map<String, String> base2 = new HashMap<String, String>();
        base2.put("item1", "呼和浩特");
        base2.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base2.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base2.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第三行数据
        Map<String, String> base3 = new HashMap<String, String>();
        base3.put("item1", "锡林郭勒");
        base3.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base3.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base3.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第四行数据
        Map<String, String> base4 = new HashMap<String, String>();
        base4.put("item1", "阿拉善");
        base4.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base4.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base4.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第五行数据
        Map<String, String> base5 = new HashMap<String, String>();
        base5.put("item1", "巴彦淖尔");
        base5.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base5.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base5.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第六行数据
        Map<String, String> base6 = new HashMap<String, String>();
        base6.put("item1", "兴安");
        base6.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base6.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base6.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第七行数据
        Map<String, String> base7 = new HashMap<String, String>();
        base7.put("item1", "乌兰察布");
        base7.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base7.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base7.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第八行数据
        Map<String, String> base8 = new HashMap<String, String>();
        base8.put("item1", "乌海");
        base8.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base8.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base8.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第九行数据
        Map<String, String> base9 = new HashMap<String, String>();
        base9.put("item1", "赤峰");
        base9.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base9.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base9.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第十行数据
        Map<String, String> base10 = new HashMap<String, String>();
        base10.put("item1", "包头");
        base10.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base10.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base10.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第十一行数据
        Map<String, String> base11 = new HashMap<String, String>();
        base11.put("item1", "呼伦贝尔");
        base11.put("item2", (int) (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base11.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base11.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        // 第十二行数据
        Map<String, String> base12 = new HashMap<String, String>();
        base12.put("item1", "鄂尔多斯");
        base12.put("item2", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base12.put("item3", (int) (1 + Math.random() * (100 - 1 + 1)) + "");
        base12.put("item4", (int) (1 + Math.random() * (100 - 1 + 1)) + "");

        listItemsByType.add(base1);
        listItemsByType.add(base2);
        listItemsByType.add(base3);
        listItemsByType.add(base4);
        listItemsByType.add(base5);
        listItemsByType.add(base6);
        listItemsByType.add(base7);
        listItemsByType.add(base8);
        listItemsByType.add(base9);
        listItemsByType.add(base10);
        listItemsByType.add(base11);
        listItemsByType.add(base12);

        // 下标0的图表-折线图
        POIXMLDocumentPart poixmlDocumentPart5 = chartsMap.get("/word/charts/chart6.xml");
        new PoiWordTools().replaceCombinationCharts(poixmlDocumentPart5, titleArr, fldNameArr, listItemsByType);
    }


}


