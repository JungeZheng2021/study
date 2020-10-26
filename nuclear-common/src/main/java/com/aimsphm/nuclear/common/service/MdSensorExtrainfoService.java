package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.MdSensorExtrainfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-08-11
 */
public interface MdSensorExtrainfoService extends IService<MdSensorExtrainfo> {

    List<MdSensorExtrainfo> selectFailedSensor(Long deviceId,Integer type);
    List<MdSensorExtrainfo> selectByTagId(String tagId);
}