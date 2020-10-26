package com.aimsphm.nuclear.pump.service;

import com.aimsphm.nuclear.common.entity.TxPumpsnapshot;
import com.aimsphm.nuclear.common.entity.TxTurbinesnapshot;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointTimesScaleVO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.pump.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/14 18:24
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/14 18:24
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface PumpEquipmentMonitoringService {
    TxPumpsnapshot getRunningStatus(Long deviceId);

    List<MeasurePointVO> listWarmingPoint(Long deviceId);

    List<List<MeasurePointTimesScaleVO>> statisticsWarmingPoints(Long deviceId, Long startTime, Long endTime);

    List<MeasurePointTimesScaleVO> statisticsRunningStatus(Long deviceId);

    List<List<MeasurePointTimesScaleVO>> turbineStatisticsWarmingPoints(Long deviceId, Long startTime, Long endTime);

    List<MeasurePointTimesScaleVO> turbineStatisticsRunningStatus(Long deviceId);

    TxTurbinesnapshot getRunningStatusTurbine(Long deviceId);

    Object getMonitorStatusOfRotary(Long deviceId);

    /**
     * 旋机运行时长统计
     *
     * @param deviceId 设备id
     * @return
     */
    MeasurePointTimesScaleVO getMonitorStatisticsOfRotary(Long deviceId);
}
