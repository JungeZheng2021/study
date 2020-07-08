/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.abel533.echarts.samples.scatter;

import com.github.abel533.echarts.AxisPointer;
import com.github.abel533.echarts.Title;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.data.ScatterData;
import com.github.abel533.echarts.series.Scatter;
import com.github.abel533.echarts.style.LineStyle;
import com.github.abel533.echarts.util.EnhancedOption;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author liuzh
 */
public class ScatterHealthTest {

    @Test
    public void test() {
        getHtml();
    }

    public static String getHtml() {
        //地址：http://echarts.baidu.com/doc/example/scatter2.html
        EnhancedOption option = new EnhancedOption();
        Title title = new Title();
        title.setText("1A主泵状态变化表");
        title.setTextAlign(X.left);
        option.setTitle(title);
        option.tooltip(new Tooltip()
                .trigger(Trigger.axis)
                .showDelay(0)
                .axisPointer(new AxisPointer().type(PointerType.cross)
                        .lineStyle(new LineStyle()
                                .type(LineType.dashed).width(1))));
        option.legend("健康", "待观察", "预警", "报警", "停运");
        option.toolbox().show(true).feature(Tool.mark, Tool.dataZoom, Tool.dataView, Tool.restore, Tool.saveAsImage);
        ValueAxis valueAxis = new ValueAxis().power(1).splitNumber(4).scale(true);
        valueAxis.axisTick().show();
        CategoryAxis data = new CategoryAxis().data("周一", "周二", "周三", "周四", "周五", "周六", "周日", "周二", "周三", "周四", "周五", "周六", "周日");
        data.axisTick().show();
        option.xAxis(data);
        option.yAxis(valueAxis);
        //注：这里的结果是一种圆形一种方形，是因为默认不设置形状时，会循环形状数组
        Map<Integer, List<ScatterData>> map = listData();

        Scatter zero = new Scatter("健康").symbolSize("10").data("0", "0", "0", "", "", "", "0", "0", "0", "", "", "", "0");
        zero.itemStyle().normal().color("green");

        Scatter one = new Scatter("待观察").symbolSize("10").data("", "1", "", "", "1", "1", "", "0", "0", "", "", "", "0");
        one.itemStyle().normal().color("lightgreen");

        Scatter two = new Scatter("预警").symbolSize("10").data("", "", "2", "", "", "2", "", "0", "0", "", "", "", "0");
        two.itemStyle().normal().color("orange");

        Scatter three = new Scatter("报警").symbolSize("10").data("", "3", "", "", "", "", "3", "0", "0", "", "", "", "0");
        three.itemStyle().normal().color("red");
//
        Scatter four = new Scatter("停运").symbolSize("10").data("", "", "", "", "", "4", "", "0", "0", "", "", "", "0");
        four.itemStyle().normal().color("gray");

        option.series(zero, one, two, three, four);
        String exportToHtml = option.exportToHtml("ScatterHealthTest.html");
        option.view();
        return exportToHtml;
    }


    private static Map<Integer, List<ScatterData>> listData() {
        Map<Integer, List<ScatterData>> data = Maps.newHashMap();
        List<ScatterData> zero = Lists.newArrayList();
        List<ScatterData> one = Lists.newArrayList();
        List<ScatterData> two = Lists.newArrayList();
        List<ScatterData> three = Lists.newArrayList();
        List<ScatterData> four = Lists.newArrayList();
        data.put(0, zero);
        data.put(1, one);
        data.put(2, two);
        data.put(3, three);
        data.put(4, four);
        for (int i = 0; i < 20; i++) {
//            if (i % 5 == 0) {
            zero.add(new ScatterData(System.currentTimeMillis(), i % 5, Math.abs(random())));
//            }
//            if (i % 5 == 1) {
            one.add(new ScatterData(nowDateString(), i % 5, 1));
//            }
//            if (i % 5 == 2) {
            two.add(new ScatterData(nowDateString(), i % 5, 3));
//            }
//            if (i % 5 == 3) {
            three.add(new ScatterData(nowDateString(), i % 5, 4));
//            }
//            if (i % 5 == 4) {
            four.add(new ScatterData(nowDateString(), i % 5, 5));
//            }
        }
        return data;
    }

    private static String nowDateString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
//        Date from = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
        return time.format(dateTimeFormatter);
    }

    private static ScatterData[] randomDataArray(int type) {

        ScatterData[] scatters = new ScatterData[50 * type + 3];
        for (int i = 0; i < scatters.length; i++) {
            scatters[i] = new ScatterData(i, i % 4, Math.abs(random()));
        }
        return scatters;
    }

    private static int random() {
        int i = (int) Math.round(Math.random() * 100);
        return (i * (i % 2 == 0 ? 1 : -1));
    }
}
