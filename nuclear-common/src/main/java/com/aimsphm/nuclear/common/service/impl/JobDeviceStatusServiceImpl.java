package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.enums.DeviceHealthEnum;
import com.aimsphm.nuclear.common.mapper.JobDeviceStatusMapper;
import com.aimsphm.nuclear.common.service.JobDeviceStatusService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_DEVICE_RUNNING_STATUS;

/**
 * <p>
 * 功能描述:设备状态扩展服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-01-04 14:30
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class JobDeviceStatusServiceImpl extends ServiceImpl<JobDeviceStatusMapper, JobDeviceStatusDO> implements JobDeviceStatusService {

    @Override
    public Page<JobDeviceStatusDO> listJobDeviceStatusByPageWithParams(QueryBO<JobDeviceStatusDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        LambdaQueryWrapper<JobDeviceStatusDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getStart()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return this.page(queryBO.getPage(), wrapper);
    }

    @Override
    public JobDeviceStatusDO getDeviceRunningStatus(Long deviceId) {
        LambdaQueryWrapper<JobDeviceStatusDO> wrapper = Wrappers.lambdaQuery(JobDeviceStatusDO.class);
        wrapper.eq(JobDeviceStatusDO::getDeviceId, deviceId).orderByDesc(JobDeviceStatusDO::getId).last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public void updateDeviceStatusWithCalculate(JobDeviceStatusDO status, CommonDeviceDO device, Integer healthStatus) {
        //上一次设备健康状态
        Integer deviceStatus = status.getStatus();
        //本次计算的健康状态
        //是否启用报警
        healthStatus = getDeviceCurrentStatus(device, healthStatus);
        //两次状态没有变化不做任何操作
        if (healthStatus.equals(deviceStatus)) {
            return;
        }
        saveOrUpdateDeviceStatus(status, healthStatus);
    }

    @Override
    public Integer getDeviceCurrentStatus(CommonDeviceDO device, Integer healthStatus) {
        //如果是停机状态，直接返回停机状态
        if (DeviceHealthEnum.STOP.getValue().equals(healthStatus)) {
            return DeviceHealthEnum.STOP.getValue();
        }
        //启用报警 - 直接使用算法结果
        if (device.getEnableMonitor()) {
            return healthStatus;
        }
        //未启用报警 - 忽略算法结果
        return DeviceHealthEnum.HEALTH.getValue();
    }

    @Override
    public int countStopStatus(Long deviceId, TimeRangeQueryBO rangeQueryBO) {
        LambdaQueryWrapper<JobDeviceStatusDO> stop = Wrappers.lambdaQuery(JobDeviceStatusDO.class);
        stop.eq(JobDeviceStatusDO::getDeviceId, deviceId).eq(JobDeviceStatusDO::getStatus, DeviceHealthEnum.STOP.getValue());
        if (Objects.nonNull(rangeQueryBO.getStart())) {
            stop.ge(JobDeviceStatusDO::getGmtStart, new Date(rangeQueryBO.getStart()));
        }
        return this.count(stop);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateDeviceStatus(JobDeviceStatusDO status, Integer deviceStatus) {
        Date currentDate = new Date();
        status.setGmtEnd(currentDate);
        status.setStatusDuration(currentDate.getTime() - status.getGmtStart().getTime());
        //更新结束时间
        this.updateById(status);
        status.setGmtEnd(null);
        status.setGmtStart(currentDate);
        status.setStatus(deviceStatus);
        status.setStatusDuration(0L);
        this.save(status);
    }

    @Override
    @CacheEvict(value = REDIS_DEVICE_RUNNING_STATUS, key = "#status.deviceId")
    public boolean updateById(JobDeviceStatusDO status) {
        return super.updateById(status);
    }
}
