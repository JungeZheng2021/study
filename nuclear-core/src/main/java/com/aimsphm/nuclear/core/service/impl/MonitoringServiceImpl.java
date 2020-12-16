package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.enums.AlarmTypeEnum;
import com.aimsphm.nuclear.common.enums.AlgorithmLevelEnum;
import com.aimsphm.nuclear.common.enums.DeviceHealthEnum;
import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.core.entity.vo.DeviceStatusVO;
import com.aimsphm.nuclear.core.enums.PointVisibleEnum;
import com.aimsphm.nuclear.core.service.MonitoringService;
import com.aimsphm.nuclear.ext.mapper.JobAlarmEventMapperExt;
import com.aimsphm.nuclear.ext.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.SymbolConstant.ZERO;
import static com.aimsphm.nuclear.core.constant.CoreConstants.*;

/**
 * @Package: com.aimsphm.nuclear.core.service.impl
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/11/18 16:49
 * @UpdateUser: milla
 * @UpdateDate: 2020/11/18 16:49
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class MonitoringServiceImpl implements MonitoringService {
    @Autowired
    private CommonMeasurePointServiceExt pointServiceExt;
    @Autowired
    private JobAlarmEventServiceExt eventServiceExt;
    @Autowired
    private JobAlarmEventMapperExt eventMapperExt;
    @Autowired
    private CommonDeviceServiceExt deviceServiceExt;
    @Autowired
    private RedisDataService redisDataService;
    @Autowired
    private JobDeviceStatusServiceExt statusServiceExt;

    /**
     * 时间戳长度
     */
    private static final Integer TIMESTAMPS_LENGTH = 13;

    @Autowired
    private CommonDeviceDetailsServiceExt detailsServiceExt;

    @Override
    public Map<String, MeasurePointVO> getMonitorInfo(Long deviceId) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = initWrapper(deviceId);
        wrapper.isNotNull(CommonMeasurePointDO::getPlaceholder);

        List<MeasurePointVO> points = listPointByWrapper(wrapper);
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        return points.stream().filter((item -> StringUtils.hasText(item.getPlaceholder()))).collect(Collectors.toMap(o -> o.getPlaceholder(), point -> point, (one, two) -> one));
    }

    private LambdaQueryWrapper<CommonMeasurePointDO> initWrapper(Long deviceId) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
        CommonDeviceDO device = deviceServiceExt.getById(deviceId);
        if (Objects.isNull(device)) {
            return wrapper;
        }
        wrapper.and(w -> w.eq(CommonMeasurePointDO::getDeviceId, deviceId)
                .or().eq(CommonMeasurePointDO::getSubSystemId, device.getSubSystemId()).isNull(CommonMeasurePointDO::getDeviceId)
                .or().eq(CommonMeasurePointDO::getSystemId, device.getSystemId()).isNull(CommonMeasurePointDO::getDeviceId).isNull(CommonMeasurePointDO::getSubSystemId)
        );
        return wrapper;
    }

    @Override
    public Map<String, List<MeasurePointVO>> getPointMonitorInfo(Long deviceId) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = initWrapper(deviceId);
        wrapper.isNotNull(CommonMeasurePointDO::getRelatedGroup);
        List<MeasurePointVO> points = listPointByWrapper(wrapper);
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        return points.stream().collect(Collectors.groupingBy(o -> o.getRelatedGroup(), LinkedHashMap::new, Collectors.toList()));
    }

    @Override
    public Map<Integer, Long> countTransfinitePiPoint(Long deviceId) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = initWrapper(deviceId);
        wrapper.eq(CommonMeasurePointDO::getPointType, 1).isNotNull(CommonMeasurePointDO::getCategory);
        List<MeasurePointVO> points = listPointByWrapper(wrapper);
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        List<MeasurePointVO> pointVOS = points.stream().filter(item -> Objects.nonNull(item.getStatus()) && item.getStatus() >= 1).collect(Collectors.toList());
        return pointVOS.stream().collect(Collectors.groupingBy(item -> item.getCategory(), Collectors.counting()));
    }

    @Override
    public DeviceStatusVO getRunningStatus(Long deviceId) {
        DeviceStatusVO status = new DeviceStatusVO();
        LambdaQueryWrapper<JobDeviceStatusDO> wrapper = Wrappers.lambdaQuery(JobDeviceStatusDO.class);
        wrapper.eq(JobDeviceStatusDO::getDeviceId, deviceId).orderByDesc(JobDeviceStatusDO::getId).last(" limit 1");
        JobDeviceStatusDO one = statusServiceExt.getOne(wrapper);
        status.setStatus(Objects.isNull(one) ? DeviceHealthEnum.Stop.getValue() : one.getStatus());
        CommonQueryBO bo = new CommonQueryBO();
        bo.setDeviceId(deviceId);
        bo.setVisible(0);
        List<CommonDeviceDetailsDO> list = detailsServiceExt.listDetailByConditions(bo);
        TimeRangeQueryBO rangeQueryBO = new TimeRangeQueryBO();
        if (CollectionUtils.isNotEmpty(list)) {
            CommonDeviceDetailsDO totalTImeStartTime = list.stream().filter(item -> StringUtils.hasText(item.getFieldNameEn()) && "total_running_duration".equals(item.getFieldNameEn())).findFirst().orElse(null);
            rangeQueryBO.setStart(Objects.nonNull(totalTImeStartTime) && StringUtils.hasText(totalTImeStartTime.getFieldValue()) ? Long.valueOf(totalTImeStartTime.getFieldValue()) : 1608024678000L);
            Map<String, String> config = list.stream().filter(item -> StringUtils.hasText(item.getFieldNameEn())).collect(Collectors.toMap(item -> item.getFieldNameEn(), CommonDeviceDetailsDO::getFieldValue, (a, b) -> a));
            String startTime = config.get(START_TIME);
            String totalRunningDuration = config.get(TOTAL_RUNNING_DURATION);
            String stopTimes = config.get(STOP_TIMES);
            status.setTotalRunningTime(StringUtils.hasText(totalRunningDuration) ? Long.valueOf(totalRunningDuration) : 0L);
            status.setStopTimes(StringUtils.hasText(stopTimes) ? Integer.valueOf(stopTimes) : 0);
            status.setContinuousRunningTime(StringUtils.hasText(startTime) ? System.currentTimeMillis() - Long.valueOf(startTime) : 0L);
        }
        LambdaQueryWrapper<JobDeviceStatusDO> stop = Wrappers.lambdaQuery(JobDeviceStatusDO.class);
        stop.eq(JobDeviceStatusDO::getDeviceId, deviceId).eq(JobDeviceStatusDO::getStatus, DeviceHealthEnum.Stop.getValue());
        if (Objects.nonNull(rangeQueryBO.getStart())) {
            stop.ge(JobDeviceStatusDO::getGmtStart, rangeQueryBO.getStart());
        }
        rangeQueryBO.setEnd(System.currentTimeMillis());
        int count = statusServiceExt.count(stop);
        Map<Integer, Long> times = listRunningDuration(deviceId, rangeQueryBO);
        long sum = times.entrySet().stream().filter(item -> item.getKey() < DeviceHealthEnum.Stop.getValue()).collect(Collectors.summarizingLong(item -> item.getValue())).getSum();
        status.setTotalRunningTime(Objects.isNull(status.getContinuousRunningTime()) ? sum : sum + status.getContinuousRunningTime());
        status.setStopTimes(Objects.isNull(status.getStopTimes()) ? count : count + status.getStopTimes());
        return status;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyDeviceStatus(DeviceStatusVO statusVO) {
        if (Objects.isNull(statusVO.getDeviceId())) {
            return false;
        }
        CommonDeviceDO device = deviceServiceExt.getById(statusVO.getDeviceId());
        if (Objects.isNull(device)) {
            throw new CustomMessageException("要修改的设备不存在");
        }
        if (Objects.isNull(statusVO.getStopTimes()) && StringUtils.isEmpty(statusVO.getStartTime()) && Objects.isNull(statusVO.getTotalRunningTime())) {
            return false;
        }
        List<CommonDeviceDetailsDO> list = initDefaultConfig(device);
        updateDetails(list, statusVO);
        return detailsServiceExt.updateBatchById(list);
    }

    private void updateDetails(List<CommonDeviceDetailsDO> list, DeviceStatusVO statusVO) {
        for (CommonDeviceDetailsDO details : list) {
            String fieldName = details.getFieldNameEn();
            String fieldValue = details.getFieldValue();
            switch (fieldName) {
                case START_TIME:
                    details.setFieldValue(Objects.isNull(statusVO) ? fieldValue : String.valueOf(statusVO.getStartTime()));
                    break;
                case TOTAL_RUNNING_DURATION:
                    details.setFieldValue(Objects.isNull(statusVO) ? fieldValue : String.valueOf(statusVO.getTotalRunningTime()));
                    break;
                case STOP_TIMES:
                    details.setFieldValue(Objects.isNull(statusVO) ? fieldValue : String.valueOf(statusVO.getStopTimes()));
                    break;
            }
        }
    }

    private List<CommonDeviceDetailsDO> initDefaultConfig(CommonDeviceDO device) {
        CommonQueryBO bo = new CommonQueryBO();
        bo.setDeviceId(device.getId());
        bo.setVisible(0);
        List<CommonDeviceDetailsDO> list = detailsServiceExt.listDetailByConditions(bo);
        if (CollectionUtils.isNotEmpty(list)) {
            return list;
        }
        CommonDeviceDetailsDO startTime = new CommonDeviceDetailsDO();
        startTime.setDeviceId(device.getId());
        BeanUtils.copyProperties(device, startTime);
        startTime.setFieldNameEn(START_TIME);
        startTime.setFieldNameZh("启动时间");
        startTime.setFieldValue(String.valueOf(System.currentTimeMillis()));
        startTime.setVisible(false);
        CommonDeviceDetailsDO totalTime = new CommonDeviceDetailsDO();
        BeanUtils.copyProperties(startTime, totalTime);
        totalTime.setFieldNameZh("总运行时间");
        totalTime.setFieldNameEn(TOTAL_RUNNING_DURATION);
        totalTime.setFieldValue(ZERO);
        CommonDeviceDetailsDO stopTime = new CommonDeviceDetailsDO();
        BeanUtils.copyProperties(totalTime, stopTime);
        stopTime.setFieldNameZh("启停次数");
        stopTime.setFieldNameEn(STOP_TIMES);
        List<CommonDeviceDetailsDO> details = Lists.newArrayList(startTime, totalTime, stopTime);
        detailsServiceExt.saveBatch(details);
        return details;
    }

    @Override
    public Map<Integer, Long> listRunningDuration(Long deviceId, TimeRangeQueryBO range) {
        checkRangeTime(range);
        Long startTime = range.getStart();
        Long endTime = range.getEnd();
        LambdaQueryWrapper<JobDeviceStatusDO> wrapper = Wrappers.lambdaQuery(JobDeviceStatusDO.class);
        wrapper.eq(JobDeviceStatusDO::getDeviceId, deviceId).ge(JobDeviceStatusDO::getGmtStart, new Date(startTime));
        List<JobDeviceStatusDO> list = statusServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Map<Integer, Long> collect = list.stream().collect(Collectors.groupingBy(item -> item.getStatus(), TreeMap::new, Collectors.summingLong(item -> item.getStatusDuration())));
        JobDeviceStatusDO first = list.get(0);
        if (first.getGmtStart().getTime() < startTime) {
            Integer status = first.getStatus();
            collect.put(status, startTime - first.getGmtStart().getTime() + collect.get(status));
        }
        JobDeviceStatusDO last = list.get(list.size() - 1);
        if (Objects.isNull(last.getGmtEnd())) {
            Integer status = last.getStatus();
            collect.put(status, endTime - last.getGmtStart().getTime() + collect.get(status));
        }
        return collect;
    }

    private void checkRangeTime(TimeRangeQueryBO range) {
        //默认是一周之前的数据
        if (Objects.isNull(range.getStart()) || range.getStart().toString().length() != TIMESTAMPS_LENGTH) {
            range.setStart(DateUtils.addMilliseconds(new Date(), -7 * 24 * 3600000).getTime());
        }
        if (Objects.isNull(range.getEnd()) || range.getEnd().toString().length() != TIMESTAMPS_LENGTH) {
            range.setEnd(System.currentTimeMillis());
        }
    }

    @Override
    public List<List<LabelVO>> listWarningPoint(Long deviceId, TimeRangeQueryBO range) {
        checkRangeTime(range);
        //测点类型占比
        List<LabelVO> pointTypeScale = listWarmingPointByPointType(deviceId, range);
        //报警类型占比  + 报警级别占比
        List<List<LabelVO>> lists = listWarmingPointByAlarmTypeAndLevel(deviceId, range);
//        //报警趋势占比
        List<LabelVO> alarmDistribution = listWarmingPointByDateDistribution(deviceId);
        lists.add(0, pointTypeScale);
        lists.add(alarmDistribution);
        return lists;
    }


    private List<LabelVO> listWarmingPointByDateDistribution(Long deviceId) {
        return eventMapperExt.selectWarmingPointsByDateDistribution(deviceId);
    }

    private List<LabelVO> listWarmingPointByPointType(Long deviceId, TimeRangeQueryBO range) {
        List<LabelVO> pointList = eventMapperExt.selectWarmingPointsByDeviceId(deviceId, range);
        if (CollectionUtils.isEmpty(pointList)) {
            return Lists.newArrayList();
        }
        pointList.stream().forEach(item -> item.setName(PointCategoryEnum.getDesc((Integer) item.getName())));
        return pointList;
    }

    private List<List<LabelVO>> listWarmingPointByAlarmTypeAndLevel(Long deviceId, TimeRangeQueryBO range) {
        LambdaQueryWrapper<JobAlarmEventDO> wrapper = Wrappers.lambdaQuery(JobAlarmEventDO.class);
        wrapper.eq(JobAlarmEventDO::getDeviceId, deviceId).ge(JobAlarmEventDO::getGmtLastAlarm, new Date(range.getStart())).le(JobAlarmEventDO::getGmtLastAlarm, new Date(range.getEnd()));
        List<JobAlarmEventDO> list = eventServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList(Lists.newArrayList(), Lists.newArrayList());
        }
        Map<Integer, Long> type = list.stream().collect(Collectors.groupingBy(item -> item.getAlarmType(), TreeMap::new, Collectors.counting()));
        Map<Integer, Long> level = list.stream().collect(Collectors.groupingBy(item -> item.getAlarmLevel(), TreeMap::new, Collectors.counting()));
        List<LabelVO> typeList = type.entrySet().stream().map(item -> new LabelVO(AlarmTypeEnum.getDesc(item.getKey()), item.getValue())).collect(Collectors.toList());
        List<LabelVO> levelList = level.entrySet().stream().map(item -> new LabelVO(AlgorithmLevelEnum.getDesc(item.getKey()), item.getValue())).collect(Collectors.toList());

        return Lists.newArrayList(typeList, levelList);
    }

    private List<MeasurePointVO> listPointByWrapper(LambdaQueryWrapper<CommonMeasurePointDO> wrapper) {
        List<CommonMeasurePointDO> list = pointServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Set<String> tagList = list.stream().map(item -> pointServiceExt.getStoreKey(item)).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(tagList)) {
            return null;
        }
        List<MeasurePointVO> pointList = redisDataService.listPointByRedisKey(tagList);
        if (CollectionUtils.isEmpty(pointList)) {
            return null;
        }
        return pointList.stream().filter(o -> Objects.nonNull(o)).collect(Collectors.toList());
    }

    /**
     * @param defaultValue
     * @return
     */
    @Override
    public List<MeasurePointVO> updatePointsData(boolean defaultValue) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.last("and visible%" + PointVisibleEnum.DEVICE_MONITOR.getCategory() + "=0");
        List<CommonMeasurePointDO> list = pointServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            pointServiceExt.clearAllPointsData();
            return null;
        }
        list.stream().forEach(item -> {
            String storeKey = pointServiceExt.getStoreKey(item);
            Object obj = redisDataService.getByKey(storeKey);
            MeasurePointVO vo = new MeasurePointVO();
            if (Objects.nonNull(obj)) {
                MeasurePointVO find = (MeasurePointVO) obj;
                BeanUtils.copyProperties(find, vo);
            }
            if (Objects.isNull(vo)) {
                System.out.println(vo);
            }
            if (Objects.isNull(vo.getValue())) {
                System.out.println(vo.getValue());
            }
            BeanUtils.copyProperties(item, vo);
            pointServiceExt.store2Redis(vo, defaultValue ? new Random().nextDouble() : null);
        });
        return redisDataService.listPointByRedisKey(list.stream().map(item -> pointServiceExt.getStoreKey(item)).collect(Collectors.toSet()));
    }

}
