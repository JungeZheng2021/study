package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.MdSensorExtrainfo;
import com.aimsphm.nuclear.common.mapper.MdSensorExtrainfoMapper;
import com.aimsphm.nuclear.common.service.MdSensorExtrainfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-08-11
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true",matchIfMissing= false)
public class MdSensorExtrainfoServiceImpl extends ServiceImpl<MdSensorExtrainfoMapper, MdSensorExtrainfo> implements MdSensorExtrainfoService {

    @Autowired
    private MdSensorExtrainfoMapper MdSensorExtrainfoMapper;

    @Override
    public List<MdSensorExtrainfo> selectFailedSensor(Long deviceId,Integer type){
        return MdSensorExtrainfoMapper.selectFailedSensor(deviceId,type);
    }

    @Override
    public List<MdSensorExtrainfo> selectByTagId(String tagId) {
        QueryWrapper<MdSensorExtrainfo> qr = new QueryWrapper<>();
        qr.lambda().eq(MdSensorExtrainfo::getTagId, tagId);
        List<MdSensorExtrainfo> lst = this.list(qr);
        return lst;
    }
}