package com.milla.report;

import com.github.abel533.echarts.AxisPointer;
import com.github.abel533.echarts.Label;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.data.Data;
import com.github.abel533.echarts.data.ScatterData;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.series.Scatter;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.LineStyle;
import com.milla.report.util.ScreenshotUtils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Random;

@SpringBootApplication
@RestController
public class ReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportApplication.class, args);
    }

    @Autowired
    @Qualifier("phantomJSDriver")
    private WebDriver webDriver;
    @Autowired
    private ScreenshotUtils screenshot;

    @GetMapping("pie/{flag}")
    public void test(@PathVariable int flag) throws IOException, InterruptedException {
        long currentTimeMillis = System.currentTimeMillis();
        GsonOption option = new GsonOption();
        //地址：http://echarts.baidu.com/doc/example/pie6.html
        ItemStyle dataStyle = new ItemStyle();
        dataStyle.normal().label(new Label().show(false)).labelLine().show(false);

        ItemStyle placeHolderStyle = new ItemStyle();
        placeHolderStyle.normal().color("rgba(0,0,0,0)").label(new Label().show(false)).labelLine().show(false);
        placeHolderStyle.emphasis().color("rgba(0,0,0,0)");

        option.title().text("你幸福吗？")
                .subtext("From ExcelHome")
                .sublink("http://e.weibo.com/1341556070/AhQXtjbqh")
                .x(X.center)
                .y(Y.center)
                .itemGap(20)
                .textStyle().color("rgba(30,144,255,0.8)")
//                .fontFamily("微软雅黑")
                .fontSize(35)
                .fontWeight("bolder");
        option.tooltip().show(true).formatter("{a} <br/>{b} : {c} ({d}%)");
        option.legend().orient(Orient.vertical)
                .x("(function(){return document.getElementById('main').offsetWidth / 2;})()")
                .y(56)
                .itemGap(12)
                .data("68%的人表示过的不错", "29%的人表示生活压力很大", "3%的人表示“我姓曾”");
        option.toolbox().show(false).feature(Tool.mark, Tool.dataView, Tool.restore, Tool.saveAsImage);

        Pie p1 = new Pie("1");
        p1.clockWise(false).radius(125, 150).itemStyle(dataStyle)
                .data(new Data("68%的人表示过的不错", 68), new Data("invisible", 32).itemStyle(placeHolderStyle));

        Pie p2 = new Pie("2");
        p2.clockWise(false).radius(100, 125).itemStyle(dataStyle)
                .data(new Data("29%的人表示生活压力很大", 29), new Data("invisible", 71).itemStyle(placeHolderStyle));

        Pie p3 = new Pie("3");
        p3.clockWise(false).radius(75, 100).itemStyle(dataStyle)
                .data(new Data("3%的人表示“我姓曾”", 3), new Data("invisible", 97).itemStyle(placeHolderStyle));

        option.series(p1, p2, p3);
        String exportToHtml = option.exportToHtml("/tmp/echarts/", "pie6.html");
        File srcFile;
        if (flag == 1) {
            srcFile = screenshot.getScreenshotAs(exportToHtml, OutputType.FILE, 300L);

        } else if (flag == 2) {
            srcFile = screenshot.getScreenshotAsByDriver(exportToHtml, OutputType.FILE, 300L);
        } else {
            srcFile = screenshot.getScreenshotAs(null, OutputType.FILE, 300L);
        }
        FileUtils.copyFile(srcFile, new File("/tmp/echarts/image" + System.currentTimeMillis() + ".png"));
        System.out.println("共计用时：" + (System.currentTimeMillis() - currentTimeMillis));
    }

    @GetMapping("scatter")
    public void scatter() throws IOException, InterruptedException {
        long currentTimeMillis = System.currentTimeMillis();
        GsonOption option = new GsonOption();
        //地址：http://echarts.baidu.com/doc/example/pie6.html
        option.tooltip(new Tooltip()
                .trigger(Trigger.axis)
                .showDelay(0)
                .axisPointer(new AxisPointer().type(PointerType.cross)
                        .lineStyle(new LineStyle()
                                .type(LineType.dashed).width(1))));
        option.legend("scatter1");
        option.toolbox().show(true).feature(Tool.mark, Tool.dataZoom, Tool.dataView, Tool.restore, Tool.saveAsImage);
        ValueAxis valueAxis = new ValueAxis().power(1).splitNumber(4).scale(true);
        option.xAxis(valueAxis);
        option.yAxis(valueAxis);
        //注：这里的结果是一种圆形一种方形，是因为默认不设置形状时，会循环形状数组
        option.series(
                new Scatter("scatter1").symbolSize("20").data(randomDataArray())
        );
        option.view();
        String exportToHtml = option.exportToHtml("/tmp/echarts/", "scatter2.html");
        File srcFile = screenshot.getScreenshotAs(exportToHtml, OutputType.FILE, 300L);
        FileUtils.copyFile(srcFile, new File("/tmp/echarts/image" + System.currentTimeMillis() + ".png"));
        System.out.println("共计用时：" + (System.currentTimeMillis() - currentTimeMillis));
    }

    private static ScatterData[] randomDataArray() {
        ScatterData[] scatters = new ScatterData[100];
        Random random = new Random(1);
        for (int i = 0; i < scatters.length; i++) {
            scatters[i] = new ScatterData(i, random.nextInt(), i);
        }
        return scatters;
    }

    @GetMapping("line")
    public void demo() throws IOException, InterruptedException {
        long currentTimeMillis = System.currentTimeMillis();
        GsonOption option = new GsonOption();
        //地址：http://echarts.baidu.com/doc/example/map.html
        option.title("某楼盘销售情况", "纯属虚构");
        option.tooltip().trigger(Trigger.axis);
        option.legend("意向", "预购", "成交");
        option.toolbox().show(true).feature(Tool.mark,
                Tool.dataView,
                new MagicType(Magic.line, Magic.bar, Magic.stack, Magic.tiled),
                Tool.restore,
                Tool.saveAsImage).padding(20);
        option.calculable(true);
        option.xAxis(new CategoryAxis().boundaryGap(false).data("周一", "周二", "周三", "周四", "周五", "周六", "周日"));
        option.yAxis(new ValueAxis());

        Line l1 = new Line("成交");
        l1.smooth(true).itemStyle().normal().areaStyle().typeDefault();
        l1.data(10, 12, 21, 54, 260, 830, 710);

        Line l2 = new Line("预购");
        l2.smooth(true).itemStyle().normal().areaStyle().typeDefault();
        l2.data(30, 182, 434, 791, 390, 30, 10);

        Line l3 = new Line("意向");
        l3.smooth(true).itemStyle().normal().areaStyle().typeDefault();
        l3.data(1320, 1132, 601, 234, 120, 90, 20);

        option.series(l1, l2, l3);
        option.view();
        String exportToHtml = option.exportToHtml("/tmp/echarts/", "saleInfo.html");
        webDriver.get("file:///" + exportToHtml);
        Thread.sleep(1000L);
        WebElement canvas = webDriver.findElement(By.tagName("canvas"));
        Dimension size = canvas.getSize();
        int height = size.getHeight();
        int width = size.getWidth();
        System.out.println("height:" + height + "  -width:" + width);
//        File srcFile = canvas.getScreenshotAs(OutputType.FILE);
        File srcFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(srcFile, new File("/tmp/echarts/image" + System.currentTimeMillis() + ".png"));
        System.out.println("共计用时：" + (System.currentTimeMillis() - currentTimeMillis));
    }

    @GetMapping("bar")
    public void get() throws IOException, InterruptedException {
        long currentTimeMillis = System.currentTimeMillis();
        GsonOption option = new GsonOption();
        option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.shadow);
        option.legend("直接访问", "邮件营销", "联盟广告", "视频广告", "搜索引擎");
        option.toolbox().show(true).feature(Tool.mark, Tool.dataView, new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
        option.calculable(true);
        option.yAxis(new CategoryAxis().data("周一", "周二", "周三", "周四", "周五", "周六", "周日"));
        option.xAxis(new ValueAxis());

        Bar bar = new Bar("直接访问");
        bar.stack("总量");
        bar.itemStyle().normal().label().show(true).position("insideRight");
        bar.data(320, 302, 301, 334, 390, 330, 320);

        Bar bar2 = new Bar("邮件营销");
        bar2.stack("总量");
        bar2.itemStyle().normal().label().show(true).position("insideRight");
        bar2.data(320, 302, 301, 334, 390, 330, 320);

        Bar bar3 = new Bar("联盟广告");
        bar3.stack("总量");
        bar3.itemStyle().normal().label().show(true).position("insideRight");
        bar3.data(120, 132, 101, 134, 90, 230, 210);

        Bar bar4 = new Bar("视频广告");
        bar4.stack("总量");
        bar4.itemStyle().normal().label().show(true).position("insideRight");
        bar4.data(150, 212, 201, 154, 190, 330, 410);

        Bar bar5 = new Bar("搜索引擎");
        bar5.stack("总量");
        bar5.itemStyle().normal().label().show(true).position("insideRight");
        bar5.data(820, 832, 901, 934, 1290, 1330, 1320);

        option.series(bar, bar2, bar3, bar4, bar5);
        String exportToHtml = option.exportToHtml("/tmp/echarts/", "bar.html");
        webDriver.get("file:///" + exportToHtml);
        Thread.sleep(300L);
        WebElement canvas = webDriver.findElement(By.tagName("canvas"));
        File srcFile = canvas.getScreenshotAs(OutputType.FILE);
//        File srcFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        Dimension size = canvas.getSize();
        int height = size.getHeight();
        int width = size.getWidth();
        System.out.println("height:" + height + "  -width:" + width);
        FileUtils.copyFile(srcFile, new File("/tmp/echarts/image" + System.currentTimeMillis() + ".png"));
        System.out.println("共计用时：" + (System.currentTimeMillis() - currentTimeMillis));
    }
}
