package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavorite;
import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavoriteRemark;
import com.aimsphm.nuclear.common.entity.bo.RemarkBO;
import com.aimsphm.nuclear.common.entity.vo.FavoriteDetailsVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 振动分析收藏夹备注
 *
 * @author lu.yi
 * @since 2020-08-14
 */
public interface MdVibrationAnalysisFavoriteRemarkService extends IService<MdVibrationAnalysisFavoriteRemark> {

    /**
     * 添加收藏夹备注
     *
     * @param remark
     * @return
     */
    Object saveMdVibrationAnalysisFavoriteRemark(RemarkBO remark);

    /**
     * 根据收藏夹获取备注
     *
     * @param favoriteId 收藏夹id
     * @return
     */
    FavoriteDetailsVO listFavoriteRemark(Long favoriteId);

    /**
     * 根据收藏夹信息获取备注信息
     *
     * @param favorite 收藏夹信息
     * @return
     */
    FavoriteDetailsVO listRemarkByFavorite(MdVibrationAnalysisFavorite favorite);
}