package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.core.enums.PointVisibleEnum;
import com.aimsphm.nuclear.core.service.MonitoringService;
import com.aimsphm.nuclear.ext.service.CommonDeviceServiceExt;
import com.aimsphm.nuclear.ext.service.CommonMeasurePointServiceExt;
import com.aimsphm.nuclear.ext.service.RedisDataService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
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
    private CommonDeviceServiceExt deviceServiceExt;
    @Autowired
    private RedisDataService redisDataService;

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
