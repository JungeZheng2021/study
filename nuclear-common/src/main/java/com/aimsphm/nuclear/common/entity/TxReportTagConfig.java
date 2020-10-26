package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.File;

/**
 * 报告生成测点配置表
 *
 * @author MILLA
 * @since 2020-06-12
 */
@Data
public class TxReportTagConfig extends ModelBase {
    private static final long serialVersionUID = 4862639240883573828L;
    /**
     * 子系统
     */
    private Long subSystemId;
    /**
     * 图标名称
     */
    private String title;
    /**
     * 占位符
     */
    private String placeholder;
    /**
     * 类型：1：趋势，2：阈值
     */
    private Integer category;
    /**
     * 测点列表
     */
    private String tagList;
    /**
     * y轴单位
     */
    private String yUnit;
    /**
     * x轴单位
     */
    private String xUnit;
    /**
     * 图例title
     */
    private String legend;
    /**
     * 生成的图片
     */
    @TableField(exist = false)
    private File image;
}