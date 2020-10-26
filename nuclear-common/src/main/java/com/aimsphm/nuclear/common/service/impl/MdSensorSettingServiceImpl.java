package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.MdSensorSetting;
import com.aimsphm.nuclear.common.mapper.MdSensorSettingMapper;
import com.aimsphm.nuclear.common.service.MdSensorSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
/**
 * 
 *
 * @author lu.yi
 * @since 2020-08-13
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class MdSensorSettingServiceImpl extends ServiceImpl<MdSensorSettingMapper, MdSensorSetting> implements MdSensorSettingService {

    @Autowired
    private MdSensorSettingMapper MdSensorSettingMapper;

}