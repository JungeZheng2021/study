package com.aimsphm.nuclear.common.mapper;

import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavorite;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 振动分析收藏夹
 *
 * @author lu.yi
 * @since 2020-08-14
 */
@Repository
public interface MdVibrationAnalysisFavoriteMapper extends BaseMapper<MdVibrationAnalysisFavorite> {

    List<MdVibrationAnalysisFavorite> listAnalysisFavorite(Page<MdVibrationAnalysisFavorite> page, @Param("query") MdVibrationAnalysisFavorite query, @Param("time") TimeRangeQueryBO timeBo);
}