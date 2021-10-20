package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.AnalysisFavoriteRemarkDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.AnalysisFavoriteRemarkMapper;
import com.aimsphm.nuclear.common.service.AnalysisFavoriteRemarkService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 功能描述:振动分析用户备注服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-01-14 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class AnalysisFavoriteRemarkServiceImpl extends ServiceImpl<AnalysisFavoriteRemarkMapper, AnalysisFavoriteRemarkDO> implements AnalysisFavoriteRemarkService {

    @Override
    public Page<AnalysisFavoriteRemarkDO> listAnalysisFavoriteRemarkByPageWithParams(QueryBO<AnalysisFavoriteRemarkDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<AnalysisFavoriteRemarkDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getStart()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return this.page(queryBO.getPage(), wrapper);
    }

    @Override
    public Integer countRemarked(Long favoriteId) {
        LambdaQueryWrapper<AnalysisFavoriteRemarkDO> remarkWrapper = Wrappers.lambdaQuery(AnalysisFavoriteRemarkDO.class);
        remarkWrapper.eq(AnalysisFavoriteRemarkDO::getFavoriteId, favoriteId);
        return this.count(remarkWrapper);
    }

    @Override
    public List<AnalysisFavoriteRemarkDO> listRemarkByFavoriteId(Long favoriteId) {
        LambdaQueryWrapper<AnalysisFavoriteRemarkDO> remarkWrapper = Wrappers.lambdaQuery(AnalysisFavoriteRemarkDO.class);
        remarkWrapper.eq(AnalysisFavoriteRemarkDO::getFavoriteId, favoriteId);
        return this.list(remarkWrapper);
    }
}
