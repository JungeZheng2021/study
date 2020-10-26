package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.aimsphm.nuclear.common.entity.*;
import com.aimsphm.nuclear.common.enums.MethodCnTypeEnum;
import com.aimsphm.nuclear.common.mapper.SysLogMapper;
import com.aimsphm.nuclear.common.entity.bo.SysLogQueryBo;
import com.aimsphm.nuclear.common.service.*;
import com.aimsphm.nuclear.core.entity.bo.MdSensorDaqConfigBO;
import com.aimsphm.nuclear.core.entity.bo.MdSensorExtrainfoBO;
import com.aimsphm.nuclear.core.entity.bo.MdSensorConfigBO;
import com.aimsphm.nuclear.common.mapper.MdSensorExtrainfoMapper ;
import com.aimsphm.nuclear.core.feign.CustomSensorDataServiceFeignClient;
import com.aimsphm.nuclear.core.service.SystemSettingService;
import com.aimsphm.nuclear.core.vo.DeviceAlarmSettingVO;
import com.aimsphm.nuclear.common.entity.bo.CustSensorConfig;
import com.aimsphm.nuclear.common.entity.bo.CustSensorConfigRaw;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SystemSettingServiceImpl implements SystemSettingService {

    @Autowired
    private CustomSensorDataServiceFeignClient customSensorDataServiceFeignClient;

    @Autowired
    MdDeviceService mdDeviceService;
    @Autowired
    TxAlarmEventService txAlarmEventService;
    @Autowired
    MdSensorExtrainfoMapper mdSensorExtrainfoMapper;
    @Autowired
    MdSensorService mdSensorService;
    @Autowired
    MdSensorExtrainfoService mdSensorExtrainfoService;
    @Autowired
    MdSensorSettingService mdSensorSettingService;
    @Autowired
    SysLogMapper sysLogMapper;

    @Autowired
    @Qualifier(value = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<DeviceAlarmSettingVO> getDeviceAlarmSetting(Long subSystemId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        QueryWrapper<MdDevice> qw = new QueryWrapper<>();
        qw.lambda().eq(MdDevice::getSubSystemId, subSystemId);
        List<MdDevice> mdDevices = mdDeviceService.list(qw);
        List<DeviceAlarmSettingVO> deviceAlarmSettingVOs = Lists.newArrayList();
        for (MdDevice device : mdDevices) {
            DeviceAlarmSettingVO deviceAlarmSettingVO = new DeviceAlarmSettingVO();
            BeanUtils.copyProperties(device, deviceAlarmSettingVO);
            QueryWrapper<TxAlarmEvent> qwd = new QueryWrapper<>();
            qwd.lambda().eq(TxAlarmEvent::getDeviceId, device.getId()).orderByDesc(TxAlarmEvent::getLastAlarm);
            Page<TxAlarmEvent> page = new Page<>(1l, 1l);
            TxAlarmEvent te = txAlarmEventService.page(page, qwd).getRecords().stream().findAny().orElse(null);
            if (te != null) {
                deviceAlarmSettingVO.setLastAlarmTime(sdf.format(te.getLastAlarm()));
            }
            deviceAlarmSettingVOs.add(deviceAlarmSettingVO);
        }
        return deviceAlarmSettingVOs;
    }

    @Override
    @Transactional
    public void resetAlarm(Long deviceId) {
        QueryWrapper<TxAlarmEvent> qwd = new QueryWrapper<>();
        qwd.lambda().eq(TxAlarmEvent::getDeviceId, deviceId);
        List<TxAlarmEvent> txs = txAlarmEventService.list(qwd);
        txs.stream().forEach(ae -> {
            ae.setStopFlag(true);

        });
        txAlarmEventService.updateBatchById(txs);
    }

    @Override
    @Transactional
    public void disableAlarm(Long deviceId) {
        MdDevice mdDevice = mdDeviceService.getById(deviceId);
        if (mdDevice != null) {
            mdDevice.setEnableMonitor(false);
        }
        mdDeviceService.updateById(mdDevice);
    }

    @Override
    @Transactional
    public void enableAlarm(Long deviceId) {
        MdDevice mdDevice = mdDeviceService.getById(deviceId);
        if (mdDevice != null) {
            mdDevice.setEnableMonitor(true);
        }
        mdDeviceService.updateById(mdDevice);
    }

    @Override
    public void purgeRedis() {
        Set<String> keys1 = redisTemplate.keys(CommonConstant.CACHE_KEY_PREFIX + "*");
        redisTemplate.delete(keys1);

        Set<String> keys2 = redisTemplate.keys(CommonConstant.REDIS_DEVICE_HEALTH_INFO_PRE + "*");
        Set<String> keys3 = redisTemplate.keys(CommonConstant.REDIS_POINT_REAL_TIME_WARNING_LIST_PRE + "*");
        Set<String> keys4 = redisTemplate.keys(CommonConstant.REDIS_POINT_REAL_TIME_PRE + "*");
        redisTemplate.delete(keys2);
        redisTemplate.delete(keys3);
        redisTemplate.delete(keys4);
    }


    //SensorConfigFunction↓↓↓↓↓

    @Override
    public Integer modifySensorDetail(MdSensorExtrainfoBO bo) {
        MdSensorExtrainfo tmp = new MdSensorExtrainfo();
        BeanUtils.copyProperties(bo, tmp);
        return mdSensorExtrainfoMapper.updateById(tmp);
    }

    @Override
    public Map<String, List<MdSensorConfigBO>> sensorConfigList(Long deviceId) {
        return sensorConfigInfo(deviceId).stream().collect(Collectors.groupingBy(MdSensorConfigBO::getGroupName));
    }

    private List<MdSensorConfigBO> sensorConfigInfo(Long deviceId) {

        QueryWrapper<MdSensor> qw = new QueryWrapper<>();
        LambdaQueryWrapper<MdSensor> lw = qw.lambda().eq(MdSensor::getDeviceId, deviceId).eq(MdSensor::getPiSensor, 0);
        List<MdSensor> mdSensors = mdSensorService.list(lw);
        if (mdSensors.isEmpty()) return null;

        List<Long> sensorIds = mdSensors.stream().sorted(Comparator.comparing(MdSensor::getId)).map(MdSensor::getId).collect(Collectors.toList());
        QueryWrapper<MdSensorExtrainfo> mdSensorExtrainfoQueryWrapper = new QueryWrapper<>();

        LambdaQueryWrapper<MdSensorExtrainfo> mdSensorExtrainfoLambdaQueryWrapper = mdSensorExtrainfoQueryWrapper.lambda().in(MdSensorExtrainfo::getSensorId, sensorIds);
        List<MdSensorExtrainfo> mdSensorExtrainfos = mdSensorExtrainfoService.list(mdSensorExtrainfoLambdaQueryWrapper);

        List<MdSensorConfigBO> mdSensorConfigBOS = new ArrayList<MdSensorConfigBO>();
        mdSensorExtrainfos.stream().forEach(mdSensorExtrainfo -> {
            MdSensorConfigBO mdSensorConfigBo = new MdSensorConfigBO();
            BeanUtils.copyProperties(mdSensorExtrainfo, mdSensorConfigBo);
            mdSensorConfigBOS.add(mdSensorConfigBo);
        });

        mdSensorConfigBOS.stream().forEach(mdSensorConfigBO -> {
            Long sensorId = mdSensorConfigBO.getSensorId();
            String sensorDes = mdSensors.stream().filter(mdSensor -> sensorId.equals(mdSensor.getId())).map(MdSensor::getSensorDes).collect(Collectors.joining(""));
            String alias = mdSensors.stream().filter(mdSensor -> sensorId.equals(mdSensor.getId())).map(MdSensor::getAlias).collect(Collectors.joining(""));
            Optional<Boolean> isWifi = mdSensors.stream().filter(mdSensor -> sensorId.equals(mdSensor.getId())).map(MdSensor::getIswifi).findFirst();
            mdSensorConfigBO.setSensorDes(sensorDes);
            mdSensorConfigBO.setAlias(alias);
            mdSensorConfigBO.setIsWifi(isWifi.get());
        });
        return mdSensorConfigBOS;
    }

    @Override
    public List<MdSensorDaqConfigBO> daqConfigDetail(Long sensorId) {

        QueryWrapper<MdSensorSetting> qw = new QueryWrapper<>();
        LambdaQueryWrapper<MdSensorSetting> lw = qw.lambda().eq(MdSensorSetting::getSensorId, sensorId);
        List<MdSensorSetting> mdSensorSettings = mdSensorSettingService.list(lw);
        List<MdSensorDaqConfigBO> mdSensorDaqConfigBOS = new ArrayList<MdSensorDaqConfigBO>();
        mdSensorSettings.stream().forEach(mdSensorSetting -> {
            MdSensorDaqConfigBO tmp = new MdSensorDaqConfigBO();
            BeanUtils.copyProperties(mdSensorSetting, tmp);
            mdSensorDaqConfigBOS.add(tmp);
        });
        if(mdSensorDaqConfigBOS.isEmpty())
        {
            QueryWrapper<MdSensorExtrainfo> qwe = new QueryWrapper<>();
            LambdaQueryWrapper<MdSensorExtrainfo> lwe = qwe.lambda().eq(MdSensorExtrainfo::getSensorId, sensorId);
            MdSensorExtrainfo mdSensorExtrainfo = mdSensorExtrainfoService.list(lwe).stream().findAny().orElse(null);
            if(!ObjectUtils.isEmpty(mdSensorExtrainfo))
            {
                MdSensorDaqConfigBO tmp = new MdSensorDaqConfigBO();
                tmp.setFmin(mdSensorExtrainfo.getFmin());
                tmp.setFmax(mdSensorExtrainfo.getFmax());
                tmp.setMode(1);
                mdSensorDaqConfigBOS.add(tmp);
            }
        }
        return mdSensorDaqConfigBOS;
    }

    @Override
    public Boolean daqConfig(MdSensorDaqConfigBO bo) {

        if (bo.getMode() == 0) {
            return null; //暂时排除所有采样配置为自动的情况
        }
        QueryWrapper<MdSensorSetting> mdSensorSettingQueryWrapper = new QueryWrapper<>();
        mdSensorSettingQueryWrapper.lambda().in(MdSensorSetting::getSensorId, bo.getSensorIds());
        List<MdSensorSetting> mdSensorSettingList = mdSensorSettingService.list(mdSensorSettingQueryWrapper);
        Boolean result = null;

        //situation1:new daqConfig
        if (mdSensorSettingList.size() == 0) {
            result = createDagConfig(bo);
        }
        //situation2:update daqConfig
        else {
            Optional<Integer> originalMode = mdSensorSettingList.stream().map(MdSensorSetting::getMode).findFirst();
            Integer daqConfigMode = bo.getMode();
            if (originalMode.get() != daqConfigMode) {
                List<Long> ids = mdSensorSettingList.stream().map(MdSensorSetting::getId).collect(Collectors.toList());
                mdSensorSettingService.removeByIds(ids);
                result = createDagConfig(bo);
            } else {
                switch (daqConfigMode) {
                    case 0: //autoConfig update
                        return false;
                    case 1://manualConfig update
                        mdSensorSettingList.stream().forEach(mdSensorSetting -> {
                            BeanUtils.copyProperties(bo, mdSensorSetting);

                            //resetting active
                            mdSensorSetting.setActive(false);

                            //update configstatus
                            QueryWrapper<MdSensorExtrainfo> mdSensorExtrainfoQueryWrapper = new QueryWrapper<>();
                            mdSensorExtrainfoQueryWrapper.lambda().eq(MdSensorExtrainfo::getSensorId,mdSensorSetting.getSensorId());
                            MdSensorExtrainfo tmp = mdSensorExtrainfoService.getOne(mdSensorExtrainfoQueryWrapper);
                            tmp.setConfigStatus(1);
                            tmp.setFmin(bo.getFmin());
                            tmp.setFmax(bo.getFmax());
                            mdSensorExtrainfoService.updateById(tmp);
                        });
                        result = mdSensorSettingService.updateBatchById(mdSensorSettingList);
                        sensorParamSet(bo);
                }
            }
        }
        return result;
    }

    private Boolean createDagConfig(MdSensorDaqConfigBO bo) {
        Boolean result = null;
        QueryWrapper<MdSensorExtrainfo> qw = new QueryWrapper<>();
        qw.lambda().in(MdSensorExtrainfo::getSensorId, bo.getSensorIds());
        List<MdSensorExtrainfo> mdSensorExtrainfos = mdSensorExtrainfoService.list(qw);
        List<MdSensorSetting> mdSensorSettings = new ArrayList<MdSensorSetting>();

        for (MdSensorExtrainfo tmp : mdSensorExtrainfos) {
            MdSensorSetting mdSensorSetting = new MdSensorSetting();

            BeanUtils.copyProperties(tmp, mdSensorSetting);
            BeanUtils.copyProperties(bo, mdSensorSetting);
            mdSensorSetting.setActive(false);
            mdSensorSettings.add(mdSensorSetting);
            tmp.setConfigStatus(1);
            tmp.setMode(bo.getMode());
            tmp.setFmax(bo.getFmax());
            tmp.setFmin(bo.getFmin());
            mdSensorExtrainfoService.updateById(tmp);
        }
        sensorParamSet(bo);

        return result = mdSensorSettingService.saveBatch(mdSensorSettings);
    }

    private void sensorParamSet(MdSensorDaqConfigBO bo) {

        QueryWrapper<MdSensorExtrainfo> qw = new QueryWrapper<>();
        qw.lambda().in(MdSensorExtrainfo::getSensorId, bo.getSensorIds());
        List<MdSensorExtrainfo> mdSensorExtrainfos = mdSensorExtrainfoService.list(qw);

        CustSensorConfig custSensorConfig = new CustSensorConfig();
        CustSensorConfigRaw custSensorConfigRaw = new CustSensorConfigRaw();
        for (MdSensorExtrainfo tmp : mdSensorExtrainfos) {

            //配置下发
            MdSensor mdSensor = mdSensorService.getById(tmp.getSensorId());
            Boolean isWifi = mdSensor.getIswifi();
            if (isWifi == null){
                return;
            }
            BeanUtils.copyProperties(tmp, custSensorConfig);
            BeanUtils.copyProperties(bo, custSensorConfigRaw);
            if (isWifi){
                if(tmp.getUnitId()==null){
                    custSensorConfigRaw.setUnitId(null);
                }
                else{custSensorConfigRaw.setUnitId(tmp.getUnitId());}
                custSensorConfigRaw.setDaqTime(bo.getDaqTime()*1000);
                custSensorConfigRaw.setSleepTime(bo.getSleepTime()*60);
                custSensorConfig.setConfig(custSensorConfigRaw);
                custSensorConfig.setTimestamp(System.currentTimeMillis());
                customSensorDataServiceFeignClient.setWifiParam(custSensorConfig);
            }
            else {
                custSensorConfigRaw.setSleepTime(bo.getSleepTime()*60);
                custSensorConfig.setConfig(custSensorConfigRaw);
                custSensorConfig.setTimestamp(System.currentTimeMillis());
                customSensorDataServiceFeignClient.setFwsParam(custSensorConfig);
            }
        }
    }

    public Page<SysLog> getDailyUsersLog(SysLogQueryBo sysLogQueryBo) {
        if(!ObjectUtils.isEmpty(MethodCnTypeEnum.getByValue(sysLogQueryBo.getMethodCnTypeCode()))) {
            sysLogQueryBo.setMethodCnType(MethodCnTypeEnum.getByValue(sysLogQueryBo.getMethodCnTypeCode()));
        }
        Page<SysLog> ps = new Page<>(sysLogQueryBo.getCurrent(),sysLogQueryBo.getSize());
        ps.setRecords(sysLogMapper.getSysLogListByInfo(sysLogQueryBo,ps));
        return ps;
    }
}
