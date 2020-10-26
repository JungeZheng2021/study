package com.aimsphm.nuclear.common.service;

import java.util.List;
import java.util.Map;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.entity.MdSensorExtrainfo;
import com.aimsphm.nuclear.common.entity.MdVibrationFeature;
import com.aimsphm.nuclear.common.entity.vo.WiFiSensorExtrainfoVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author lu.yi
 * @since 2020-03-18
 */
public interface MdSensorService extends IService<MdSensor> {

    List<MdSensor> getPIMdSensorBySubSystemId(Long subSystemId);

    List<MdSensor> getMdSensorBySubSystemId(Long subSystemId);


    List<MdSensor> getNonePiMdSensorBySubSystemId(Long subSystemId);

    List<Object> getAbNormalMdSensorExtraInfo(Long subSystemId, Integer type);

    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "viFeatureSensorExtraInfo", key = "#tagId")
    MdSensorExtrainfo getSensorExtraInfo(String tagId);

    Object getMdSensorExtraInfo(String tagId);

    Map<String, List<MdVibrationFeature>> getViFeatureMdSensorMap(String tagId);


    Boolean isNonePiViSensor(String tagId);

    Boolean isWifiSensor(String tagId);

    String getBaseTagForViSensor(String tagId);

    String[] getBaseTagAndFeatureForViSensor(String tagId);

    List<MdVibrationFeature> getViFeatureMdSensor(String tagId);


    List<String> getSensorDescBySubSystemId(Long subSystemId);

    MdSensor getMdSensorBySubSystemIdDesCode(Long subSystemId, String desCode);

    MdSensor getMdSensorByDeviceIdAndDesCode(Long deviceId, String desCode);

    
    List<MdSensor> getNonePiMdSensorsByDeviceId(Long deviceId);

    List<MdSensor> getMdSensorsByDeviceId(Long deviceId);

    List<MdSensor> getMdSensorsAndFeatureByDeviceId(Long deviceId);

    List<MdSensor> getMdSensorsByTagIds(List<String> tagIds);

    List<MdSensor> getMdSensorsByTagIds(List<String> tagIds, Long subSystemId);

    List<MdSensor> getMdSensorsByTagId(String tagId, Long subSystemId);

    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "sensorBySensorDesc", key = "#sensorDesc")
    MdSensor getMdSensorsBySensorDesc(String sensorDesc);

    MdSensor getMdSensorsByTagIds(String tagId);

    //added by Mao
    List<MdSensor> getPublicSendorBySubSystemId(Long subSystemId);

    int updateWithOptismicLock(MdSensor sensor) throws Exception;

    /**
     * 根据设备id和测点位置获取测点集合 - 不包含pi测点
     *
     * @param deviceId
     * @param location
     * @return
     */
    List<MdSensor> getSensorByLocationCodeAndDeviceId(Long deviceId, String location);

    /**
     * 根据子系统编号获取测点位置 -  不包含pi测点
     *
     * @param subSystemId 子系统编号
     * @return
     */
    List<MdSensor> getSensorBySubSystemId(Long subSystemId);
}