package com.aimsphm.nuclear.core.service.impl;


import com.aimsphm.nuclear.common.entity.MdDevice;
import com.aimsphm.nuclear.common.service.MdDeviceService;
import com.aimsphm.nuclear.core.entity.MdRuntimeBaseCfg;
import com.aimsphm.nuclear.core.mapper.MdRuntimeBaseCfgMapper;
import com.aimsphm.nuclear.core.service.MdRuntimeBaseCfgService;
import com.aimsphm.nuclear.core.service.MdSubSystemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Mao
 * @since 2020-05-11
 */
@Slf4j
@Service
public class MdRuntimeBaseCfgServiceImpl extends ServiceImpl<MdRuntimeBaseCfgMapper, MdRuntimeBaseCfg> implements MdRuntimeBaseCfgService {

    @Autowired
    private MdRuntimeBaseCfgMapper MdRuntimeBaseCfgMapper;
    @Autowired
    MdDeviceService mdDeviceService;
    @Autowired
    MdSubSystemService mdSubSystemService;

    @Override
    @Transactional
    public Boolean updateConfigBySubSystemId(List<MdRuntimeBaseCfg> configList) throws Exception {
        Long subSystemId = -1l;
        if (!ObjectUtils.isEmpty(configList)) {
            subSystemId = configList.stream().findFirst().orElse(null).getSubSystemId();
        }
        List<MdDevice> mdDevices = mdDeviceService.getDevicesBySubSystemId(subSystemId);
        Date currentDate = Calendar.getInstance().getTime();
        for (MdDevice mdDevice : mdDevices) {
            configList.stream().forEach(obj -> obj.setDeviceId(mdDevice.getId()));
            //Boolean isSuccessful=true;
            for (MdRuntimeBaseCfg cfg : configList) {
                int count = MdRuntimeBaseCfgMapper.updateOptimisticLock(cfg, currentDate);
                if (count == 0) {
                    throw new Exception("Consistent Error");
                }
            }
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean updateConfigByDeviceId(List<MdRuntimeBaseCfg> configList) throws Exception {
        Date currentDate = Calendar.getInstance().getTime();
        //Boolean isSuccessful=true;
        for (MdRuntimeBaseCfg cfg : configList) {
            int count = MdRuntimeBaseCfgMapper.updateOptimisticLock(cfg, currentDate);
            if (count == 0) {
                throw new Exception("Consistent Error");
            }
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean updateSingleConfig(MdRuntimeBaseCfg config) throws Exception {
        Date currentDate = Calendar.getInstance().getTime();
        int count = MdRuntimeBaseCfgMapper.updateOptimisticLock(config, currentDate);
        if (count == 0) {
            throw new Exception("Consistent Error");
        }
        return true;
    }

    @Override
    public List<MdRuntimeBaseCfg> getAllConfigByDeviceId(Long deviceId) {
        QueryWrapper<MdRuntimeBaseCfg> queryWrapper = new QueryWrapper<>();
        MdRuntimeBaseCfg cfg = new MdRuntimeBaseCfg();
        cfg.setDeviceId(deviceId);
        queryWrapper.setEntity(cfg);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional
    public List<MdRuntimeBaseCfg> getCreateAllConfigBySubSystemId(Long subSystemId) {
        List<MdDevice> mdDevices = mdDeviceService.getDevicesBySubSystemId(subSystemId);
        for (MdDevice mdDevice : mdDevices) {
            QueryWrapper<MdRuntimeBaseCfg> queryWrapper = new QueryWrapper<>();
            MdRuntimeBaseCfg cfg = new MdRuntimeBaseCfg();
            cfg.setDeviceId(mdDevice.getId());
            queryWrapper.setEntity(cfg);
            List<MdRuntimeBaseCfg> mdRuntimeBaseCfgs = this.list(queryWrapper);
            if (ObjectUtils.isEmpty(mdRuntimeBaseCfgs)) {
                initRuntimeBaseCfg(subSystemId, mdDevice);
            }
        }
        if (!ObjectUtils.isEmpty(mdDevices)) {
            QueryWrapper<MdRuntimeBaseCfg> queryWrapper = new QueryWrapper<>();
            MdRuntimeBaseCfg cfg = new MdRuntimeBaseCfg();
            cfg.setDeviceId(mdDevices.stream().findFirst().orElse(null).getId());
            queryWrapper.setEntity(cfg);
            return this.list(queryWrapper);
        }
        return Lists.newArrayList();
    }

    public void initRuntimeBaseCfg(Long subSystemId, MdDevice mdDevice) {
        MdRuntimeBaseCfg mdRuntimeBaseCfg1 = new MdRuntimeBaseCfg();
        mdRuntimeBaseCfg1.setDeviceId(mdDevice.getId());
        mdRuntimeBaseCfg1.setSubSystemId(subSystemId);
        mdRuntimeBaseCfg1.setInitialParameterName("heath_running_time");
        mdRuntimeBaseCfg1.setParameterDisplayName("持续运行时间 （分钟）");
        mdRuntimeBaseCfg1.setInitialParameterDataType("Double");
        mdRuntimeBaseCfg1.setSetId(mdDevice.getSetId());
        mdRuntimeBaseCfg1.setSiteId(mdDevice.getSiteId());
        mdRuntimeBaseCfg1.setInitialParameterValue("0");
        mdRuntimeBaseCfg1.setUnit(2);
        this.save(mdRuntimeBaseCfg1);
        MdRuntimeBaseCfg mdRuntimeBaseCfg2 = new MdRuntimeBaseCfg();
        mdRuntimeBaseCfg2.setDeviceId(mdDevice.getId());
        mdRuntimeBaseCfg2.setSubSystemId(subSystemId);
        mdRuntimeBaseCfg2.setInitialParameterName("stop_times");
        mdRuntimeBaseCfg2.setParameterDisplayName("启停次数 （次）");
        mdRuntimeBaseCfg2.setInitialParameterDataType("Integer");
        mdRuntimeBaseCfg2.setSetId(mdDevice.getSetId());
        mdRuntimeBaseCfg2.setSiteId(mdDevice.getSiteId());
        mdRuntimeBaseCfg2.setInitialParameterValue("0");
        mdRuntimeBaseCfg2.setUnit(4);
        this.save(mdRuntimeBaseCfg2);
        MdRuntimeBaseCfg mdRuntimeBaseCfg3 = new MdRuntimeBaseCfg();
        mdRuntimeBaseCfg3.setDeviceId(mdDevice.getId());
        mdRuntimeBaseCfg3.setSubSystemId(subSystemId);
        mdRuntimeBaseCfg3.setInitialParameterName("total_running_duration");
        mdRuntimeBaseCfg3.setParameterDisplayName("总运行时间 （小时）");
        mdRuntimeBaseCfg3.setInitialParameterDataType("Double");
        mdRuntimeBaseCfg3.setSetId(mdDevice.getSetId());
        mdRuntimeBaseCfg3.setSiteId(mdDevice.getSiteId());
        mdRuntimeBaseCfg3.setInitialParameterValue("0");
        mdRuntimeBaseCfg3.setUnit(1);
        this.save(mdRuntimeBaseCfg3);
    }
}