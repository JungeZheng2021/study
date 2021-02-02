package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.CommonSensorDO;
import com.aimsphm.nuclear.common.entity.CommonSensorSettingsDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.SensorVO;
import com.aimsphm.nuclear.common.enums.ConfigStatusEnum;
import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import com.aimsphm.nuclear.common.mapper.CommonSensorMapper;
import com.aimsphm.nuclear.common.service.CommonSensorService;
import com.aimsphm.nuclear.common.service.CommonSensorSettingsService;
import com.aimsphm.nuclear.data.feign.entity.dto.ConfigSettingsDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <传感器信息服务实现类>
 * @Author: MILLA
 * @CreateDate: 2021-01-21
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-21
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonSensorServiceImpl extends ServiceImpl<CommonSensorMapper, CommonSensorDO> implements CommonSensorService {

    @Resource
    private CommonSensorSettingsService settingsService;

    @Override
    public Page<CommonSensorDO> listCommonSensorByPageWithParams(QueryBO<CommonSensorDO> queryBO) {
        if (Objects.nonNull(queryBO.getPage().getOrders()) && !queryBO.getPage().getOrders().isEmpty()) {
            queryBO.getPage().getOrders().stream().forEach(item -> item.setColumn(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, item.getColumn())));
        }
        return this.page(queryBO.getPage(), customerConditions(queryBO));
    }

    /**
     * 拼装查询条件
     *
     * @param queryBO
     * @return
     */
    private LambdaQueryWrapper<CommonSensorDO> customerConditions(QueryBO<CommonSensorDO> queryBO) {
        LambdaQueryWrapper<CommonSensorDO> wrapper = queryBO.lambdaQuery();
        ConditionsQueryBO query = queryBO.getQuery();
        if (Objects.nonNull(query.getEnd()) && Objects.nonNull(query.getEnd())) {
        }
        if (StringUtils.hasText(queryBO.getQuery().getKeyword())) {
        }
        return wrapper;
    }

    @Override
    public List<CommonSensorDO> listCommonSensorWithParams(QueryBO<CommonSensorDO> queryBO) {
        return this.list(customerConditions(queryBO));
    }

    @Override
    public List<SensorVO> listCommonSensorSettingsWithParams(CommonSensorDO entity) {
        List<CommonSensorDO> list = this.list(Wrappers.lambdaQuery(entity));
        Map<String, List<CommonSensorDO>> collect = list.stream().filter(x -> Objects.nonNull(x.getEdgeId()) && Objects.nonNull(x.getCategory()))
                .collect(Collectors.groupingBy(CommonSensorDO::getEdgeName));
        return collect.entrySet().stream().map(x -> {
            String key = x.getKey();
            List<CommonSensorDO> value = x.getValue();
            CommonSensorDO sensorDO = value.get(0);
            SensorVO vo = new SensorVO();
            vo.setEdgeName(key);
            vo.setSensorList(value);
            LambdaQueryWrapper<CommonSensorSettingsDO> query = Wrappers.lambdaQuery(CommonSensorSettingsDO.class);
            query.eq(CommonSensorSettingsDO::getEdgeId, sensorDO.getEdgeId()).eq(CommonSensorSettingsDO::getCategory, sensorDO.getCategory()).last("limit 1");
            CommonSensorSettingsDO one = settingsService.getOne(query);
            if (Objects.nonNull(one)) {
                vo.setSettingsStatus(one.getConfigStatus());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConfigStatus(String edgeCode, ConfigSettingsDTO result) {
        updateOilConfigStatus(edgeCode, result);
        updateSoundConfigStatus(edgeCode, result);
        updateVibrationConfigStatus(edgeCode, result);

    }

    @Override
    public List<CommonSensorDO> listCommonSensorBySensorCodeList(ArrayList<String> sensorCodeList) {
        LambdaQueryWrapper<CommonSensorDO> wrapper = Wrappers.lambdaQuery(CommonSensorDO.class);
        wrapper.in(CommonSensorDO::getSensorCode, sensorCodeList);
        return this.list(wrapper);
    }

    private void updateSoundConfigStatus(String edgeCode, ConfigSettingsDTO result) {
        Long soundValueSleepTime = result.getSoundValueSleepTime();
        Long soundWaveSleepTime = result.getSoundWaveSleepTime();
        if (Objects.isNull(soundValueSleepTime) || Objects.isNull(soundWaveSleepTime)) {
            return;
        }
        Boolean isSuccess = soundValueSleepTime == 1 && soundWaveSleepTime == 1;
        LambdaUpdateWrapper<CommonSensorSettingsDO> update = Wrappers.lambdaUpdate(CommonSensorSettingsDO.class);
        update.set(CommonSensorSettingsDO::getConfigStatus, isSuccess ? ConfigStatusEnum.CONFIG_SUCCESS.getValue() : ConfigStatusEnum.CONFIG_FAILED.getValue())
                .eq(CommonSensorSettingsDO::getEdgeCode, edgeCode).eq(CommonSensorSettingsDO::getCategory, PointCategoryEnum.ACOUSTICS.getValue());
        settingsService.update(update);
    }

    private void updateOilConfigStatus(String edgeCode, ConfigSettingsDTO result) {
        //油质状态更新
        Long oilSleepTime = result.getOilSleepTime();
        Integer method = result.getOilViscosityCalculMethod();
        Integer resetAbrasion = result.getOilResetAbrasion();
        //数据清零
        if (Objects.nonNull(resetAbrasion)) {
            LambdaUpdateWrapper<CommonSensorSettingsDO> update = Wrappers.lambdaUpdate(CommonSensorSettingsDO.class);
            update.set(CommonSensorSettingsDO::getConfigStatus, resetAbrasion == 1 ? ConfigStatusEnum.CONFIG_SUCCESS.getValue() : ConfigStatusEnum.CONFIG_FAILED.getValue())
                    .eq(CommonSensorSettingsDO::getEdgeCode, edgeCode).eq(CommonSensorSettingsDO::getCategory, PointCategoryEnum.OIL_QUALITY.getValue());
            settingsService.update(update);
            return;
        }
        if (Objects.isNull(oilSleepTime) || Objects.isNull(method)) {
            return;
        }
        //采样方式和周期
        Boolean isSuccess = oilSleepTime == 1 && method == 1;
        LambdaUpdateWrapper<CommonSensorDO> update = Wrappers.lambdaUpdate(CommonSensorDO.class);
        update.set(CommonSensorDO::getConfigStatus, isSuccess ? ConfigStatusEnum.CONFIG_SUCCESS.getValue() : ConfigStatusEnum.CONFIG_FAILED.getValue())
                .eq(CommonSensorDO::getEdgeCode, edgeCode).eq(CommonSensorDO::getCategory, PointCategoryEnum.OIL_QUALITY.getValue());
        this.update(update);

    }

    private void updateVibrationConfigStatus(String edgeCode, ConfigSettingsDTO result) {
        //灵敏度
        Map<String, Double> sensitivity = result.getVibrationSensitivity();
        if (!MapUtils.isEmpty(sensitivity)) {
            sensitivity.forEach((sensorCode, status) -> {
                LambdaUpdateWrapper<CommonSensorDO> update = Wrappers.lambdaUpdate(CommonSensorDO.class);
                update.set(CommonSensorDO::getConfigStatus, status == 1 ? ConfigStatusEnum.CONFIG_SUCCESS.getValue() : ConfigStatusEnum.CONFIG_FAILED.getValue())
                        .eq(CommonSensorDO::getEdgeCode, edgeCode).eq(CommonSensorDO::getCategory, PointCategoryEnum.VIBRATION.getValue()).eq(CommonSensorDO::getSensorCode, sensorCode);
                this.update(update);
            });
            return;
        }
        //采集配置
        Long vibrationValueSleepTime = result.getVibrationValueSleepTime();
        Long vibrationWaveAcquisitionTime = result.getVibrationWaveAcquisitionTime();
        Long vibrationWaveSleepTime = result.getVibrationWaveSleepTime();
        if (Objects.isNull(vibrationValueSleepTime) || Objects.isNull(vibrationWaveAcquisitionTime) || Objects.isNull(vibrationWaveSleepTime)) {
            return;
        }
        LambdaUpdateWrapper<CommonSensorSettingsDO> update = Wrappers.lambdaUpdate(CommonSensorSettingsDO.class);
        Boolean isSuccess = vibrationValueSleepTime == 1 && vibrationWaveAcquisitionTime == 1 && vibrationWaveSleepTime == 1;
        update.set(CommonSensorSettingsDO::getConfigStatus, isSuccess ? ConfigStatusEnum.CONFIG_SUCCESS.getValue() : ConfigStatusEnum.CONFIG_FAILED.getValue())
                .eq(CommonSensorSettingsDO::getEdgeCode, edgeCode).eq(CommonSensorSettingsDO::getCategory, PointCategoryEnum.VIBRATION.getValue());
        settingsService.update(update);
    }
}
