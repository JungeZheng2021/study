package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.SparkDownSampleConfigDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.SparkDownSampleConfigMapper;
import com.aimsphm.nuclear.common.service.SparkDownSampleConfigService;
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
 * 功能描述:降采样配置表服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-11-01
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class SparkDownSampleConfigServiceImpl extends ServiceImpl<SparkDownSampleConfigMapper, SparkDownSampleConfigDO> implements SparkDownSampleConfigService {

    @Override
    public Page<SparkDownSampleConfigDO> listSparkDownSampleConfigByPageWithParams(QueryBO<SparkDownSampleConfigDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    /**
     * 拼装查询条件
     *
     * @param queryBO
     * @return
     */
    private LambdaQueryWrapper<SparkDownSampleConfigDO> customerConditions(QueryBO<SparkDownSampleConfigDO> queryBO) {
        return queryBO.lambdaQuery();
    }

    @Override
    public List<SparkDownSampleConfigDO> listSparkDownSampleConfigWithParams(QueryBO<SparkDownSampleConfigDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }
}
