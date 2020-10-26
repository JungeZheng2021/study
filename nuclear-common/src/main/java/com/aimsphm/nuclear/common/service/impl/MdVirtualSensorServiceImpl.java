package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.aimsphm.nuclear.common.entity.MdDevice;
import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.entity.MdVirtualSensor;
import com.aimsphm.nuclear.common.mapper.MdVirtualSensorMapper;
import com.aimsphm.nuclear.common.service.MdDeviceService;
import com.aimsphm.nuclear.common.service.MdVirtualSensorService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-06-10
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class MdVirtualSensorServiceImpl extends ServiceImpl<MdVirtualSensorMapper, MdVirtualSensor> implements MdVirtualSensorService {

    @Autowired
    private MdVirtualSensorMapper MdVirtualSensorMapper;
    @Autowired
    private MdDeviceService mdDeviceService;
    @Override
    public List<MdVirtualSensor> getMdSensorBySubSystemId(Long subSystemId) {
        QueryWrapper<MdVirtualSensor> queryWrapper = new QueryWrapper<>();
        MdVirtualSensor mdSensor = new MdVirtualSensor();
        mdSensor.setSubSystemId(subSystemId);
        queryWrapper.setEntity(mdSensor);
        return this.list(queryWrapper);
    }

    @Override
    public List<MdVirtualSensor> getMdSensorByDeviceId(Long deviceId) {
        MdDevice mdDevice = mdDeviceService.getById(deviceId);
        if (mdDevice == null) {
            return Lists.newArrayList();
        }
        QueryWrapper<MdVirtualSensor> mdSensorQueryWrapper = new QueryWrapper<>();
        mdSensorQueryWrapper.lambda().and(Wrapper -> Wrapper.eq(MdVirtualSensor::getSubSystemId, mdDevice.getSubSystemId()).eq(MdVirtualSensor::getSystemSensor, true)).or().eq(MdVirtualSensor::getDeviceId, deviceId);
        return this.list(mdSensorQueryWrapper);
    }
    @Override
    public MdVirtualSensor getMdSensorByTagId(String tagId) {
        QueryWrapper<MdVirtualSensor> queryWrapper = new QueryWrapper<>();
        MdVirtualSensor mdSensor = new MdVirtualSensor();
        mdSensor.setTagId(tagId);
        queryWrapper.setEntity(mdSensor);
        return this.list(queryWrapper).stream().findAny().orElse(null);
    }

}