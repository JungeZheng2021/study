package com.milla.report.service.impl;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.PointerType;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.data.BasicData;
import com.github.abel533.echarts.data.Data;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Package: com.aimsphm.nuclear.report.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/28 18:02
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/28 18:02
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service("bar")
public class BarOperationServiceImpl {

    public String getEChartsHtml(List<Data> options, String title) {
        if (CollectionUtils.isEmpty(options)) {
            return null;
        }
        List<String> titleList = options.stream().map(BasicData::getName).collect(Collectors.toList());
        List<Object> valueList = options.stream().map(BasicData::value).collect(Collectors.toList());
        GsonOption option = new GsonOption();
        option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.shadow);
        option.calculable(true);
        option.yAxis(new ValueAxis());
        option.xAxis(new CategoryAxis().data(titleList.toArray(new Object[]{})));

        Bar bar = new Bar();
        bar.itemStyle().normal().color("#3d63d8").label().show(true).color("#3d63d8").position("top");
        bar.barWidth(25);//设置柱体的宽度
        bar.data(valueList.toArray(new Object[]{}));
//        Bar bar1 = new Bar();
//        bar1.itemStyle().normal().color("#3d63d8").label().show(true).color("#3d63d8").position("top");
//        bar1.barWidth(25);//设置柱体的宽度
//        bar1.data(valueList.toArray(new Object[]{}));

        option.series(bar);
        String exportToHtml = option.exportToHtml("/tmp/echarts/", "bar.html");
        option.view();
        return exportToHtml;
    }

    public static void main(String[] args) {
        List<Data> data = Lists.newArrayList(new Data("温度相关", 34), new Data("振动相关", 12),
                new Data("电信号相关", 28), new Data("流量相关", 87),
                new Data("其他测点", 3));
        BarOperationServiceImpl pie = new BarOperationServiceImpl();
//        pie.getEChartsHtml(data, "我是测试的图片");
//        pie.test();
        pie.demo();
    }

    private void demo() {
        GsonOption option = new GsonOption();
        option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.shadow);
        option.legend("主泵1A", "主泵1B", "主泵2A", "主泵2B");
//        option.toolbox().show(true).feature(Tool.mark, Tool.dataView, new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
//        option.calculable(true);
        option.xAxis(new CategoryAxis().data("近一天", "近三天", "近一周", "近半月", "近一月"));
        option.yAxis(new ValueAxis());

        Bar bar = new Bar("主泵1A");
        bar.stack("主泵1A");
        bar.barWidth(25);
        bar.itemStyle().normal().label().show(true).position("top");
        bar.barGap("0");
        bar.data(320, 302, 301, 334, 390);

        Bar bar2 = new Bar("主泵1B");
        bar2.barWidth(25);
        bar2.stack("主泵1B");
        bar2.itemStyle().normal().label().show(true).position("top");
        bar2.data(320, 334, 390, 330, 320);

        Bar bar3 = new Bar("主泵2A");
        bar3.barWidth(25);
        bar3.stack("主泵2A");
        bar3.itemStyle().normal().label().show(true).position("top");
        bar3.data(120, 132, 90, 230, 210);

        Bar bar4 = new Bar("主泵2B");
        bar4.barWidth(25);
        bar4.stack("主泵2B");
        bar4.itemStyle().normal().label().show(true).position("top");
        bar4.data(150, 212, 201, 154, 190);


        option.series(bar, bar2, bar3, bar4);
        String s = option.exportToHtml("/tmp/echarts/", "bar4.html");
        option.view();
    }

    private void test() {
        GsonOption option = new GsonOption();
        option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.shadow);
        option.legend("近一天", "近三天", "近一周", "近半月", "近一月");
        option.toolbox().show(true).feature(Tool.mark, Tool.dataView, new MagicType(Magic.line, Magic.bar).show(true), Tool.restore, Tool.saveAsImage);
        option.calculable(true);
        option.xAxis(new CategoryAxis().data("主泵1A", "主泵1B", "主泵2A", "主泵2B"));
        option.yAxis(new ValueAxis());

        Bar bar = new Bar("近一天");
        bar.stack("近一天");
        bar.itemStyle().normal().label().show(true).position("top");
        bar.data(320, 302, 301, 334);
        bar.barGap("0");

        Bar bar2 = new Bar("近三天");
        bar2.stack("近三天");
        bar2.itemStyle().normal().label().show(true).position("top");
        bar2.data(320, 334, 390, 330);

        Bar bar3 = new Bar("近一周");
        bar3.stack("近一周");
        bar3.itemStyle().normal().label().show(true).position("top");
        bar3.data(120, 132, 90, 230);

        Bar bar4 = new Bar("近半月");
        bar4.stack("近半月");
        bar4.itemStyle().normal().label().show(true).position("top");
        bar4.data(150, 212, 201, 154);

        Bar bar5 = new Bar("近一月");
        bar5.stack("近一月");
        bar5.itemStyle().normal().label().show(true).position("top");
        bar5.data(150, 212, 201, 154);


        option.series(bar, bar2, bar3, bar4, bar5);
        String s = option.exportToHtml("bar4.html");
        option.view();
    }
}
