package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.algorithm.entity.dto.FaultReportResponseDTO;
import com.aimsphm.nuclear.common.entity.AlgorithmRulesConclusionDO;
import com.aimsphm.nuclear.common.entity.BizDiagnosisResultDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 功能描述:服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-02-01 14:30
 */
public interface BizDiagnosisResultService extends IService<BizDiagnosisResultDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<BizDiagnosisResultDO> listBizDiagnosisResultByPageWithParams(QueryBO<BizDiagnosisResultDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<BizDiagnosisResultDO> listBizDiagnosisResultWithParams(QueryBO<BizDiagnosisResultDO> queryBO);

    /**
     * 根据事件id获取故障推理结果
     *
     * @param eventId 事件id
     * @return 布尔
     */
    boolean getDiagnosisResult(Long eventId);

    /**
     * 获取上一次推理结果
     *
     * @param eventId 事件id
     * @return 集合
     */
    List<AlgorithmRulesConclusionDO> lastRulesConclusionWithEventId(Long eventId);

    /**
     * 生成故障推理记录
     *
     * @param eventId 事件id
     */
    void saveRulesConclusionAsync(Long eventId);

    /**
     * 生成故障推理记录
     *
     * @param eventId      事件id
     * @param isReportType 是否需要返回report字段
     * @return map
     */
    Map<String, List<FaultReportResponseDTO>> saveRulesConclusion(Long eventId, Integer isReportType);

}
