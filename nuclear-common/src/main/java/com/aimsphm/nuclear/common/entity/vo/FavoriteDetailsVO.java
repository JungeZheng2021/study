package com.aimsphm.nuclear.common.entity.vo;

import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavorite;
import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavoriteRemark;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 收藏夹详情VO
 *
 * @author lu.yi
 * @since 2020-08-14
 */
@Data
public class FavoriteDetailsVO {
    @ApiModelProperty(value = "收藏夹信息")
    private MdVibrationAnalysisFavorite favorite;
    @ApiModelProperty(value = "备注列表")
    private List<MdVibrationAnalysisFavoriteRemark> remarkList;
}