package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.AlgorithmModelDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.AlgorithmModelMapper;
import com.aimsphm.nuclear.common.service.AlgorithmModelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 功能描述:算法模型信息服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-12-23 14:30
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class AlgorithmModelServiceImpl extends ServiceImpl<AlgorithmModelMapper, AlgorithmModelDO> implements AlgorithmModelService {

    @Override
    public Page<AlgorithmModelDO> listAlgorithmModelByPageWithParams(QueryBO<AlgorithmModelDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    private LambdaQueryWrapper<AlgorithmModelDO> customerConditions(QueryBO<AlgorithmModelDO> queryBO) {
        LambdaQueryWrapper<AlgorithmModelDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
            wrapper.like(AlgorithmModelDO::getModelName, query.getKeyword());
        }
        return wrapper;
    }

    @Override
    public List<AlgorithmModelDO> listAlgorithmModelWithParams(QueryBO<AlgorithmModelDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }
}
