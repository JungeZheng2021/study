package com.aimsphm.nuclear.ext.service;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.entity.vo.DeviceStatusVO;
import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

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
     * 获取redis中的测点信息
     *
     * @param wrapper
     * @return
     */
    List<MeasurePointVO> listPointByWrapper(LambdaQueryWrapper<CommonMeasurePointDO> wrapper);

    /**
     * 更新所有测点的缓存值
     *
     * @param defaultValue
     * @param queryBO
     * @return
     */
    List<MeasurePointVO> updatePointsData(boolean defaultValue, CommonQueryBO queryBO);

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

    /**
     * 根据时间和设备统计运行时长
     *
     * @param deviceId 设备id
     * @param range    开始时间
     * @return
     */
    Map<Integer, Long> listRunningDuration(Long deviceId, TimeRangeQueryBO range);

    /**
     * 根据时间和设备统计报警信息
     *
     * @param deviceId
     * @param range
     * @return
     */
    List<List<LabelVO>> listWarningPoint(Long deviceId, TimeRangeQueryBO range);

    /**
     * 设备运行状态
     *
     * @param deviceId 设备id
     * @return
     */
    DeviceStatusVO getRunningStatus(Long deviceId);

    /**
     * 修改基础信息
     *
     * @param statusVO 状态
     * @return
     */
    boolean modifyDeviceStatus(DeviceStatusVO statusVO);
}
