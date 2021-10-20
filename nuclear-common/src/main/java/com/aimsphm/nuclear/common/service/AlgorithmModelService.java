package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmModelDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 功能描述:算法模型信息服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-12-23 14:30
 */
public interface AlgorithmModelService extends IService<AlgorithmModelDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<AlgorithmModelDO> listAlgorithmModelByPageWithParams(QueryBO<AlgorithmModelDO> queryBO);

    /**
     * 根据条件查询数据，不分页
     *
     * @param queryBO 条件
     * @return 集合
     */
    List<AlgorithmModelDO> listAlgorithmModelWithParams(QueryBO<AlgorithmModelDO> queryBO);
}
