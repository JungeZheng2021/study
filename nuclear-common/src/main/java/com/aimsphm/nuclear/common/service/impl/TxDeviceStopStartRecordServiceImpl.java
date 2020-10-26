package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.TxDeviceStopStartRecord;
import com.aimsphm.nuclear.common.mapper.TxDeviceStopStartRecordMapper;
import com.aimsphm.nuclear.common.service.TxDeviceStopStartRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-06-12
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class TxDeviceStopStartRecordServiceImpl extends ServiceImpl<TxDeviceStopStartRecordMapper, TxDeviceStopStartRecord> implements TxDeviceStopStartRecordService {

    @Autowired
    private TxDeviceStopStartRecordMapper TxDeviceStopStartRecordMapper;


    @Override
    public TxDeviceStopStartRecord getPreviousRecordByColumn(String colName, Date baseDate,Long deviceId, Integer deviceType,Long exceptionalId) {
        return TxDeviceStopStartRecordMapper.getPreviousRecordByColumn(colName,baseDate,deviceId,deviceType,exceptionalId);
    }

    @Override
    public TxDeviceStopStartRecord getNextRecordByColumn(String colName, Date baseDate,Long deviceId,Integer deviceType,Long exceptionalId) {
        return TxDeviceStopStartRecordMapper.getNextRecordByColumn(colName,baseDate,deviceId,deviceType,exceptionalId);
    }

    @Override
    public List<TxDeviceStopStartRecord> getSuceedRecords(Date baseDate,Long deviceId,Integer deviceType,Long exceptionalId) {
        return TxDeviceStopStartRecordMapper.getSucceedRecords(baseDate,deviceId,deviceType,exceptionalId);
    }

    @Override
    public Integer getMaxActiontims(Long deviceId,Integer deviceType) {
        return TxDeviceStopStartRecordMapper.getMaxActiontims(deviceId,deviceType);
    }

    @Override
    public TxDeviceStopStartRecord selectForUpdateById(Long id) {
        return TxDeviceStopStartRecordMapper.selectForUpdateById(id);
    }


}