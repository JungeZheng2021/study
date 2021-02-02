package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.algorithm.service.BizDiagnosisService;
import com.aimsphm.nuclear.common.entity.AlgorithmRulesConclusionDO;
import com.aimsphm.nuclear.common.entity.BizDiagnosisResultDO;
import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.SymbolConstant.COMMA;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <故障诊断信息服务实现类>
 * @Author: MILLA
 * @CreateDate: 2021-02-01
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-01
 * @UpdateRemark: <>
 * @Version: 1.0
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
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    /**
     * 拼装查询条件
     *
     * @param queryBO
     * @return
     */
    private LambdaQueryWrapper<BizDiagnosisResultDO> customerConditions(QueryBO<BizDiagnosisResultDO> queryBO) {
        LambdaQueryWrapper<BizDiagnosisResultDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return wrapper;
    }

    @Override
    public List<BizDiagnosisResultDO> listBizDiagnosisResultWithParams(QueryBO<BizDiagnosisResultDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }

    @Override
    public List<AlgorithmRulesConclusionDO> listRulesConclusion(Long eventId) {
        JobAlarmEventDO byId = eventService.getById(eventId);
        if (Objects.isNull(byId) || StringUtils.isEmpty(byId.getPointIds())) {
            return null;
        }
        List<String> collect = Arrays.stream(byId.getPointIds().split(COMMA)).collect(Collectors.toList());
        BizDiagnosisResultDO resultDO = new BizDiagnosisResultDO();
        resultDO.setEventId(eventId);
        BeanUtils.copyProperties(byId, resultDO);
        diagnosisService.faultDiagnosis(collect, resultDO, 0);
        String result = resultDO.getDiagnosisResult();
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        return conclusionService.listAlgorithmRulesConclusionWithRuleIds(result.split(COMMA));
    }

    @Override
    public List<AlgorithmRulesConclusionDO> lastRulesConclusionWithEventId(Long eventId) {
        LambdaQueryWrapper<BizDiagnosisResultDO> wrapper = Wrappers.lambdaQuery(BizDiagnosisResultDO.class);
        wrapper.eq(BizDiagnosisResultDO::getEventId, eventId).orderByDesc(BizDiagnosisResultDO::getGmtDiagnosis).last("limit 1");
        BizDiagnosisResultDO one = this.getOne(wrapper);
        if (Objects.isNull(one) || StringUtils.isEmpty(one.getDiagnosisResult())) {
            return null;
        }
        return conclusionService.listAlgorithmRulesConclusionWithRuleIds(one.getDiagnosisResult().split(COMMA));
    }
}
