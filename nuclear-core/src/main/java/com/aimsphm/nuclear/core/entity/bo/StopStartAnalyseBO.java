package com.aimsphm.nuclear.core.entity.bo;

import com.aimsphm.nuclear.common.entity.TxDeviceStopStartRecord;
import com.aimsphm.nuclear.common.entity.vo.CommonSensorVO;
import lombok.Data;

import java.util.List;


@Data
public class StopStartAnalyseBO {
    CommonSensorVO xCommonSensorVO;
    CommonSensorVO yCommonSensorVO;
    Long onset;
    Long offset;
    List<TxDeviceStopStartRecord> records;
    Long baseRecordId;
    Boolean startFlag;

}
