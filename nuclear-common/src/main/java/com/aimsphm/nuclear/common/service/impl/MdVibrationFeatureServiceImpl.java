package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.entity.MdVibrationFeature;
import com.aimsphm.nuclear.common.entity.TxAlarmEvent;
import com.aimsphm.nuclear.common.enums.VibrationFeatureTypeEnum;
import com.aimsphm.nuclear.common.mapper.MdVibrationFeatureMapper;
import com.aimsphm.nuclear.common.service.MdVibrationFeatureService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
 * @since 2020-08-14
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class MdVibrationFeatureServiceImpl extends ServiceImpl<MdVibrationFeatureMapper, MdVibrationFeature> implements MdVibrationFeatureService {

    @Autowired
    private MdVibrationFeatureMapper MdVibrationFeatureMapper;

    @Override
    public List<MdVibrationFeature> getVibrationFeature(String featureType, String desCode){
        QueryWrapper<MdVibrationFeature> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MdVibrationFeature> lw = queryWrapper.lambda();
        if(VibrationFeatureTypeEnum.CWS_O.getValue().equals(featureType)){
            lw.eq(MdVibrationFeature::getCwsO,true);
        }else if(VibrationFeatureTypeEnum.CWS_W.getValue().equals(featureType)){
            lw.eq(MdVibrationFeature::getCwsW,true);
        }else if(VibrationFeatureTypeEnum.CCS.getValue().equals(featureType)){
            lw.eq(MdVibrationFeature::getCcs,true);
        }else if(VibrationFeatureTypeEnum.SWS.getValue().equals(featureType)){
            lw.eq(MdVibrationFeature::getSws,true);
        }else if(VibrationFeatureTypeEnum.FWS.getValue().equals(featureType)){
            lw.eq(MdVibrationFeature::getFws,true);
        }
        if(desCode == null)
        {
            desCode = "";
        }
        lw.like(MdVibrationFeature::getDesCodes,desCode+",");
        return this.list(lw);
    }
}