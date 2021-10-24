package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalRuleFeatureDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.AlgorithmNormalRuleFeatureMapper;
import com.aimsphm.nuclear.common.service.AlgorithmNormalRuleFeatureService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
 * @since 2021-06-03 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class AlgorithmNormalRuleFeatureServiceImpl extends ServiceImpl<AlgorithmNormalRuleFeatureMapper, AlgorithmNormalRuleFeatureDO> implements AlgorithmNormalRuleFeatureService {

    @Override
    public Page<AlgorithmNormalRuleFeatureDO> listAlgorithmNormalRuleFeatureByPageWithParams(QueryBO<AlgorithmNormalRuleFeatureDO> queryBO) {
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
    private LambdaQueryWrapper<AlgorithmNormalRuleFeatureDO> customerConditions(QueryBO<AlgorithmNormalRuleFeatureDO> queryBO) {
        return queryBO.lambdaQuery();
    }

    @Override
    public List<AlgorithmNormalRuleFeatureDO> listAlgorithmNormalRuleFeatureWithParams(QueryBO<AlgorithmNormalRuleFeatureDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }
}
