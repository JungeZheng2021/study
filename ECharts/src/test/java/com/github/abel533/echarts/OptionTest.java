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

package com.github.abel533.echarts;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.data.LineData;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.style.LineStyle;
import com.github.abel533.echarts.util.EnhancedOption;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: OptionTest
 *
 * @author liuzh
 * @since liuzh(2014 - 08 - 26 14 : 08)
 */
public class OptionTest {

    @Test
    public void basicOption() {
        EnhancedOption option = new EnhancedOption();
//        option.legend().padding(5).itemGap(10).type(LegendType.scroll).data("ios7", "android4");
//        option.toolbox().show(true).feature(Tool.dataView, Tool.saveAsImage, Tool.dataZoom, Tool.magicType);
        option.tooltip().trigger(Trigger.item);
        option.xAxis(new CategoryAxis().data("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"));
        option.yAxis(new ValueAxis().boundaryGap(new Double[]{0.1, 0.1}).splitNumber(10));
        Line ground = new Line();
        ground.data(112, 25, 8, 36, 203, 343, 454, 89, 343, 123, 45, 123);
        ground.itemStyle().normal().lineStyle().width(20).color("lightgreen");
        ground.symbol("none");//itemStyle().normal().color("lightgreen").borderWidth(0);
        ground.itemStyle().normal().color("lightgreen").borderWidth(0);
        ground.smooth(true);
        option.series(ground);

        Line line = new Line();
        line.data(112, 23, 45, 56, 233, 343, 454, 89, 343, 123, 45, 123);
        line.markLine().symbol("none").data(new LineData().yAxis(300).name("高报"));
        line.markLine().symbol("none").data(new LineData().yAxis(100).name("低报"));
//        line.markLine().symbol("none").data(new LineData().type(MarkType.min).name("低报"));
        Map<Object, Object> left = Maps.newHashMap();
        left.put("lt", 100);
        left.put("symbolSize", 8);
        left.put("symbol", "circle");

        Map<Object, Object> right = Maps.newHashMap();
        right.put("gt", 300);
        right.put("symbolSize", 8);
        right.put("symbol", "circle");
        Map<Object, Object> middle = Maps.newHashMap();
        middle.put("gte", 100);
        middle.put("lte", 300);
        middle.put("symbol", "none");
        line.lineStyle().normal().color("blue");
        line.itemStyle().normal().color("red").borderWidth(0);
        line.smooth(true);
        option.series(line).visualMapNew().show(false).pieces(new Object[]{left, middle, right}).itemSymbol(Symbol.none);


        option.view();
    }
}