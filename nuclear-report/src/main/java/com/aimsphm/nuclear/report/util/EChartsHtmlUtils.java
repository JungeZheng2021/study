package com.aimsphm.nuclear.report.util;

import com.github.abel533.echarts.AxisPointer;
import com.github.abel533.echarts.Label;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.TimeAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.*;
import com.github.abel533.echarts.data.BasicData;
import com.github.abel533.echarts.data.Data;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.series.Scatter;
import com.github.abel533.echarts.style.ItemStyle;
import com.github.abel533.echarts.style.LineStyle;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.ReportConstant.ECHARTS_HTML_SUFFIX;
import static com.aimsphm.nuclear.common.constant.ReportConstant.ECHARTS_TEMP_DIR;


/**
 * @Package: com.aimsphm.nuclear.report.util
 * @Description: <获取echarts-html工具类>
 * @Author: MILLA
 * @CreateDate: 2020/5/7 13:08
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/7 13:08
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public final class EChartsHtmlUtils {
    private EChartsHtmlUtils() {
    }

    /**
     * 折线图有多条折线
     *
     * @param options 数据
     * @return
     */
    public static String lineEChartsHtmlMultiterm(List<Line> options) {
        if (CollectionUtils.isEmpty(options)) {
            return null;
        }
        GsonOption option = new GsonOption();
        option.tooltip(new Tooltip().trigger(Trigger.axis).showDelay(0).axisPointer(new AxisPointer().type(PointerType.cross)
                .lineStyle(new LineStyle().type(LineType.dashed).width(1))));
        List<String> titleList = options.stream().map(Line::getName).collect(Collectors.toList());
        option.legend(titleList.toArray(new String[]{}));

        option.yAxis(new ValueAxis().scale(true));

        option.xAxis(new TimeAxis().scale(true));

        option.series(options.toArray(new Line[]{}));
        return option.exportToHtml(ECHARTS_TEMP_DIR, SeriesType.line.name() + UUIDUtils.randomUUID() + ECHARTS_HTML_SUFFIX);
    }

    /**
     * 折线图
     *
     * @param options 数据
     * @return
     */
    public static String lineEChartsHtml(Object[] options) {
        if (Objects.isNull(options) || options.length == 0) {
            return null;
        }
        GsonOption option = new GsonOption();
        option.tooltip(new Tooltip().trigger(Trigger.axis).showDelay(0).axisPointer(new AxisPointer().type(PointerType.cross)
                .lineStyle(new LineStyle().type(LineType.dashed).width(1))));
        option.calculable(true);

        option.yAxis(new ValueAxis().scale(true));

        option.xAxis(new TimeAxis().scale(true));

        Line line = new Line();
        line.symbol(Symbol.none).data(options).itemStyle().normal().color("#3f90ea");
        option.series(line);
        return option.exportToHtml(ECHARTS_TEMP_DIR, SeriesType.line.name() + UUIDUtils.randomUUID() + ECHARTS_HTML_SUFFIX);
    }

    /**
     * 柱状图
     *
     * @param options    数据
     * @param xTitleList x轴显示标题列表
     * @return
     */
    public static String barEChartsHtml(List<Bar> options, List<String> xTitleList) {
        if (CollectionUtils.isEmpty(options) || CollectionUtils.isEmpty(xTitleList)) {
            return null;
        }
        GsonOption option = new GsonOption();
        option.tooltip().trigger(Trigger.axis).axisPointer().type(PointerType.shadow);
        List<String> titleList = options.stream().map(Bar::getName).collect(Collectors.toList());
        option.legend(titleList.toArray(new String[]{}));

        option.xAxis(new CategoryAxis().data(xTitleList.toArray(new String[]{})));
        option.yAxis(new ValueAxis());

        option.series(options.toArray(new Bar[]{}));
        return option.exportToHtml(ECHARTS_TEMP_DIR, SeriesType.bar.name() + UUIDUtils.randomUUID() + ECHARTS_HTML_SUFFIX);
    }

    /**
     * 散点图
     *
     * @param options 数据
     * @param title   散点图名称
     * @return
     */
    public static String scatterEChartsHtml(List<Scatter> options, String title) {
        if (CollectionUtils.isEmpty(options)) {
            return null;
        }
        GsonOption option = new GsonOption();
        option.title().text(title).x(X.center).y(Y.bottom);
        List<String> titleList = options.stream().map(Scatter::getName).collect(Collectors.toList());
        option.legend(titleList.toArray());
        option.xAxis(new TimeAxis().scale(true));
        option.yAxis(new ValueAxis().scale(true));

        option.series(options.toArray(new Scatter[]{}));
        return option.exportToHtml(ECHARTS_TEMP_DIR, SeriesType.scatter.name() + UUIDUtils.randomUUID() + ECHARTS_HTML_SUFFIX);
    }

    /**
     * 饼状图
     *
     * @param options 数据
     * @param title   饼图名称
     * @return
     */
    public static String pieEChartsHtml(List<Data> options, String title) {
        if (CollectionUtils.isEmpty(options)) {
            return null;
        }
        List<String> titleList = options.stream().map(BasicData::getName).collect(Collectors.toList());
        GsonOption option = new GsonOption();
        option.title().text(title).x("31%").y(Y.center);
        option.legend().data(titleList).orient(Orient.vertical).x("70%").y(Y.center);
        ItemStyle itemStyle = new ItemStyle();
        itemStyle.normal().label(new Label().formatter("{c}次({d}%)"));
        Pie pie = new Pie();
        Data[] data = options.toArray(new Data[]{});
        pie.data(data).radius("40%", "60%").center("40%", "50%").itemStyle(itemStyle);
        option.series(pie);
        return option.exportToHtml(ECHARTS_TEMP_DIR, SeriesType.pie.name() + UUIDUtils.randomUUID() + ECHARTS_HTML_SUFFIX);
    }
}
