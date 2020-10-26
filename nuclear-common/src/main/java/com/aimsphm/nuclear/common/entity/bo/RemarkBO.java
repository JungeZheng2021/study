package com.aimsphm.nuclear.common.entity.bo;

import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavorite;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity.bo
 * @Description: <振动分析收藏夹备注>
 * @Author: milla
 * @CreateDate: 2020/08/21 10:12
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/21 10:12
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class RemarkBO extends MdVibrationAnalysisFavorite {
    /**
     * 收藏id
     */
    private Long favoriteId;
    /**
     * 备注信息
     */
    private String remark;
}
