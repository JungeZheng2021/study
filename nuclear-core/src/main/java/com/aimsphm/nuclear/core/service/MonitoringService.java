package com.aimsphm.nuclear.core.service;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;

import java.util.List;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.core.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/11/18 16:49
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/18 16:49
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface MonitoringService {

    /**
     * 设备检测接口
     *
     * @param deviceId 设备编号
     * @return
     */
    Map<String, MeasurePointVO> getMonitorInfo(Long deviceId);

    /**
     * 更新所有测点的缓存值
     *
     * @return
     */
    List<CommonMeasurePointDO> updatePointsData();

    /**
     * 测点监测接口
     *
     * @param deviceId 设备的id
     * @return
     */
    Map<String, List<MeasurePointVO>> getPointMonitorInfo(Long deviceId);

    /**
     * 计算超限的测点个数
     *
     * @param deviceId 设备id
     * @return
     */
    Map<Integer, Long> countTransfinitePiPoint(Long deviceId);
}
