package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.impl.JobDeviceStatusServiceImpl;
import com.aimsphm.nuclear.ext.service.JobDeviceStatusServiceExt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_DEVICE_RUNNING_STATUS;

/**
 * @Package: com.aimsphm.nuclear.ext.service.impl
 * @Description: <设备状态扩展服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-12-04
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class JobDeviceStatusServiceExtImpl extends JobDeviceStatusServiceImpl implements JobDeviceStatusServiceExt {

    @Override
    public Page<JobDeviceStatusDO> listJobDeviceStatusByPageWithParams(QueryBO<JobDeviceStatusDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<JobDeviceStatusDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return this.page(queryBO.getPage(), wrapper);
    }

    @Override
    @Cacheable(value = REDIS_DEVICE_RUNNING_STATUS, key = "#deviceId")
    public JobDeviceStatusDO getDeviceRunningStatus(Long deviceId) {
        LambdaQueryWrapper<JobDeviceStatusDO> wrapper = Wrappers.lambdaQuery(JobDeviceStatusDO.class);
        wrapper.eq(JobDeviceStatusDO::getDeviceId, deviceId).orderByDesc(JobDeviceStatusDO::getId).last(" limit 1");
        return this.getOne(wrapper);
    }
}
