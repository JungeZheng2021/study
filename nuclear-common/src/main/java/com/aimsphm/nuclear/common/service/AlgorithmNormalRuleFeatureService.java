package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalRuleFeatureDO;
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
 * @since 2021-06-04 14:30
 */
public interface AlgorithmNormalRuleFeatureService extends IService<AlgorithmNormalRuleFeatureDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<AlgorithmNormalRuleFeatureDO> listAlgorithmNormalRuleFeatureByPageWithParams(QueryBO<AlgorithmNormalRuleFeatureDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<AlgorithmNormalRuleFeatureDO> listAlgorithmNormalRuleFeatureWithParams(QueryBO<AlgorithmNormalRuleFeatureDO> queryBO);
}
