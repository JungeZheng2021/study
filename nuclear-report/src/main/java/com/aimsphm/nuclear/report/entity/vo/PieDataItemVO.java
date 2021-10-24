package com.aimsphm.nuclear.report.entity.vo;

import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import lombok.Data;

import java.util.List;

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
