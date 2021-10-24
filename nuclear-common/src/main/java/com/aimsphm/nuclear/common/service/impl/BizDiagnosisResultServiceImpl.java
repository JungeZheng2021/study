package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.algorithm.entity.dto.FaultReportResponseDTO;
import com.aimsphm.nuclear.algorithm.service.BizDiagnosisService;
import com.aimsphm.nuclear.common.entity.AlgorithmRulesConclusionDO;
import com.aimsphm.nuclear.common.entity.BizDiagnosisResultDO;
import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.enums.DataStatusEnum;
import com.aimsphm.nuclear.common.mapper.BizDiagnosisResultMapper;
import com.aimsphm.nuclear.common.service.AlgorithmRulesConclusionService;
import com.aimsphm.nuclear.common.service.BizDiagnosisResultService;
import com.aimsphm.nuclear.common.service.JobAlarmEventService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.SymbolConstant.COMMA;

/**
 * <p>
 * 功能描述:故障诊断信息服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-02-01 14:30
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class BizDiagnosisResultServiceImpl extends ServiceImpl<BizDiagnosisResultMapper, BizDiagnosisResultDO> implements BizDiagnosisResultService {

    @Resource
    private JobAlarmEventService eventService;

    @Resource
    private AlgorithmRulesConclusionService conclusionService;

    @Autowired(required = false)
    private BizDiagnosisService diagnosisService;

    @Override
    public Page<BizDiagnosisResultDO> listBizDiagnosisResultByPageWithParams(QueryBO<BizDiagnosisResultDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    /**
     * 拼装查询条件
     *
     * @param queryBO 查询条件
     * @return 封装后的条件
     */
    private LambdaQueryWrapper<BizDiagnosisResultDO> customerConditions(QueryBO<BizDiagnosisResultDO> queryBO) {
        return queryBO.lambdaQuery();
    }

    @Override
    public List<BizDiagnosisResultDO> listBizDiagnosisResultWithParams(QueryBO<BizDiagnosisResultDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }

    @Override
    public boolean getDiagnosisResult(Long eventId) {
        LambdaQueryWrapper<BizDiagnosisResultDO> wrapper = Wrappers.lambdaQuery(BizDiagnosisResultDO.class);
        wrapper.eq(BizDiagnosisResultDO::getEventId, eventId)
                .eq(BizDiagnosisResultDO::getStatus, DataStatusEnum.RUNNING.getValue())
                .orderByDesc(BizDiagnosisResultDO::getGmtDiagnosis).last("limit 1");
        return this.count(wrapper) > 0;
    }

    @Async
    @Override
    public void saveRulesConclusionAsync(Long eventId) {
        this.saveRulesConclusion(eventId, 0);
    }

    @Override
    public Map<String, List<FaultReportResponseDTO>> saveRulesConclusion(Long eventId, Integer isReportType) {
        JobAlarmEventDO byId = eventService.getById(eventId);
        if (Objects.isNull(byId) || StringUtils.isEmpty(byId.getPointIds())) {
            return null;
        }
        List<String> collect = Arrays.stream(byId.getPointIds().split(COMMA)).collect(Collectors.toList());
        BizDiagnosisResultDO resultDO = new BizDiagnosisResultDO();
        resultDO.setEventId(eventId);
        resultDO.setDeviceId(byId.getDeviceId());
        resultDO.setSubSystemId(byId.getSubSystemId());
        resultDO.setModelId(byId.getModelId());
        resultDO.setDeviceCode(byId.getDeviceCode());
        resultDO.setDeviceName(byId.getDeviceName());
        resultDO.setStatus(DataStatusEnum.RUNNING.getValue());
        this.save(resultDO);
        //推理
        Map<String, List<FaultReportResponseDTO>> data = diagnosisService.faultDiagnosis(collect, resultDO, Objects.isNull(isReportType) ? 0 : isReportType);
        this.updateById(resultDO);
        return data;
    }

    @Override
    public List<AlgorithmRulesConclusionDO> lastRulesConclusionWithEventId(Long eventId) {
        LambdaQueryWrapper<BizDiagnosisResultDO> wrapper = Wrappers.lambdaQuery(BizDiagnosisResultDO.class);
        wrapper.eq(BizDiagnosisResultDO::getEventId, eventId).eq(BizDiagnosisResultDO::getStatus, DataStatusEnum.SUCCESS.getValue())
                .orderByDesc(BizDiagnosisResultDO::getGmtDiagnosis).last("limit 1");
        BizDiagnosisResultDO one = this.getOne(wrapper);
        if (Objects.isNull(one) || StringUtils.isEmpty(one.getDiagnosisResult())) {
            return new ArrayList<>();
        }
        return conclusionService.listAlgorithmRulesConclusionWithRuleIds(one.getDiagnosisResult().split(COMMA));
    }
}
