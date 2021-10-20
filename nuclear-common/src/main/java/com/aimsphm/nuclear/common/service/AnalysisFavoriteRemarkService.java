package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AnalysisFavoriteRemarkDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 功能描述:服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-01-14 14:30
 */
public interface AnalysisFavoriteRemarkService extends IService<AnalysisFavoriteRemarkDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<AnalysisFavoriteRemarkDO> listAnalysisFavoriteRemarkByPageWithParams(QueryBO<AnalysisFavoriteRemarkDO> queryBO);

    /**
     * 根据收藏夹id获取备注个数
     *
     * @param favoriteId 收藏id
     * @return 证书
     */
    Integer countRemarked(Long favoriteId);

    /**
     * 获取所有的备注信息
     *
     * @param favoriteId 收藏夹id
     * @return 集合
     */
    List<AnalysisFavoriteRemarkDO> listRemarkByFavoriteId(Long favoriteId);
}
