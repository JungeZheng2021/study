package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavorite;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 振动分析收藏夹
 *
 * @author lu.yi
 * @since 2020-08-14
 */
public interface MdVibrationAnalysisFavoriteService extends IService<MdVibrationAnalysisFavorite> {

    /**
     * 查询所有的收藏夹
     *
     * @param page   分页参数
     * @param query  查询条件
     * @param timeBo
     * @return
     */
    Object listAnalysisFavorite(Page<MdVibrationAnalysisFavorite> page, MdVibrationAnalysisFavorite query, TimeRangeQueryBO timeBo);

    /**
     * 删除收藏
     *
     * @param id 要删除的id
     * @return
     */
    Integer removeFavorite(Long id);


    /**
     * 添加收藏夹
     *
     * @param favorite 收藏夹信息
     * @return
     */
    Boolean saveMdVibrationAnalysisFavorite(MdVibrationAnalysisFavorite favorite);
}