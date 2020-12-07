package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import com.aimsphm.nuclear.ext.service.JobDeviceStatusServiceExt;
import com.aimsphm.nuclear.common.service.impl.JobDeviceStatusServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_DEVICE_RUNNING_STATUS;
import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_POINT_INFO_LIST;

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
    @Cacheable(value = REDIS_DEVICE_RUNNING_STATUS, key = "#deviceId")
    public JobDeviceStatusDO getDeviceRunningStatus(Long deviceId) {
        LambdaQueryWrapper<JobDeviceStatusDO> wrapper = Wrappers.lambdaQuery(JobDeviceStatusDO.class);
        wrapper.orderByDesc(JobDeviceStatusDO::getId).last(" limit 1");
        return this.getOne(wrapper);
    }
}
