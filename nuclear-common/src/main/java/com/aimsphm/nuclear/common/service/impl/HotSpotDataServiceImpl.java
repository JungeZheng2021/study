package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.aimsphm.nuclear.common.entity.TxPumpsnapshot;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.common.mapper.MdDeviceMapper;
import com.aimsphm.nuclear.common.mapper.MdSensorMapper;
import com.aimsphm.nuclear.common.service.HotSpotDataService;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Package: com.aimsphm.nuclear.core.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 14:19
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 14:19
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class HotSpotDataServiceImpl implements HotSpotDataService {

    @Autowired
    @Qualifier(value = "redisTemplate")
    private RedisTemplate<String, Object> redis;

    @Autowired
    private MdDeviceMapper deviceMapper;

    @Autowired
    private MdSensorMapper sensorMapper;

    @Override
    public List<TxPumpsnapshot> listPumpSnapshot(Long subSystemId) {
        List<Long> deviceIds = deviceMapper.selectDeviceIdsBySubSystemId(subSystemId);
        if (CollectionUtils.isEmpty(deviceIds)) {
            return null;
        }
        List<String> keys = deviceIds.stream().map(id -> CommonConstant.REDIS_DEVICE_HEALTH_INFO_PRE + id).collect(Collectors.toList());
        Object objects = redis.opsForValue().multiGet(keys);
        return (List<TxPumpsnapshot>) objects;
    }

    @Override
    public TxPumpsnapshot getPumpSnapshot(Long deviceId) {
        return (TxPumpsnapshot) redis.opsForValue().get(CommonConstant.REDIS_DEVICE_HEALTH_INFO_PRE + deviceId);
    }

    @Override
    public List<MeasurePointVO> getWarmingPumpPointsByDeviceId(Long deviceId) {
        Object members = redis.opsForSet().members(CommonConstant.REDIS_POINT_REAL_TIME_WARNING_LIST_PRE + deviceId);
        Object commons = redis.opsForSet().members(CommonConstant.REDIS_POINT_REAL_TIME_WARNING_LIST_PRE + "common");
        Set<String> tags = Sets.newHashSet();
        tags.addAll((Collection<? extends String>) members);
        tags.addAll((Collection<? extends String>) commons);
        return multiGetByKeyList(tags);
    }

    private List<MeasurePointVO> multiGetByKeyList(Collection<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return null;
        }
        List<String> keys = tags.stream().map(tag -> CommonConstant.REDIS_POINT_REAL_TIME_PRE + tag).collect(Collectors.toList());
        Object objects = redis.opsForValue().multiGet(keys);
        return (List<MeasurePointVO>) objects;
    }

    @Override
    public boolean setPumpSnapshot(TxPumpsnapshot value) {
        try {
            redis.opsForValue().set(CommonConstant.REDIS_DEVICE_HEALTH_INFO_PRE + value.getDeviceId(), value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<MeasurePointVO> getPoints(Long subSystemId, Long deviceId, Boolean locationCodeNotNull) {
        Set<String> tags = sensorMapper.selectPointTagsBySubSystemId(subSystemId, deviceId, locationCodeNotNull);
        return multiGetByKeyList(tags);
    }

    @Override
    public List<MeasurePointVO> getHotPointsByDeviceId(Long deviceId, Boolean locationCodeNotNull) {
        return getPoints(null, deviceId, locationCodeNotNull);
    }

    @Override
    public List<MeasurePointVO> getHotPointsBySubSystemId(Long subSystemId, Boolean locationCodeNotNull) {
        return getPoints(subSystemId, null, locationCodeNotNull);
    }

    @Override
    public List<MeasurePointVO> getPoints(Set<String> tags) {
        return multiGetByKeyList(tags);
    }

    @Override
    public Object getDeviceSnapshot(Long deviceId) {
        return redis.opsForValue().get(CommonConstant.REDIS_DEVICE_HEALTH_INFO_PRE + deviceId);
    }

}
