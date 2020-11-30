package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.core.enums.PointVisibleEnum;
import com.aimsphm.nuclear.core.service.MonitoringService;
import com.aimsphm.nuclear.ext.service.CommonMeasurePointServiceExt;
import com.aimsphm.nuclear.ext.service.RedisDataService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    private RedisDataService redisDataService;

    @Override
    public Map<String, MeasurePointVO> getMonitorInfo(Long deviceId) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonMeasurePointDO::getDeviceId, deviceId).isNotNull(CommonMeasurePointDO::getPlaceholder);
        List<MeasurePointVO> points = listPointByWrapper(wrapper);
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        return points.stream().filter((item -> StringUtils.hasText(item.getPlaceholder()))).collect(Collectors.toMap(o -> o.getPlaceholder(), point -> point, (one, two) -> one));
    }

    @Override
    public Map<String, List<MeasurePointVO>> getPointMonitorInfo(Long deviceId) {
        LambdaQueryWrapper<CommonMeasurePointDO> pintQuery = new LambdaQueryWrapper<>();
        pintQuery.eq(CommonMeasurePointDO::getDeviceId, deviceId).isNotNull(CommonMeasurePointDO::getRelatedGroup);
        List<MeasurePointVO> points = listPointByWrapper(pintQuery);
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        return points.stream().collect(Collectors.groupingBy(o -> o.getRelatedGroup(), LinkedHashMap::new, Collectors.toList()));
    }

    @Override
    public Map<Integer, Long> countTransfinitePiPoint(Long deviceId) {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommonMeasurePointDO::getDeviceId, deviceId).eq(CommonMeasurePointDO::getTagType, 1).isNotNull(CommonMeasurePointDO::getCategory);
        List<MeasurePointVO> points = listPointByWrapper(wrapper);
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        List<MeasurePointVO> pointVOS = points.stream().filter(item -> Objects.nonNull(item.getStatus()) && item.getStatus() >= 1).collect(Collectors.toList());
        return pointVOS.stream().collect(Collectors.groupingBy(item -> item.getCategory(), Collectors.counting()));
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
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     * ---------------------------------------非业务使用接口-------------------------------------
     *
     * @return
     */
    @Override
    public List<CommonMeasurePointDO> updatePointsData() {
        LambdaQueryWrapper<CommonMeasurePointDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.last("visible%" + PointVisibleEnum.DEVICE_MONITOR.getCategory() + "=0");
        List<CommonMeasurePointDO> list = pointServiceExt.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            pointServiceExt.clearAllPointsData();
            return null;
        }
        list.stream().forEach(item -> pointServiceExt.updateMeasurePointsInRedis(item.getTagId(), new Random().nextDouble()));
        return list;
    }

}
