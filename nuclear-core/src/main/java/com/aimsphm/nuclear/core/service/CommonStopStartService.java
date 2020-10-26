package com.aimsphm.nuclear.core.service;

import com.aimsphm.nuclear.common.entity.TxAlarmEvent;
import com.aimsphm.nuclear.common.entity.TxAlarmRealtime;
import com.aimsphm.nuclear.common.entity.TxDeviceStopStartRecord;
import com.aimsphm.nuclear.common.entity.bo.AlarmEventQueryPageBO;
import com.aimsphm.nuclear.common.entity.bo.AlarmRealtimeQueryPageBO;
import com.aimsphm.nuclear.common.entity.dto.Cell;
import com.aimsphm.nuclear.common.entity.vo.CommonSensorVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.core.service
 * @Description: <热点数据操作类>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 14:19
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 14:19
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface CommonStopStartService {

    List<CommonSensorVO> getAllSubSystemSensors(Long subSystemId,Long deviceId);


    Map<String,Map> analyseStopStart(CommonSensorVO xCommonSensorVO, CommonSensorVO yCommonSensorVO, Long onset, Long offset, List<TxDeviceStopStartRecord> record, Boolean startFlag);


    List<Cell> getByCommonSensorVO(CommonSensorVO yCommonSensorVO, Long start, Long end);

    Map<String, Object> analyseStopStartXTime(CommonSensorVO yCommonSensorVO, Long onset, Long offset, List<TxDeviceStopStartRecord> records, Long baseRecordId, Boolean startFlag);
}
