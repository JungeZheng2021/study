package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.AlgorithmConfigDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.AlgorithmConfigMapper;
import com.aimsphm.nuclear.common.service.AlgorithmConfigService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * <p>
 * 功能描述:服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-12-22 14:30
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class AlgorithmConfigServiceImpl extends ServiceImpl<AlgorithmConfigMapper, AlgorithmConfigDO> implements AlgorithmConfigService {

    @Override
    public Page<AlgorithmConfigDO> listAlgorithmConfigByPageWithParams(QueryBO<AlgorithmConfigDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<AlgorithmConfigDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getStart()) && Objects.nonNull(query.getEnd())) {
            log.debug("startTime:{}, endTime:{}", query.getStart(), query.getEnd());
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
            log.debug("keyword:{}", queryBO.getQuery().getKeyword());
        }
        return this.page(queryBO.getPage(), wrapper);
    }
}
