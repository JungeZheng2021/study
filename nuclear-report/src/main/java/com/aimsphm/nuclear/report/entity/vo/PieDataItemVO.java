package com.aimsphm.nuclear.report.entity.vo;

import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.report.entity.vo
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2021/03/09 16:50
 * @UpdateUser: milla
 * @UpdateDate: 2021/03/09 16:50
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class PieDataItemVO {
    /**
     * 单位
     */
    private String unit;
    /**
     * 数据表名称
     */
    private String title;
    /**
     * 数据
     */
    private List<LabelVO> data;
}
