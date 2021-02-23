package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmRulesParameterDO;
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
public interface AlgorithmRulesParameterService extends IService<AlgorithmRulesParameterDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<AlgorithmRulesParameterDO> listAlgorithmRulesParameterByPageWithParams(QueryBO<AlgorithmRulesParameterDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<AlgorithmRulesParameterDO> listAlgorithmRulesParameterWithParams(QueryBO<AlgorithmRulesParameterDO> queryBO);

    /**
     * 根据规则id获取参数
     *
     * @param ruleIds 规则id列表
     * @return
     */
    List<AlgorithmRulesParameterDO> listParamByRuleList(List<Long> ruleIds);
}
