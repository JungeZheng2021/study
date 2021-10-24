package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.AlgorithmRulesDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.AlgorithmRulesMapper;
import com.aimsphm.nuclear.common.service.AlgorithmRulesService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

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
public class AlgorithmRulesServiceImpl extends ServiceImpl<AlgorithmRulesMapper, AlgorithmRulesDO> implements AlgorithmRulesService {

    @Override
    public Page<AlgorithmRulesDO> listAlgorithmRulesByPageWithParams(QueryBO<AlgorithmRulesDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    /**
     * 拼装查询条件
     *
     * @param queryBO 条件
     * @return 封装的条件
     */
    private LambdaQueryWrapper<AlgorithmRulesDO> customerConditions(QueryBO<AlgorithmRulesDO> queryBO) {
        return queryBO.lambdaQuery();
    }

    @Override
    public List<AlgorithmRulesDO> listAlgorithmRulesWithParams(QueryBO<AlgorithmRulesDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }

    @Override
    public List<AlgorithmRulesDO> listRulesBySensorCodeList(List<String> sensorCodeList) {
        LambdaQueryWrapper<AlgorithmRulesDO> query = Wrappers.lambdaQuery(AlgorithmRulesDO.class);
        query.in(AlgorithmRulesDO::getSensorCode, sensorCodeList);
        return this.list(query);
    }
}
