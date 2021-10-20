package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.BizOriginalDataDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.BizOriginalDataMapper;
import com.aimsphm.nuclear.common.service.BizOriginalDataService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 功能描述:波形数据信息服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-02-03 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class BizOriginalDataServiceImpl extends ServiceImpl<BizOriginalDataMapper, BizOriginalDataDO> implements BizOriginalDataService {

    @Override
    public Page<BizOriginalDataDO> listBizOriginalDataByPageWithParams(QueryBO<BizOriginalDataDO> queryBO) {
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
    private LambdaQueryWrapper<BizOriginalDataDO> customerConditions(QueryBO<BizOriginalDataDO> queryBO) {
        LambdaQueryWrapper<BizOriginalDataDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getStart()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return wrapper;
    }

    @Override
    public List<BizOriginalDataDO> listBizOriginalDataWithParams(QueryBO<BizOriginalDataDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }

    @Override
    public List<Long> listBizOriginalDataByParams(String sensorCode, Long start, Long end) {
        LambdaQueryWrapper<BizOriginalDataDO> query = Wrappers.lambdaQuery(BizOriginalDataDO.class);
        query.eq(BizOriginalDataDO::getSensorCode, sensorCode).ge(BizOriginalDataDO::getTimestamp, start).le(BizOriginalDataDO::getTimestamp, end);
        List<BizOriginalDataDO> list = this.list(query);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().map(x -> x.getTimestamp()).collect(Collectors.toList());
    }
}
