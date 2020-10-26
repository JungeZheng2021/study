package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.TxPumpsnapshot;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;

import java.util.List;
import java.util.Set;

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
public interface HotSpotDataService {
    List<TxPumpsnapshot> listPumpSnapshot(Long subSystemId);

    boolean setPumpSnapshot(TxPumpsnapshot value);

    /**
     * 根据deviceId获取热点数据
     *
     * @param deviceId            设备id
     * @param locationCodeNotNull 是否需要指定位置标识符不为空
     * @return
     */
    List<MeasurePointVO> getHotPointsByDeviceId(Long deviceId, Boolean locationCodeNotNull);

    /**
     * 根据子系统编号获取热点数据
     *
     * @param subSystemId         子系统id
     * @param locationCodeNotNull 是否需要指定位置标识符不为空
     * @return
     */
    List<MeasurePointVO> getHotPointsBySubSystemId(Long subSystemId, Boolean locationCodeNotNull);

    TxPumpsnapshot getPumpSnapshot(Long deviceId);

    List<MeasurePointVO> getWarmingPumpPointsByDeviceId(Long deviceId);

    List<MeasurePointVO> getPoints(Set<String> tags);

    Object getDeviceSnapshot(Long deviceId);
}
