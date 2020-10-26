package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.TxDeviceStopStartRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;


import java.util.Date;
import java.util.List;

/**
 * @author lu.yi
 * @since 2020-06-12
 */
public interface TxDeviceStopStartRecordService extends IService<TxDeviceStopStartRecord> {

     TxDeviceStopStartRecord getPreviousRecordByColumn(String colName, Date baseDate,Long deviceId,Integer deviceType,Long exceptionalId);

     TxDeviceStopStartRecord getNextRecordByColumn(String colName, Date baseDate,Long deviceId,Integer deviceType,Long exceptionalId);

     List<TxDeviceStopStartRecord> getSuceedRecords(Date baseDate,Long deviceId,Integer deviceType,Long exceptionalId);

     Integer getMaxActiontims(Long deviceId,Integer deviceType);

     TxDeviceStopStartRecord selectForUpdateById(Long id);
}