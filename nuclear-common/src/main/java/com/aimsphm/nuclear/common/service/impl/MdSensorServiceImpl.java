package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.aimsphm.nuclear.common.entity.MdDevice;
import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.entity.MdSensorExtrainfo;
import com.aimsphm.nuclear.common.entity.MdVibrationFeature;
import com.aimsphm.nuclear.common.entity.vo.WiFiSensorExtrainfoVO;
import com.aimsphm.nuclear.common.entity.vo.WiredSensorExtrainfoVO;
import com.aimsphm.nuclear.common.mapper.MdSensorMapper;
import com.aimsphm.nuclear.common.service.MdDeviceService;
import com.aimsphm.nuclear.common.service.MdSensorExtrainfoService;
import com.aimsphm.nuclear.common.service.MdSensorService;
import com.aimsphm.nuclear.common.service.MdVibrationFeatureService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author lu.yi
 * @since 2020-03-18
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class MdSensorServiceImpl extends ServiceImpl<MdSensorMapper, MdSensor> implements MdSensorService {

    @Autowired
    private MdSensorMapper MdSensorMapper;

    @Autowired
    private MdDeviceService mdDeviceService;
    @Autowired
    private MdVibrationFeatureService mdVibrationFeatureService;
    @Autowired
    @Qualifier(value = "redisTemplate")
    private RedisTemplate<String, Object> redis;

    @Autowired
    private MdSensorExtrainfoService mdSensorExtrainfoService;

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "PImdSensorServiceSub", key = "#subSystemId")
    public List<MdSensor> getPIMdSensorBySubSystemId(Long subSystemId) {
        QueryWrapper<MdSensor> queryWrapper = new QueryWrapper<>();
        MdSensor mdSensor = new MdSensor();
        mdSensor.setSubSystemId(subSystemId);
        mdSensor.setPiSensor(true);
        queryWrapper.setEntity(mdSensor);
        return this.list(queryWrapper);
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "mdSensorAndFeatureServiceDeviceId", key = "#deviceId")
    public List<MdSensor> getMdSensorsAndFeatureByDeviceId(Long deviceId) {
        MdDevice mdDevice = mdDeviceService.getById(deviceId);
        if (mdDevice == null) {
            return Lists.newArrayList();
        }
        QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
        mdSensorQueryWrapper.lambda().and(Wrapper -> Wrapper.eq(MdSensor::getSubSystemId, mdDevice.getSubSystemId()).eq(MdSensor::getPiSensor, true).eq(MdSensor::getSystemSensor, true)).or().eq(MdSensor::getDeviceId, deviceId).eq(MdSensor::getPiSensor, true);
        List<MdSensor> sensors = this.list(mdSensorQueryWrapper);
        QueryWrapper<MdSensor> mdSensorQueryWrapper2 = new QueryWrapper<>();
        mdSensorQueryWrapper2.lambda().eq(MdSensor::getDeviceId, deviceId).eq(MdSensor::getPiSensor, false);
        List<MdSensor> noPisensors = this.list(mdSensorQueryWrapper2);
        List<MdSensor> newnoPiSensors = Lists.newArrayList();
        for (MdSensor nsensor : noPisensors) {
            for (int i = 0; i < 3; i++) {
                MdSensor mdSensor = new MdSensor();
                BeanUtils.copyProperties(nsensor, mdSensor);
                mdSensor.setTagId(mdSensor.getTagId() + "_" + i);
                newnoPiSensors.add(mdSensor);
            }
        }
        sensors.addAll(newnoPiSensors);
        return sensors;
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "mdSensorServiceSub", key = "#subSystemId")
    public List<MdSensor> getMdSensorBySubSystemId(Long subSystemId) {
        QueryWrapper<MdSensor> queryWrapper = new QueryWrapper<>();
        MdSensor mdSensor = new MdSensor();
        mdSensor.setSubSystemId(subSystemId);
        queryWrapper.setEntity(mdSensor);
        return this.list(queryWrapper);
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "nonePiMdSensorServiceSub", key = "#subSystemId")
    public List<MdSensor> getNonePiMdSensorBySubSystemId(Long subSystemId) {
        QueryWrapper<MdSensor> queryWrapper = new QueryWrapper<>();
        MdSensor mdSensor = new MdSensor();
        mdSensor.setPiSensor(false);
        mdSensor.setSubSystemId(subSystemId);
        queryWrapper.setEntity(mdSensor);
        return this.list(queryWrapper);
    }

    @Override
    public List<Object> getAbNormalMdSensorExtraInfo(Long deviceId, Integer type) {
        List<MdSensorExtrainfo> sensors = mdSensorExtrainfoService.selectFailedSensor(deviceId, type);
        List<Object> newsensors = Lists.newArrayList();
        sensors.stream().forEach(sensor -> {
            adjustSensorStatus(sensor);
            if (sensor.getIswifi().intValue() == 1) {
                WiFiSensorExtrainfoVO wv = new WiFiSensorExtrainfoVO();
                BeanUtils.copyProperties(sensor, wv);
                assignRemainPower(wv);
                newsensors.add(wv);
            } else {
                WiredSensorExtrainfoVO wv = new WiredSensorExtrainfoVO();
                BeanUtils.copyProperties(sensor, wv);
                newsensors.add(wv);
            }
        });
        return newsensors;
    }

    private void adjustSensorStatus(MdSensorExtrainfo sensor) {
        if (ObjectUtils.isEmpty(sensor.getConnectStatus())) {
            sensor.setConnectStatus(0);
        }
        if (ObjectUtils.isEmpty(sensor.getStatus())) {
            sensor.setStatus(1);
        }
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "viFeatureSensorExtraInfo", key = "#tagId")
    public MdSensorExtrainfo getSensorExtraInfo(String tagId) {
        QueryWrapper<MdSensorExtrainfo> queryWrapper = new QueryWrapper<>();
        MdSensorExtrainfo mdSensor = new MdSensorExtrainfo();
        mdSensor.setTagId(tagId);
        queryWrapper.setEntity(mdSensor);
        MdSensorExtrainfo se = mdSensorExtrainfoService.list(queryWrapper).stream().findAny().orElse(null);
        return se;
    }
    @Override
    public Object getMdSensorExtraInfo(String tagId) {
        MdSensor mdSensoro = this.getMdSensorsByTagIds(tagId);
        if (mdSensoro == null) {
            return new WiFiSensorExtrainfoVO();
        }
        QueryWrapper<MdSensorExtrainfo> queryWrapper = new QueryWrapper<>();
        MdSensorExtrainfo mdSensor = new MdSensorExtrainfo();
        mdSensor.setTagId(tagId);
        queryWrapper.setEntity(mdSensor);
        MdSensorExtrainfo se = mdSensorExtrainfoService.list(queryWrapper).stream().findAny().orElse(null);
        adjustSensorStatus(se);
        if (mdSensoro.getIswifi()) {
            WiFiSensorExtrainfoVO wv = new WiFiSensorExtrainfoVO();
            BeanUtils.copyProperties(se, wv);
            assignRemainPower(wv);
            return wv;
        } else {
            WiredSensorExtrainfoVO wv = new WiredSensorExtrainfoVO();
            BeanUtils.copyProperties(se, wv);
            return wv;
        }

    }

    private void assignRemainPower(WiFiSensorExtrainfoVO wv) {
        Double voltage =  wv.getVoltage();
        if(voltage >=3.59d)
        {
            wv.setRemainingPower(1d);
        }else if(voltage >=3.55d &&voltage<3.59d)
        {
            wv.setRemainingPower(0.75d);
        }else if(voltage >=3.50d && voltage<3.55d)
        {
            wv.setRemainingPower(0.5d);
        }else if(voltage >=3.45d && voltage<3.5d)
        {
            wv.setRemainingPower(0.25d);
        }else if(voltage <3.45d)
        {
            wv.setRemainingPower(0.05d);
        }
    }


    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "viFeatureMdSensorMap", key = "#tagId")
    public Map<String, List<MdVibrationFeature>> getViFeatureMdSensorMap(String tagId) {
//        List<MdVibrationFeature> vecFlist = Lists.newArrayList();
//        List<MdVibrationFeature> accFlist = Lists.newArrayList();
//        List<MdVibrationFeature> tempFlist = Lists.newArrayList();
//        List<MdVibrationFeature> mdVibrationFeatures = this.getViFeatureMdSensor(tagId);
//        mdVibrationFeatures.stream().forEach(mf->{
//            if(mf.getVifPostfix().startsWith("acc"))
//            {
//                accFlist.add(mf);
//            }else if(mf.getVifPostfix().startsWith("vec"))
//            {
//                vecFlist.add(mf);
//            }else if(mf.getVifPostfix().startsWith("temp"))
//            {
//                tempFlist.add(mf);
//            }
//        });
//        Map map = Maps.newHashMap();
//        map.put("vec",vecFlist);
//        map.put("acc",accFlist);
//        map.put("temp",tempFlist);
//        return map;
        List<MdVibrationFeature> mdVibrationFeatures =   this.getViFeatureMdSensor(tagId);
        mdVibrationFeatures = mdVibrationFeatures.stream().sorted((u1, u2) ->
        {
            return u1.getOrderString().compareTo(u2.getOrderString());

        }).collect(Collectors.toList());
        return mdVibrationFeatures.stream().collect(Collectors.groupingBy(MdVibrationFeature::getGroupName, (() -> new TreeMap().descendingMap()),Collectors.toList()));
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "isNonePiVi", key = "#tagId")
    public Boolean isNonePiViSensor(String tagId)
    {
        String tag = getBaseTagForViSensor(tagId);
        return !(getViFeatureMdSensor(tag).isEmpty());
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "isWifi", key = "#tagId")
    public Boolean isWifiSensor(String tagId) {
        String tag = getBaseTagForViSensor(tagId);
        QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
        mdSensorQueryWrapper.lambda().eq(MdSensor::getTagId, tag).or().eq(MdSensor::getAlias, tag);
        MdSensor mdSensor = this.list(mdSensorQueryWrapper).stream().findAny().orElse(null);
        return (mdSensor!=null &&mdSensor.getIswifi() != null && mdSensor.getIswifi());
    }

    @Override
    public  String getBaseTagForViSensor(String tagId) {
       String tagSplit[] = getBaseTagAndFeatureForViSensor(tagId);
        return tagSplit[0];
    }

    @Override
    public  String[] getBaseTagAndFeatureForViSensor(String tagId) {
        String[] tag = new String[]{"",""};
        if (org.apache.commons.lang3.StringUtils.contains(tagId, "_")) {
            tag[0] = tagId.substring(0, tagId.indexOf('_'));
            tag[1] = tagId.substring(tagId.indexOf('_')+1);
        }else{
            tag[0] = tagId;
        }

        return tag;
    }
    public static void main(String[] args) {
        String tagId = "20-CCS-MP-01A-M2V_acc_Rms";
//        if (org.apache.commons.lang3.StringUtils.contains(tagId,"_")){
//            tagId = tagId.substring(tagId.indexOf('_'));
//        }
//        System.out.println(getBaseTagForViSensor(tagId));
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "viFeatureMdSensorList", key = "#tagId")
    public List<MdVibrationFeature> getViFeatureMdSensor(String tagId) {
        QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
        mdSensorQueryWrapper.lambda().eq(MdSensor::getTagId, tagId).or().eq(MdSensor::getAlias,tagId);
        MdSensor mdSensor = this.list(mdSensorQueryWrapper).stream().findAny().orElse(null);
        if (ObjectUtils.isEmpty(mdSensor)) {
            return Lists.newArrayList();
        }
        if(ObjectUtils.isEmpty(mdSensor.getFeatureType()))
        {
            return Lists.newArrayList();
        }
        List<MdVibrationFeature> mdVibrationFeatures = mdVibrationFeatureService.getVibrationFeature(mdSensor.getFeatureType(), mdSensor.getSensorDes());
        return mdVibrationFeatures;
    }

    @Override
    public List<String> getSensorDescBySubSystemId(Long subSystemId) {
        List<MdSensor> mdSensors = this.getMdSensorBySubSystemId(subSystemId);
        List<String> sensorDescs = mdSensors.stream().map(s -> s.getSensorDesc()).distinct().collect(Collectors.toList());
        return sensorDescs;
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "mdSensorServiceSubSubDes", key = "#subSystemId + '_' + #desCode")
    public MdSensor getMdSensorBySubSystemIdDesCode(Long subSystemId, String desCode) {
        QueryWrapper<MdSensor> queryWrapper = new QueryWrapper<>();
        MdSensor mdSensor = new MdSensor();
        mdSensor.setSubSystemId(subSystemId);
        mdSensor.setSensorDesCode(desCode);
        queryWrapper.setEntity(mdSensor);
        return this.list(queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "mdSensorServiceSubDevDes", key = "#deviceId + '_' + #desCode")
    public MdSensor getMdSensorByDeviceIdAndDesCode(Long deviceId, String desCode) {
        QueryWrapper<MdSensor> queryWrapper = new QueryWrapper<>();
        MdSensor mdSensor = new MdSensor();
        mdSensor.setDeviceId(deviceId);
        mdSensor.setSensorDesCode(desCode);
        queryWrapper.setEntity(mdSensor);
        return this.list(queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "nonePimdSensorServiceDeviceId", key = "#deviceId")
    public List<MdSensor> getNonePiMdSensorsByDeviceId(Long deviceId) {
        MdDevice mdDevice = mdDeviceService.getById(deviceId);
        if (mdDevice == null) {
            return Lists.newArrayList();
        }
        QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
        mdSensorQueryWrapper.lambda().and(Wrapper -> Wrapper.eq(MdSensor::getSubSystemId, mdDevice.getSubSystemId()).eq(MdSensor::getSystemSensor, true)).or().eq(MdSensor::getDeviceId, deviceId);
        return this.list(mdSensorQueryWrapper).stream().filter(md->!md.getPiSensor()).collect(Collectors.toList());
    }
    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "mdSensorServiceDeviceId", key = "#deviceId")
    public List<MdSensor> getMdSensorsByDeviceId(Long deviceId) {
        MdDevice mdDevice = mdDeviceService.getById(deviceId);
        if (mdDevice == null) {
            return Lists.newArrayList();
        }
        QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
        mdSensorQueryWrapper.lambda().and(Wrapper -> Wrapper.eq(MdSensor::getSubSystemId, mdDevice.getSubSystemId()).eq(MdSensor::getSystemSensor, true)).or().eq(MdSensor::getDeviceId, deviceId);
        return this.list(mdSensorQueryWrapper);
    }

    @Override
    public List<MdSensor> getMdSensorsByTagIds(List<String> tagIds) {
        QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
        mdSensorQueryWrapper.lambda().in(MdSensor::getTagId, tagIds);
        return this.list(mdSensorQueryWrapper);
    }

    @Override
    public List<MdSensor> getMdSensorsByTagIds(List<String> tagIds, Long subSystemId) {
        QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MdSensor> lw = mdSensorQueryWrapper.lambda();
        lw.in(MdSensor::getTagId, tagIds);

        if (!ObjectUtils.isEmpty(subSystemId)) {
            lw.eq(MdSensor::getSubSystemId, subSystemId);
        }
        return this.list(lw);
    }
    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "mdSensorServiceTagIdAndSubSystem", key = "#tagId + '_' + #subSystemId")
    public List<MdSensor> getMdSensorsByTagId(String tagId, Long subSystemId) {
        QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MdSensor> lw = mdSensorQueryWrapper.lambda();
        lw.eq(MdSensor::getTagId, tagId);

        if (!ObjectUtils.isEmpty(subSystemId)) {
            lw.eq(MdSensor::getSubSystemId, subSystemId);
        }
        return this.list(lw);
    }
    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "sensorBySensorDesc", key = "#sensorDesc")
    public MdSensor getMdSensorsBySensorDesc(String sensorDesc) {
        QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
        mdSensorQueryWrapper.lambda().eq(MdSensor::getSensorDesc, sensorDesc);
        return this.list(mdSensorQueryWrapper).stream().findAny().orElse(null);
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "sensorByTag", key = "#tagId")
    public MdSensor getMdSensorsByTagIds(String tagId) {
        QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
        mdSensorQueryWrapper.lambda().eq(MdSensor::getTagId, tagId);
        return this.list(mdSensorQueryWrapper).stream().findAny().orElse(null);
    }

    @Override
    public List<MdSensor> getPublicSendorBySubSystemId(Long subSystemId) {

        QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
        mdSensorQueryWrapper.lambda().and(Wrapper -> Wrapper.eq(MdSensor::getSubSystemId, subSystemId).eq(MdSensor::getSystemSensor, true));
        return this.list(mdSensorQueryWrapper);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = CommonConstant.CACHE_KEY_PREFIX + "mdSensorServiceSub", key = "#sensor.subSystemId"),
            @CacheEvict(value = CommonConstant.CACHE_KEY_PREFIX + "sensorByTag", key = "#sensor.tagId"),
            @CacheEvict(value = CommonConstant.CACHE_KEY_PREFIX + "nonePimdSensorServiceDeviceId", allEntries = true)
    })
    @CacheEvict(value = CommonConstant.CACHE_KEY_PREFIX + "mdSensorServiceDeviceId", allEntries = true)
    public int updateWithOptismicLock(MdSensor sensor) throws Exception {
        int count = MdSensorMapper.updateWarningThreadOptimisticLock(sensor, new Date());
        if (count == 0) {
            throw new Exception("consistence error");
        }
        String deleteKey;
        //是pi测点
        if (sensor.getPiSensor()) {
            deleteKey = CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_POINT_INFO_LIST + CommonConstant.REDIS_KEY_COLON + CommonConstant.REDIS_KEY_COLON + sensor.getTagId();
        } else {
            deleteKey = CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_NONE_PI_POINT_INFO_LIST + CommonConstant.REDIS_KEY_COLON + CommonConstant.REDIS_KEY_COLON + sensor.getTagId();
        }
        //清空缓存中所有的点
        redis.delete(deleteKey);
        return count;
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "vibrate:", key = "#deviceId+'_'+#location")
    public List<MdSensor> getSensorByLocationCodeAndDeviceId(Long deviceId, String location) {
        QueryWrapper<MdSensor> query = new QueryWrapper<>();
        query.lambda().eq(MdSensor::getDeviceId, deviceId).eq(MdSensor::getPiSensor, 0);
        if (StringUtils.hasText(location)) {
            query.lambda().likeRight(MdSensor::getSensorDes, location);
        }
        return this.list(query);
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "vibrate:subSystemId:", key = "#subSystemId")
    public List<MdSensor> getSensorBySubSystemId(Long subSystemId) {
        QueryWrapper<MdSensor> query = new QueryWrapper<>();
        query.lambda().eq(MdSensor::getSubSystemId, subSystemId).eq(MdSensor::getPiSensor, 0);
        return this.list(query);
    }
}