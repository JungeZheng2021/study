package com.aimsphm.nuclear.common.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.dto.BaseSnapshotInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.aimsphm.nuclear.common.redis.RedisClient;
import com.aimsphm.nuclear.common.service.AlgorithmCacheService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlgorithmCacheServiceImpl implements AlgorithmCacheService {
    @Autowired
    RedisClient redisClient;

    @Autowired
    @Qualifier(value = "redisTemplate")
    private RedisTemplate<String, Object> redis;

    @Override
    public <T> T getSnapshot(Integer deviceType, Long deviceId) {
        String key = deviceType + "_" + deviceId;
        return redisClient.getObject(key);
    }

    @Override
    public <T> T getSnapshot(String key) {

        return redisClient.getObject(key);
    }

    @Override
    public <T> void putSnapshot(Integer deviceType, Long deviceId, T object) throws Exception {
        String key = deviceType + "_" + deviceId;
        redisClient.setObject(key, object);
    }

    @Override
    public Boolean putBaseSnapShotList(Long deviceId, BaseSnapshotInfo baseSnapshotInfo)
    {
        try {
           Long size =  redis.opsForList().size(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_BASE_SNAPSHOT_LIST + deviceId);
           while (size >= 150)
           {
               redis.opsForList().rightPop(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_BASE_SNAPSHOT_LIST + deviceId);
               size =  redis.opsForList().size(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_BASE_SNAPSHOT_LIST + deviceId);

           }
            redis.opsForList().leftPush(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_BASE_SNAPSHOT_LIST + deviceId, baseSnapshotInfo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<BaseSnapshotInfo> getBaseSnapshotList(Long deviceId)
    {
       return (List<BaseSnapshotInfo>)(Object) redis.opsForList().range(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_BASE_SNAPSHOT_LIST + deviceId,0,-1);
    }
    @Override
    public Boolean putAlarmSnapshot(Long deviceId, TxAlarmsnapshot txAlarmsnapshot) {
        try {
            redis.opsForValue().set(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_ALARM_PUMP_INFO_PRE + deviceId, txAlarmsnapshot);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean putPumpSnapshot(Long deviceId, TxPumpsnapshot txPumpsnapshot) {
        try {
            redis.opsForValue().set(CommonConstant.REDIS_DEVICE_HEALTH_INFO_PRE + deviceId, txPumpsnapshot);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public Boolean putRotatingsnapshot(Long deviceId, TxRotatingsnapshot txRotatingsnapshot) {
        try {
            redis.opsForValue().set(CommonConstant.REDIS_DEVICE_HEALTH_INFO_PRE + deviceId, txRotatingsnapshot);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public Boolean putDeviceMidStatus(Long deviceId, Integer middleStatus) {
        try {
            redis.opsForValue().set(CommonConstant.REDIS_DEVICE_MIDDLE_STATUS_PRE + deviceId, middleStatus);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Integer getDeviceMidStatus(Long deviceId) {
        try {
            return (Integer) redis.opsForValue().get(CommonConstant.REDIS_DEVICE_MIDDLE_STATUS_PRE + deviceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public TxRotatingsnapshot getRotatingsnapshot(Long deviceId) {
        TxRotatingsnapshot txRotatingsnapshot = (TxRotatingsnapshot) redis.opsForValue().get(CommonConstant.REDIS_DEVICE_HEALTH_INFO_PRE  + deviceId);
        return txRotatingsnapshot;
    }
    @Override
    public TxAlarmsnapshot getAlarmSnapshot(Long deviceId) {
        TxAlarmsnapshot txAlarmsnapshot = (TxAlarmsnapshot) redis.opsForValue().get(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_ALARM_PUMP_INFO_PRE + deviceId);
        return txAlarmsnapshot;
    }
    @Override
    public Boolean putTurbinesnapshot(Long deviceId, TxTurbinesnapshot txTurbinesnapshot) {
        try {
            redis.opsForValue().set(CommonConstant.REDIS_DEVICE_HEALTH_INFO_PRE + deviceId, txTurbinesnapshot);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public TxTurbinesnapshot getTurbineSnapshot(Long deviceId) {
        TxTurbinesnapshot turbineSnapshot = (TxTurbinesnapshot) redis.opsForValue().get(CommonConstant.REDIS_DEVICE_HEALTH_INFO_PRE  + deviceId);
        return turbineSnapshot;
    }

    @Override
    public TxPumpsnapshot getPumpSnapshot(Long deviceId) {
        TxPumpsnapshot txPumpsnapshot = (TxPumpsnapshot) redis.opsForValue().get(CommonConstant.REDIS_DEVICE_HEALTH_INFO_PRE + deviceId);
        return txPumpsnapshot;
    }

    @Override
    public Boolean putTrendRecognition(String tagId, TxSensorTrend tsensor) {
        try {
            redis.opsForValue().set(CommonConstant.REDIS_SENSOR_TREND_RECOGNITION_PRE + tagId, tsensor);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean putLastCollectorTime(String tagId, Long lastTime) {
        try {
            redis.opsForValue().set(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_SENSOR_COLLECTOR_PRE + tagId, lastTime);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public Boolean putLastDeviceVibUnitRetry(String deviceId,String devicePart, Integer retryTime) {
        try {
            redis.opsForValue().set(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_LAST_VIB_UNIT_ALARM_RETRY_PRE + "_"+deviceId+"_"+devicePart, retryTime);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean removeLastDeviceVibUnitAlarmRetry(String deviceId, String devicePart) {
        try {

            return redis.delete(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_LAST_VIB_UNIT_ALARM_PRE + "_" + deviceId + "_" + devicePart);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public Integer getLastDeviceVibUnitAlarmRetry(String deviceId, String devicePart) {
        try {
            Integer retry =0;
            retry   = (Integer)redis.opsForValue().get(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_LAST_VIB_UNIT_ALARM_PRE + "_"+deviceId+"_"+devicePart);
            return retry;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public Boolean putLastDeviceVibUnitAlarmTime(String deviceId, String devicePart, Long lastTime) {
        try {
            redis.opsForValue().set(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_LAST_VIB_UNIT_ALARM_PRE + "_"+deviceId+"_"+devicePart, lastTime);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Long getLastDeviceVibUnitAlarmTime(String deviceId, String devicePart) {
        try {
            Long lastTime =0l;
            lastTime   = (Long)redis.opsForValue().get(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_LAST_VIB_UNIT_ALARM_PRE + "_"+deviceId+"_"+devicePart);
            return lastTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }
    @Override
    public Long getLastCollectorTime(String tagId) {
        try {
            Long lastTime =0l;
            lastTime   = (Long)redis.opsForValue().get(CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_SENSOR_COLLECTOR_PRE + tagId);
            return lastTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    @Override
    public List<TxSensorTrend> getTrendRecognition(List<String> tagIds) {
        List<String> keys = tagIds.stream().map(tag -> CommonConstant.REDIS_SENSOR_TREND_RECOGNITION_PRE + tag)
                .collect(Collectors.toList());
        Object objects = redis.opsForValue().multiGet(keys);
        return (List<TxSensorTrend>) objects;
    }

    @Override
    public TxSensorTrend getTrendRecognition(String tagId) {
        TxSensorTrend sensorTrend = null;
        try {
            sensorTrend = (TxSensorTrend) redis.opsForValue()
                    .get(CommonConstant.REDIS_SENSOR_TREND_RECOGNITION_PRE + tagId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensorTrend;
    }

}
