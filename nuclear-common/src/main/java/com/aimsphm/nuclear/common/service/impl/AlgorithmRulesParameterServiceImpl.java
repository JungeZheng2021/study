package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.AlgorithmRulesParameterDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.AlgorithmRulesParameterMapper;
import com.aimsphm.nuclear.common.service.AlgorithmRulesParameterService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 功能描述:服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-01-29 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class AlgorithmRulesParameterServiceImpl extends ServiceImpl<AlgorithmRulesParameterMapper, AlgorithmRulesParameterDO> implements AlgorithmRulesParameterService {

    @Override
    public Page<AlgorithmRulesParameterDO> listAlgorithmRulesParameterByPageWithParams(QueryBO<AlgorithmRulesParameterDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }


    /**
     * 拼装查询条件
     *
     * @param queryBO 条件
     * @return 封装后的条件
     */
    private LambdaQueryWrapper<AlgorithmRulesParameterDO> customerConditions(QueryBO<AlgorithmRulesParameterDO> queryBO) {
        LambdaQueryWrapper<AlgorithmRulesParameterDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getStart()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return wrapper;
    }

    @Override
    public List<AlgorithmRulesParameterDO> listAlgorithmRulesParameterWithParams(QueryBO<AlgorithmRulesParameterDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }

    @Override
    public List<AlgorithmRulesParameterDO> listParamByRuleList(List<Long> ruleIds) {
        LambdaQueryWrapper<AlgorithmRulesParameterDO> query = Wrappers.lambdaQuery(AlgorithmRulesParameterDO.class);
        query.in(AlgorithmRulesParameterDO::getRuleId, ruleIds);
        return this.list(query);
    }
}
