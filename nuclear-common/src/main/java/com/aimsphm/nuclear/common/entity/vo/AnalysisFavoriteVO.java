package com.aimsphm.nuclear.common.entity.vo;

import com.aimsphm.nuclear.common.entity.AnalysisFavoriteDO;
import com.aimsphm.nuclear.common.entity.AnalysisFavoriteRemarkDO;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <振动分析收藏夹实体>
 * @Author: MILLA
 * @CreateDate: 2021-01-14
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("analysis_favorite")
@ApiModel(value = "振动分析收藏夹实体")
public class AnalysisFavoriteVO extends AnalysisFavoriteDO {

    @ApiModelProperty(value = "是否有备注", notes = "1:已经备注 0:没有备注")
    private Boolean remarked;

    @ApiModelProperty(value = "是否有备注")
    private String remark;

    @ApiModelProperty(value = "备注列表")
    private List<AnalysisFavoriteRemarkDO> remarkList;

}