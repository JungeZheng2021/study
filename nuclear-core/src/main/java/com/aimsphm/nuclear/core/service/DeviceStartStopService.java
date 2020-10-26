package com.aimsphm.nuclear.core.service;



import com.aimsphm.nuclear.common.entity.TxDeviceStopStartRecord;
import com.aimsphm.nuclear.common.exception.InvalidStartStopRecordException;
import com.aimsphm.nuclear.common.service.TxDeviceStopStartRecordService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DeviceStartStopService {

    /* TxDeviceStopStartRecord getPreviousRecord(Date baseDate);

     TxDeviceStopStartRecord getNextRecord(Date baseDate);*/

     Boolean createRecord(TxDeviceStopStartRecord record) throws Exception;

     Boolean updateRecord(TxDeviceStopStartRecord record) throws Exception;

     Boolean deleteRecord(Long id) throws Exception;

     List<TxDeviceStopStartRecord> getRecordsByDeviceIdAndType(Long deviceId,Integer deviceType);

     TxDeviceStopStartRecord getRecordById(Long deviceId);

}
