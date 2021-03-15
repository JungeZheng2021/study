package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.CommonSensorDO;
import com.aimsphm.nuclear.common.entity.CommonSensorSettingsDO;
import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.enums.ConfigStatusEnum;
import com.aimsphm.nuclear.common.enums.EventStatusEnum;
import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.response.ResponseData;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import com.aimsphm.nuclear.common.service.CommonSensorService;
import com.aimsphm.nuclear.common.service.CommonSensorSettingsService;
import com.aimsphm.nuclear.common.service.JobAlarmEventService;
import com.aimsphm.nuclear.core.service.SettingsService;
import com.aimsphm.nuclear.data.feign.DataServiceFeignClient;
import com.aimsphm.nuclear.data.feign.entity.dto.ConfigSettingsDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.core.service.impl
 * @Description: <系统设置>
 * @Author: MILLA
 * @CreateDate: 2021/01/21 11:06
 * @UpdateUser: MILLA
 * @UpdateDate: 2021/01/21 11:06
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service
public class SettingsServiceImpl implements SettingsService {
    @Resource
    CommonDeviceService deviceService;
    @Resource
    JobAlarmEventService eventService;

    @Resource
    CommonSensorService sensorService;

    @Resource
    CommonSensorSettingsService settingsService;

    @Resource
    DataServiceFeignClient dataFeignClient;

    @Override
    public boolean updateDeviceMonitorStatus(Long deviceId, boolean enable) {
        LambdaUpdateWrapper<CommonDeviceDO> update = Wrappers.lambdaUpdate(CommonDeviceDO.class);
        update.eq(CommonDeviceDO::getId, deviceId).set(CommonDeviceDO::getEnableMonitor, enable);
        return deviceService.update(update);
    }

    @Override
    public boolean updateDeviceEventProduce(Long deviceId) {
        LambdaUpdateWrapper<JobAlarmEventDO> update = Wrappers.lambdaUpdate(JobAlarmEventDO.class);
        update.eq(JobAlarmEventDO::getDeviceId, deviceId).set(JobAlarmEventDO::getAlarmStatus, EventStatusEnum.FINISHED.getValue());
        return eventService.update(update);
    }

    @Override
    public boolean modifyCommonSensor(CommonSensorDO dto) {
        CommonSensorDO byId = sensorService.getById(dto.getId());
        if (Objects.isNull(byId)) {
            return false;
        }
        byId.setConfigStatus(null);
        sensorService.updateById(dto);
        //灵敏度变化了需要发送数据
        sendMessage2Edge(dto, byId);
        return true;
    }

    private void sendMessage2Edge(CommonSensorDO dto, CommonSensorDO one) {
        boolean flag = Objects.isNull(dto.getSensitivity()) ||
                (ConfigStatusEnum.CONFIG_SUCCESS.equals(one.getConfigStatus()) && one.getSensitivity().equals(dto.getSensitivity()) ||
                        (ConfigStatusEnum.CONFIG_SUCCESS.equals(one.getConfigStatus()) && one.getViscosityCalculateMethod().equals(dto.getViscosityCalculateMethod())
                                && one.getSamplePeriod().equals(dto.getSamplePeriod())));
        if (flag) {
            return;
        }
        try {
            String edgeCode = one.getEdgeCode();
            ConfigSettingsDTO settings = new ConfigSettingsDTO();
            settings.setVibrationSensitivity(new HashMap<String, Double>(16) {{
                put(one.getSensorCode(), dto.getSensitivity());
            }});
            settings.setOilViscosityCalculMethod(dto.getViscosityCalculateMethod());
            settings.setOilSleepTime(dto.getSamplePeriod());
            ResponseData<Boolean> response = dataFeignClient.invokeService(edgeCode, settings);
            if (!response.getData()) {
                throw new CustomMessageException("push config failed");
            }
            dto.setConfigStatus(ConfigStatusEnum.IN_CONFIG.getValue());
        } catch (Exception e) {
            log.error("配置信息下发失败：{}", e);
            dto.setConfigStatus(ConfigStatusEnum.CONFIG_FAILED.getValue());
        }
        sensorService.updateById(dto);
    }

    @Override
    public boolean saveOrModifyCommonSensorSettings(CommonSensorSettingsDO dto) {
        dto.setConfigStatus(null);
        //新建
        if (dto.getId() == -1) {
            dto.setId(null);
            LambdaQueryWrapper<CommonSensorDO> wrapper = Wrappers.lambdaQuery(CommonSensorDO.class);
            wrapper.eq(CommonSensorDO::getEdgeId, dto.getEdgeId()).last("limit 1");
            CommonSensorDO one = sensorService.getOne(wrapper);
            Assert.notNull(one, "edgeId can not be null");
            dto.setCategory(one.getCategory());
            dto.setEdgeCode(one.getEdgeCode());
            settingsService.save(dto);
            sendMessage2Edge(dto, dto);
            return true;
        }
        //修改
        CommonSensorSettingsDO one = settingsService.getById(dto.getId());
        if (Objects.isNull(one)) {
            return false;
        }
        settingsService.updateById(dto);
        //灵敏度变化了需要发送数据
        sendMessage2Edge(dto, one);
        return true;
    }

    private void sendMessage2Edge(CommonSensorSettingsDO dto, CommonSensorSettingsDO one) {
        //如果两个对象完全一样，说明新增数据(需要下发配置)
        boolean equals = dto.equals(one);
        boolean flag = Objects.nonNull(one.getEigenvalueSamplePeriod()) && one.getEigenvalueSamplePeriod().equals(dto.getEigenvalueSamplePeriod())
                && Objects.nonNull(one.getWaveformSampleDuration()) && one.getWaveformSampleDuration().equals(dto.getWaveformSampleDuration())
                && Objects.nonNull(one.getWaveformSamplePeriod()) && one.getWaveformSamplePeriod().equals(dto.getWaveformSamplePeriod())
                && Objects.nonNull(one.getDataReset()) && one.getDataReset().equals(dto.getDataReset());
        //数据和上次一样且上次是配置成功，不下发数据
        if (!equals && flag && ConfigStatusEnum.CONFIG_SUCCESS.equals(one.getConfigStatus())) {
            return;
        }
        try {
            String edgeCode = one.getEdgeCode();
            ConfigSettingsDTO settings = initSettingsByCategory(dto, one);
            ResponseData<Boolean> response = dataFeignClient.invokeService(edgeCode, settings);
            if (!response.getData()) {
                throw new CustomMessageException("push config failed");
            }
            dto.setConfigStatus(ConfigStatusEnum.IN_CONFIG.getValue());
        } catch (Exception e) {
            log.error("配置信息下发失败：{}", e);
            dto.setConfigStatus(ConfigStatusEnum.CONFIG_FAILED.getValue());
            settingsService.updateById(dto);
        }
    }

    private ConfigSettingsDTO initSettingsByCategory(CommonSensorSettingsDO dto, CommonSensorSettingsDO one) {
        Integer category = one.getCategory();
        ConfigSettingsDTO settings = new ConfigSettingsDTO();
        //振动传感器
        if (PointCategoryEnum.VIBRATION.getValue().equals(category)) {
            //特征值周期
            settings.setVibrationValueSleepTime(dto.getEigenvalueSamplePeriod());
            //波形采样周期
            settings.setVibrationWaveSleepTime(dto.getWaveformSamplePeriod());
//            //波形采样时长 --默认数据库写死的
            settings.setVibrationWaveAcquisitionTime(one.getWaveformSampleDuration());
        }
        //声学传感器
        if (PointCategoryEnum.ACOUSTICS.getValue().equals(category)) {
            //特征值周期
            settings.setSoundValueSleepTime(dto.getEigenvalueSamplePeriod());
            //波形采样周期
            settings.setSoundWaveSleepTime(dto.getWaveformSamplePeriod());
        }
        //油质传感器
        if (PointCategoryEnum.OIL_QUALITY.getValue().equals(category)) {
            //特征值周期
            settings.setOilSleepTime(dto.getEigenvalueSamplePeriod());
            //是否清零
            settings.setOilResetAbrasion(dto.getDataReset());
        }
        return settings;
    }
}
