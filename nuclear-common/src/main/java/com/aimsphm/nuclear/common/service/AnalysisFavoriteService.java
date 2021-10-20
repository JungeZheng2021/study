package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AnalysisFavoriteDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.AnalysisFavoriteVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 功能描述:服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-01-14 14:30
 */
public interface AnalysisFavoriteService extends IService<AnalysisFavoriteDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<AnalysisFavoriteDO> listAnalysisFavoriteByPageWithParams(QueryBO<AnalysisFavoriteDO> queryBO);

    /**
     * 保存收藏和备注
     *
     * @param dto 实体
     * @return 布尔
     */
    boolean saveFavoriteAndRemark(AnalysisFavoriteVO dto);

    /**
     * 根据参数获取某个收藏及备注信息
     *
     * @param entity 条件
     * @return 对象
     */
    AnalysisFavoriteVO getAnalysisFavoriteWithParams(AnalysisFavoriteDO entity);
}
