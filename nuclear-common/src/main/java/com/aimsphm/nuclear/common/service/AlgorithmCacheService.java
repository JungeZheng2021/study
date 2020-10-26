package com.aimsphm.nuclear.common.service;

import java.util.List;

import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.entity.dto.BaseSnapshotInfo;

public interface AlgorithmCacheService {

	<T> T getSnapshot(Integer deviceType, Long deviceId);

	<T> void putSnapshot(Integer deviceType, Long deviceId, T object) throws Exception;

    Boolean putRotatingsnapshot(Long deviceId, TxRotatingsnapshot txRotatingsnapshot);

	Boolean putDeviceMidStatus(Long deviceId, Integer middleStatus);

	Integer getDeviceMidStatus(Long deviceId);

	TxRotatingsnapshot getRotatingsnapshot(Long deviceId);

	TxAlarmsnapshot getAlarmSnapshot(Long deviceId);

	Boolean putTurbinesnapshot(Long deviceId, TxTurbinesnapshot txTurbinesnapshot);

	TxTurbinesnapshot getTurbineSnapshot(Long deviceId);

	TxPumpsnapshot getPumpSnapshot(Long deviceId);

	Boolean putTrendRecognition(String tagId, TxSensorTrend tsensor) ;

	Boolean putBaseSnapShotList(Long deviceId, BaseSnapshotInfo baseSnapshotInfo);

	List<BaseSnapshotInfo> getBaseSnapshotList(Long deviceId);

    Boolean putAlarmSnapshot(Long deviceId, TxAlarmsnapshot txAlarmsnapshot);

    Boolean putPumpSnapshot(Long deviceId, TxPumpsnapshot txPumpsnapshot);

	<T> T getSnapshot(String key);

    Boolean putLastCollectorTime(String tagId, Long lasttime);

	Boolean putLastDeviceVibUnitRetry(String deviceId, String devicePart, Integer retryTime);

	Boolean removeLastDeviceVibUnitAlarmRetry(String deviceId, String devicePart);

	Integer getLastDeviceVibUnitAlarmRetry(String deviceId, String devicePart);

	Boolean putLastDeviceVibUnitAlarmTime(String deviceId, String devicePart, Long lastTime);

	Long getLastDeviceVibUnitAlarmTime(String deviceId, String devicePart);

    Long getLastCollectorTime(String tagId);

	List<TxSensorTrend> getTrendRecognition(List<String> tagIds);

	TxSensorTrend getTrendRecognition(String tagIds);

}
