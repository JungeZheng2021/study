package com.aimsphm.nuclear.common.mapper;

import com.aimsphm.nuclear.common.entity.TxAlarmEvent;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointTimesVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lu.yi
 * @since 2020-04-07
 */
@Repository
public interface TxAlarmEventMapper extends BaseMapper<TxAlarmEvent> {

    List<MeasurePointTimesVO> selectAlarmByAlarmType(@Param("deviceId") Long deviceId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<MeasurePointTimesVO> selectAlarmByLevelType(@Param("deviceId") Long deviceId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<Long> selectWarmingPointsByDateDistribution(@Param("deviceId") Long deviceId);
}