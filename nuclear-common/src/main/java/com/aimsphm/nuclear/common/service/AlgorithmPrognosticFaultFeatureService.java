package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmPrognosticFaultFeatureDO;
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
 * @since 2021-07-15 14:30
 */
public interface AlgorithmPrognosticFaultFeatureService extends IService<AlgorithmPrognosticFaultFeatureDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<AlgorithmPrognosticFaultFeatureDO> listAlgorithmPrognosticFaultFeatureByPageWithParams(QueryBO<AlgorithmPrognosticFaultFeatureDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<AlgorithmPrognosticFaultFeatureDO> listAlgorithmPrognosticFaultFeatureWithParams(QueryBO<AlgorithmPrognosticFaultFeatureDO> queryBO);
}
