package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.BizReportConfigDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.BizReportConfigMapper;
import com.aimsphm.nuclear.common.service.BizReportConfigService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
 * 功能描述:报告生成测点配置表服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-02-03 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class BizReportConfigServiceImpl extends ServiceImpl<BizReportConfigMapper, BizReportConfigDO> implements BizReportConfigService {

    @Override
    public Page<BizReportConfigDO> listBizReportConfigByPageWithParams(QueryBO<BizReportConfigDO> queryBO) {
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
    private LambdaQueryWrapper<BizReportConfigDO> customerConditions(QueryBO<BizReportConfigDO> queryBO) {
        LambdaQueryWrapper<BizReportConfigDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getStart()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return wrapper;
    }

    @Override
    public List<BizReportConfigDO> listBizReportConfigWithParams(QueryBO<BizReportConfigDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }

    @Override
    public List<BizReportConfigDO> listConfigByDeviceId(Long deviceId) {
        LambdaQueryWrapper<BizReportConfigDO> wrapper = Wrappers.lambdaQuery(BizReportConfigDO.class);
        wrapper.eq(BizReportConfigDO::getDeviceId, deviceId);
        wrapper.orderByAsc(BizReportConfigDO::getSort);
        return this.list(wrapper);
    }
}
