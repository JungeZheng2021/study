package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonSensorSettingsDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.mapper.CommonSensorSettingsMapper;
import com.aimsphm.nuclear.common.service.CommonSensorSettingsService;
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
 * 功能描述:传感器信息服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-01-21 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonSensorSettingsServiceImpl extends ServiceImpl<CommonSensorSettingsMapper, CommonSensorSettingsDO> implements CommonSensorSettingsService {

    @Override
    public Page<CommonSensorSettingsDO> listCommonSensorSettingsByPageWithParams(QueryBO<CommonSensorSettingsDO> queryBO) {
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
    private LambdaQueryWrapper<CommonSensorSettingsDO> customerConditions(QueryBO<CommonSensorSettingsDO> queryBO) {
        return queryBO.lambdaQuery();
    }

    @Override
    public List<CommonSensorSettingsDO> listCommonSensorSettingsWithParams(QueryBO<CommonSensorSettingsDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }

    @Override
    public CommonSensorSettingsDO getCommonSensorByEdgeId(Integer edgeId, Integer category) {
        LambdaQueryWrapper<CommonSensorSettingsDO> wrapper = Wrappers.lambdaQuery(CommonSensorSettingsDO.class);
        wrapper.eq(CommonSensorSettingsDO::getEdgeId, edgeId).eq(CommonSensorSettingsDO::getCategory, category);
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }
}
