package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.File;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <报告生成测点配置表实体>
 * @Author: MILLA
 * @CreateDate: 2021-03-09
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-03-09
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("biz_report_config")
@ApiModel(value = "报告生成测点配置表实体")
public class BizReportConfigDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -5162136401791653551L;

    @ApiModelProperty(value = "设备信息id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "子系统", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "图标名称", notes = "")
    private String title;

    @ApiModelProperty(value = "占位符", notes = "")
    private String placeholder;

    @ApiModelProperty(value = "类型：1：折线图，2：柱状图，11：折线图(多Y轴)", notes = "")
    private Integer category;

    @ApiModelProperty(value = "测点列表", notes = "")
    private String pointIds;

    @ApiModelProperty(value = "y轴单位", notes = "")
    private String yUnit;

    @ApiModelProperty(value = "x轴单位", notes = "")
    private String xUnit;

    @ApiModelProperty(value = "图例title", notes = "")
    private String legend;

    @ApiModelProperty(value = "颜色表", notes = "例子:['#ff0000', '#ff7700', '#60b720', '#999']")
    private String color;

    @ApiModelProperty(value = "x轴坐标数据", notes = "例子：['一天', '三天', '一周']")
    private String xAxisData;

    @ApiModelProperty(value = "备注", notes = "")
    private String remark;

    @TableField(exist = false)
    @ApiModelProperty(value = "生成的图片", notes = "一个和多个")
    private File image;
}