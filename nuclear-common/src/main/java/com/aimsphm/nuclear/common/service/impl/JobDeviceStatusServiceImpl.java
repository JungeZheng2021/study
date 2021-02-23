package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.entity.JobAlarmThresholdDO;
import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.enums.DeviceHealthEnum;
import com.aimsphm.nuclear.common.enums.EventStatusEnum;
import com.aimsphm.nuclear.common.enums.ThresholdAlarmStatusEnum;
import com.aimsphm.nuclear.common.mapper.JobDeviceStatusMapper;
import com.aimsphm.nuclear.common.service.JobAlarmEventService;
import com.aimsphm.nuclear.common.service.JobAlarmThresholdService;
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

import javax.annotation.Resource;
import java.util.Date;
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
public class JobDeviceStatusServiceImpl extends ServiceImpl<JobDeviceStatusMapper, JobDeviceStatusDO> implements JobDeviceStatusService {

    @Resource
    private JobAlarmThresholdService thresholdService;

    @Resource
    private JobAlarmEventService eventService;

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
//    @Cacheable(value = REDIS_DEVICE_RUNNING_STATUS, key = "#deviceId")
    public JobDeviceStatusDO getDeviceRunningStatus(Long deviceId) {
        LambdaQueryWrapper<JobDeviceStatusDO> wrapper = Wrappers.lambdaQuery(JobDeviceStatusDO.class);
        wrapper.eq(JobDeviceStatusDO::getDeviceId, deviceId).orderByDesc(JobDeviceStatusDO::getId).last(" limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public void updateDeviceStatusWithCalculate(JobDeviceStatusDO status, Boolean enableMonitor) {
        //上一次设备健康状态
        Integer deviceStatus = status.getStatus();
        //本次计算的健康状态
        Integer currentStatus = DeviceHealthEnum.HEALTH.getValue();
        //是否启用报警
        if (enableMonitor) {
            currentStatus = getDeviceCurrentStatus(status.getDeviceId(), false);
        }
        //两次状态没有变化不做任何操作
        if (currentStatus.equals(deviceStatus)) {
            return;
        }
        saveOrUpdateDeviceStatus(status, currentStatus);
    }

    @Override
    public Integer getDeviceCurrentStatus(Long deviceId, boolean isStop) {
        //如果是停机状态，直接返回停机状态
        if (isStop) {
            return DeviceHealthEnum.STOP.getValue();
        }
        //以下判断为非停机状态
        //阈值报警
        LambdaQueryWrapper<JobAlarmThresholdDO> wrapper = Wrappers.lambdaQuery(JobAlarmThresholdDO.class);
        wrapper.eq(JobAlarmThresholdDO::getDeviceId, deviceId).eq(JobAlarmThresholdDO::getAlarmStatus, ThresholdAlarmStatusEnum.IN_ACTIVITY.getValue());
        int count = thresholdService.count(wrapper);
        //存在阈值超限 设备状态为：报警
        if (count > 0) {
            return DeviceHealthEnum.ALARM.getValue();
        }
        //报警事件
        LambdaQueryWrapper<JobAlarmEventDO> eventWrapper = Wrappers.lambdaQuery(JobAlarmEventDO.class);
        eventWrapper.eq(JobAlarmEventDO::getDeviceId, deviceId).in(JobAlarmEventDO::getAlarmStatus, EventStatusEnum.IN_ACTIVITY.getValue(), EventStatusEnum.ACKNOWLEDGED.getValue());
        int eventCount = eventService.count(eventWrapper);
        //存在算法异常 设备状态为：预警
        if (eventCount > 0) {
            return DeviceHealthEnum.ALARM.getValue();
        }
        //什么都不存在是健康状态
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
