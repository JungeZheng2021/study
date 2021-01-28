package com.aimsphm.nuclear.common.mapper;

import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.mapper
 * @Description: <报警事件信息Mapper接口>
 * @Author: MILLA
 * @CreateDate: 2020-12-09
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-09
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface JobAlarmEventMapper extends BaseMapper<JobAlarmEventDO> {
    /**
     * 获取测点的个数
     *
     * @param deviceId 设备id
     * @param range    起止时间
     * @return
     */
    List<LabelVO> selectWarmingPointsByDeviceId(@Param("deviceId") Long deviceId, @Param("range") TimeRangeQueryBO range);

    /**
     * 获取测点的个数
     *
     * @param deviceId 设备id
     * @return
     */
    List<LabelVO> selectWarmingStatusPoints(@Param("deviceId") Long deviceId);

    /**
     * 报警时间分布
     *
     * @param deviceId 设备id
     * @return
     */
    List<LabelVO> selectWarmingPointsByDateDistribution(@Param("deviceId") Long deviceId);
}
