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

package com.github.abel533.echarts.samples.pie;

import com.github.abel533.echarts.Label;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.data.Data;
import com.github.abel533.echarts.data.LineData;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Funnel;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.util.EnhancedOption;
import lombok.experimental.var;
import org.junit.Test;

/**
 * 复杂的时间轴效果
 *
 * @author liuzh
 */
public class PieTest5 {

    @Test
    public void test() {
        getHtml(true);
    }

    public static String getHtml(boolean isShow) {
        GsonOption option = new GsonOption();
        option.title().text("报警分布").x("35%").y(Y.center);
        option.legend().data("温度相关", "振动相关", "电信号相关", "流量相关", "其他测点").orient(Orient.vertical).x("70%").y(Y.center);
        ItemStyle itemStyle = new ItemStyle();
        itemStyle.normal().label(new Label().formatter("{c}次({d}%)"));
        Pie pie = new Pie();
        pie.data(new Data("温度相关", 34), new Data("振动相关", 12),
                new Data("电信号相关", 28), new Data("流量相关", 87),
                new Data("其他测点", 3)
        ).radius("30%", "60%").center("40%", "50%").itemStyle(itemStyle);
        option.series(pie);
        String exportToHtml = option.exportToHtml("pie5.html");
        if (isShow) {
            option.view();
        }
        return exportToHtml;
    }

    public static String getHtml() {
        return getHtml(false);
    }
}
