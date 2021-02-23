package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmRulesConclusionDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <服务类>
 * @Author: MILLA
 * @CreateDate: 2021-01-29
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmRulesConclusionService extends IService<AlgorithmRulesConclusionDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<AlgorithmRulesConclusionDO> listAlgorithmRulesConclusionByPageWithParams(QueryBO<AlgorithmRulesConclusionDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<AlgorithmRulesConclusionDO> listAlgorithmRulesConclusionWithParams(QueryBO<AlgorithmRulesConclusionDO> queryBO);

    /**
     * 根据规则id获取具体的推列结果
     *
     * @param ruleIds
     * @return
     */
    List<AlgorithmRulesConclusionDO> listAlgorithmRulesConclusionWithRuleIds(String[] ruleIds);
}
