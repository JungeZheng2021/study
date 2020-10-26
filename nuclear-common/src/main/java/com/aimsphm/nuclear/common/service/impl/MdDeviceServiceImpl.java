package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.aimsphm.nuclear.common.entity.MdDevice;
import com.aimsphm.nuclear.common.mapper.MdDeviceMapper;
import com.aimsphm.nuclear.common.service.MdDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * @author lu.yi
 * @since 2020-03-18
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class MdDeviceServiceImpl extends ServiceImpl<MdDeviceMapper, MdDevice> implements MdDeviceService {

    @Autowired
    private MdDeviceMapper MdDeviceMapper;

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "mdDeviceName", key = "#id")
    public String getNameById(Serializable id) {
        String name = "";

        MdDevice mdDevice = this.getById(id);
        if (mdDevice != null) {
            name = mdDevice.getDeviceName() + "_" + mdDevice.getDeviceCode();
        }
        return name;
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + "mdDeviceOnlyName", key = "#id")
    public String getOnlyNameById(Serializable id) {
        String name = "";

        MdDevice mdDevice = this.getById(id);
        if (mdDevice != null) {
            name = mdDevice.getDeviceCode();
        }
        return name;
    }

    @Override
    public List<MdDevice> getDevicesBySubSystemId(Long subSystemId) {
        QueryWrapper<MdDevice> qw = new QueryWrapper<>();
        MdDevice mdDevice = new MdDevice();
        mdDevice.setSubSystemId(subSystemId);
        qw.setEntity(mdDevice);
        return this.list(qw);
    }

    @Override
    public Integer getTypeById(Serializable id) {
        Integer type = -1;

        MdDevice mdDevice = this.getById(id);
        if (mdDevice != null) {
            type = mdDevice.getDeviceType();
        }
        return type;
    }
}