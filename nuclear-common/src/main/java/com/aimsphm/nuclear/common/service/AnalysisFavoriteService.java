package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AnalysisFavoriteDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.AnalysisFavoriteVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <振动分析收藏夹服务类>
 * @Author: MILLA
 * @CreateDate: 2021-01-14
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AnalysisFavoriteService extends IService<AnalysisFavoriteDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<AnalysisFavoriteDO> listAnalysisFavoriteByPageWithParams(QueryBO<AnalysisFavoriteDO> queryBO);

    /**
     * 保存收藏和备注
     *
     * @param dto 实体
     * @return
     */
    boolean saveFavoriteAndRemark(AnalysisFavoriteVO dto);

    /**
     * 根据参数获取某个收藏及备注信息
     *
     * @param entity
     * @return
     */
    AnalysisFavoriteVO getAnalysisFavoriteWithParams(AnalysisFavoriteDO entity);
}
