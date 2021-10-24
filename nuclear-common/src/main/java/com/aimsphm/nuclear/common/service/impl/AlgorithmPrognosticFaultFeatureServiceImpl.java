package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.AlgorithmPrognosticFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.AlgorithmPrognosticFaultFeatureMapper;
import com.aimsphm.nuclear.common.service.AlgorithmPrognosticFaultFeatureService;
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
 * 功能描述:用于特征预测的数据	该表中的id和algorithm_normal_fault_feature中的id对应，但是该表中的数据可能比algorithm_normal_fault_feature中的数据少服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-07-15 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class AlgorithmPrognosticFaultFeatureServiceImpl extends ServiceImpl<AlgorithmPrognosticFaultFeatureMapper, AlgorithmPrognosticFaultFeatureDO> implements AlgorithmPrognosticFaultFeatureService {

    @Override
    public Page<AlgorithmPrognosticFaultFeatureDO> listAlgorithmPrognosticFaultFeatureByPageWithParams(QueryBO<AlgorithmPrognosticFaultFeatureDO> queryBO) {
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
    private LambdaQueryWrapper<AlgorithmPrognosticFaultFeatureDO> customerConditions(QueryBO<AlgorithmPrognosticFaultFeatureDO> queryBO) {
        return queryBO.lambdaQuery();
    }

    @Override
    public List<AlgorithmPrognosticFaultFeatureDO> listAlgorithmPrognosticFaultFeatureWithParams(QueryBO<AlgorithmPrognosticFaultFeatureDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }
}
