package com.aimsphm.nuclear.ext.mapper;

import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.entity.vo.LabelVO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointTimesVO;
import com.aimsphm.nuclear.common.mapper.JobAlarmEventMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.ext.mapper
 * @Description: <报警事件MapperExt接口>
 * @Author: MILLA
 * @CreateDate: 2020-12-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface JobAlarmEventMapperExt extends JobAlarmEventMapper {
    /**
     * 获取测点的个数
     *
     * @param deviceId 设备id
     * @param range    起止时间
     * @return
     */
    List<LabelVO> selectWarmingPointsByDeviceId(@Param("deviceId") Long deviceId, @Param("range") TimeRangeQueryBO range);

    /**
     * 报警时间分布
     *
     * @param deviceId 设备id
     * @return
     */
    List<LabelVO> selectWarmingPointsByDateDistribution(@Param("deviceId") Long deviceId);
}
