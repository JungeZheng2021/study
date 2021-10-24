package com.aimsphm.nuclear.report.entity.vo;

import lombok.Data;

import java.io.File;

/**
 * <p>
 * 功能描述:频谱数据
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/03/09 16:50
 */
@Data
public class GraphDataItemVO {
    /**
     * 单位
     */
    private String title;
    /**
     * 数据表名称
     */
    private String desc;
    /**
     * 图片
     */
    private File image;
}
