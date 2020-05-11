package com.milla.report.service.impl;

import com.github.abel533.echarts.Label;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.Y;
import com.github.abel533.echarts.data.BasicData;
import com.github.abel533.echarts.data.Data;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.style.ItemStyle;
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
@Service("pie")
public class PieOperationServiceImpl {

    public String getEChartsHtml(List<Data> options, String title) {
        if (CollectionUtils.isEmpty(options)) {
            return null;
        }
        List<String> titleList = options.stream().map(BasicData::getName).collect(Collectors.toList());
        GsonOption option = new GsonOption();
        option.title().text(title).x("35%").y(Y.center);
        option.legend().data(titleList).orient(Orient.vertical).x("70%").y(Y.center);
        ItemStyle itemStyle = new ItemStyle();
        itemStyle.normal().label(new Label().formatter("{c}次({d}%)"));
        Pie pie = new Pie();
        Data[] data = options.toArray(new Data[]{});
        pie.data(data).radius("40%", "60%").center("40%", "50%").itemStyle(itemStyle);
        option.series(pie);
        String exportToHtml = option.exportToHtml("/tmp/echarts/", "pie5.html");
        option.view();
        return exportToHtml;
    }

    public static void main(String[] args) {
        List<Data> data = Lists.newArrayList(new Data("温度相关", 34), new Data("振动相关", 12),
                new Data("电信号相关", 28), new Data("流量相关", 87),
                new Data("其他测点", 3));
        PieOperationServiceImpl pie = new PieOperationServiceImpl();
        pie.getEChartsHtml(data, "我是测试的图片");

    }
}
