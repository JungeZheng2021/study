package com.aimsphm.nuclear.report.entity.vo;

import lombok.Data;

import java.io.File;

/**
 * @Package: com.aimsphm.nuclear.report.entity.vo
 * @Description: <频谱数据>
 * @Author: MILLA
 * @CreateDate: 2021/03/09 16:50
 * @UpdateUser: MILLA
 * @UpdateDate: 2021/03/09 16:50
 * @UpdateRemark: <>
 * @Version: 1.0
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
