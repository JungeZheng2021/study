package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmRulesConclusionDO;
import com.aimsphm.nuclear.common.entity.BizDiagnosisResultDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <故障诊断信息服务类>
 * @Author: MILLA
 * @CreateDate: 2021-02-01
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-01
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface BizDiagnosisResultService extends IService<BizDiagnosisResultDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<BizDiagnosisResultDO> listBizDiagnosisResultByPageWithParams(QueryBO<BizDiagnosisResultDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<BizDiagnosisResultDO> listBizDiagnosisResultWithParams(QueryBO<BizDiagnosisResultDO> queryBO);

    /**
     * 根据事件id获取故障推理结果
     *
     * @param eventId 事件id
     * @return
     */
    List<AlgorithmRulesConclusionDO> listRulesConclusion(Long eventId);

    /**
     * 获取上一次推理结果
     *
     * @param eventId
     * @return
     */
    List<AlgorithmRulesConclusionDO> lastRulesConclusionWithEventId(Long eventId);
}
