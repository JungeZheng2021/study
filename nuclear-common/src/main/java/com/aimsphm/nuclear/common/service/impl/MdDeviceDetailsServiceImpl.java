package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.MdDevice;
import com.aimsphm.nuclear.common.entity.MdDeviceDetails;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.MdDeviceDetailsMapper;
import com.aimsphm.nuclear.common.mapper.MdDeviceMapper;
import com.aimsphm.nuclear.common.service.MdDeviceDetailsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 设备详细信息
 *
 * @author MILLA
 * @since 2020-06-10
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class MdDeviceDetailsServiceImpl extends ServiceImpl<MdDeviceDetailsMapper, MdDeviceDetails> implements MdDeviceDetailsService {

    @Autowired
    private MdDeviceDetailsMapper MdDeviceDetailsMapper;
    @Autowired
    private MdDeviceMapper deviceMapper;
    /**
     * 上次停机时间
     */
    public static final String LAST_STOP_TIME = "last_stop_time";
    /**
     * 上次启动时间
     */
    public static final String LAST_START_TIME = "last_start_time";


    @Override
    public List<MdDeviceDetails> listDeviceInfo(Long subSystemId) {
        QueryWrapper<MdDeviceDetails> query = new QueryWrapper<>();
        query.lambda().eq(MdDeviceDetails::getSubSystemId, subSystemId);
        query.lambda().orderByDesc(MdDeviceDetails::getImportance);
        return this.list(query);
    }

    @Override
    public List<MdDeviceDetails> listDeviceInfo(MdDeviceDetails query) {
        QueryWrapper<MdDeviceDetails> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(query.getSystemId())) {
            wrapper.lambda().eq(MdDeviceDetails::getSystemId, query.getSystemId());
        }
        if (Objects.nonNull(query.getId())) {
            wrapper.lambda().eq(MdDeviceDetails::getId, query.getId());
        }
        if (Objects.nonNull(query.getSubSystemId())) {
            wrapper.lambda().eq(MdDeviceDetails::getSubSystemId, query.getSubSystemId());
        }
        //如果传了设备id需要将当前子系统下公共的信息也查询出来
        if (Objects.nonNull(query.getDeviceId())) {
            wrapper.lambda().eq(MdDeviceDetails::getDeviceId, query.getDeviceId());
            MdDevice device = deviceMapper.selectById(query.getDeviceId());
            if (Objects.isNull(device)) {
                throw new CustomMessageException("this device is not exist");
            }
            if (Objects.isNull(query.getSystemId())) {
                wrapper.lambda().or(true, w -> w.eq(MdDeviceDetails::getSystemId, device.getSubSystemId()).isNull(MdDeviceDetails::getDeviceId));
            }
        }
        if (StringUtils.hasText(query.getFieldNameEn())) {
            wrapper.lambda().eq(MdDeviceDetails::getFieldNameEn, query.getFieldNameEn());
        }
        wrapper.lambda().orderByDesc(MdDeviceDetails::getImportance);
        return this.list(wrapper);
    }

    @Override
    public Map<String, Date> getLatTimes(Long deviceId) {
        MdDevice mdDevice = deviceMapper.selectById(deviceId);
        if (Objects.isNull(mdDevice)) {
            return null;
        }
        Long subSystemId = mdDevice.getSubSystemId();
        if (Objects.isNull(subSystemId)) {
            return null;
        }
        QueryWrapper<MdDeviceDetails> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MdDeviceDetails::getSystemId, subSystemId);
        wrapper.lambda().eq(MdDeviceDetails::getFieldNameEn, LAST_STOP_TIME).or().eq(MdDeviceDetails::getFieldNameEn, LAST_START_TIME);
        List<MdDeviceDetails> list = this.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Map<String, Date> data = Maps.newHashMap();
        for (MdDeviceDetails m : list) {
            String fieldValue = m.getFieldValue();
            if (StringUtils.isEmpty(fieldValue)) {
                continue;
            }
            try {
                long l = Long.parseLong(fieldValue);
//                变成毫秒值
                data.put(m.getFieldNameEn(), new Date(l * 1000));
            } catch (NumberFormatException e) {
                log.error("字符串转换程整型异常{}", e);
            }
        }
        return data;
    }
}