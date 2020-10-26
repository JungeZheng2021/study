package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.MdVirtualSensor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-06-10
 */
public interface MdVirtualSensorService extends IService<MdVirtualSensor> {

    List<MdVirtualSensor> getMdSensorBySubSystemId(Long subSystemId);
    List<MdVirtualSensor> getMdSensorByDeviceId(Long deviceId);

    MdVirtualSensor getMdSensorByTagId(String tagId);
}