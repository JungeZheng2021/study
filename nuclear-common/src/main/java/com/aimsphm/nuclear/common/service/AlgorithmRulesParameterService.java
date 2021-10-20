package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmRulesParameterDO;
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
 * @since 2021-01-29 14:30
 */
public interface AlgorithmRulesParameterService extends IService<AlgorithmRulesParameterDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<AlgorithmRulesParameterDO> listAlgorithmRulesParameterByPageWithParams(QueryBO<AlgorithmRulesParameterDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<AlgorithmRulesParameterDO> listAlgorithmRulesParameterWithParams(QueryBO<AlgorithmRulesParameterDO> queryBO);

    /**
     * 根据规则id获取参数
     *
     * @param ruleIds 规则id列表
     * @return 集合
     */
    List<AlgorithmRulesParameterDO> listParamByRuleList(List<Long> ruleIds);
}
