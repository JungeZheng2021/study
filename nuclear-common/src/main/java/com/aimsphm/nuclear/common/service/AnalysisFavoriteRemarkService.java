package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AnalysisFavoriteRemarkDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <振动分析用户备注服务类>
 * @Author: MILLA
 * @CreateDate: 2021-01-14
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AnalysisFavoriteRemarkService extends IService<AnalysisFavoriteRemarkDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<AnalysisFavoriteRemarkDO> listAnalysisFavoriteRemarkByPageWithParams(QueryBO<AnalysisFavoriteRemarkDO> queryBO);

    /**
     * 根据收藏夹id获取备注个数
     *
     * @param favoriteId
     * @return
     */
    Integer countRemarked(Long favoriteId);

    /**
     * 获取所有的备注信息
     *
     * @param favoriteId 收藏夹id
     * @return
     */
    List<AnalysisFavoriteRemarkDO> listRemarkByFavoriteId(Long favoriteId);
}
