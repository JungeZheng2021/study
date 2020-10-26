package com.aimsphm.nuclear.pump.service;

import com.aimsphm.nuclear.common.entity.TxAlarmEvent;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.core.vo.PumpPanoramaVO;
import com.aimsphm.nuclear.core.vo.RotaryMonitorVO;

import java.util.List;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.pump.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 13:34
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 13:34
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface SystemPanoramaService {

    PumpPanoramaVO getPanoramaInfo(Long subSystemId);

    List<TxAlarmEvent> getWarningNewest(Long queryId, Integer top, boolean type);

    Map<String, MeasurePointVO> getMonitorInfo(Long deviceId);

    boolean getExchangerOrValveStatus(Long deviceId);

    Double getExchangerFlowMax();

    /**
     * 获取旋转机械的设备检测信息
     *
     * @param deviceId
     * @return
     */
    RotaryMonitorVO getMonitorInfoOfRotary(Long deviceId);
}
