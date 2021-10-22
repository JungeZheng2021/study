package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <振动分析用户备注实体>
 * @Author: MILLA
 * @CreateDate: 2021-01-14
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("analysis_favorite_remark")
@ApiModel(value = "振动分析用户备注实体")
public class AnalysisFavoriteRemarkDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6009474376561836290L;

    @ApiModelProperty(value = "收藏id", notes = "")
    private Long favoriteId;

    @ApiModelProperty(value = "备注信息", notes = "")
    private String remark;

}