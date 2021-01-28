package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonSensorSettingsDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
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
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <传感器信息服务实现类>
 * @Author: MILLA
 * @CreateDate: 2021-01-21
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-21
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonSensorSettingsServiceImpl extends ServiceImpl<CommonSensorSettingsMapper, CommonSensorSettingsDO> implements CommonSensorSettingsService {

    @Override
    public Page<CommonSensorSettingsDO> listCommonSensorSettingsByPageWithParams(QueryBO<CommonSensorSettingsDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    /**
     * 拼装查询条件
     *
     * @param queryBO
     * @return
     */
    private LambdaQueryWrapper<CommonSensorSettingsDO> customerConditions(QueryBO<CommonSensorSettingsDO> queryBO) {
        LambdaQueryWrapper<CommonSensorSettingsDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return wrapper;
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
