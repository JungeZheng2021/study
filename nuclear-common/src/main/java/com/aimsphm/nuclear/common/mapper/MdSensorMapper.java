package com.aimsphm.nuclear.common.mapper;

import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointTimesScaleVO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointTimesVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author lu.yi
 * @since 2020-03-18
 */
@Repository
public interface MdSensorMapper extends BaseMapper<MdSensor> {

    Set<String> selectPointTagsBySubSystemId(@Param("subSystemId") Long subSystemId,
                                             @Param("deviceId") Long deviceId, @Param("locations") Boolean locationCodeNotNull);

    List<MeasurePointTimesVO> selectWarmingPointsByDeviceId(@Param("deviceId") Long deviceId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    @Update("UPDATE md_sensor\n" +
            "SET\n" +
            "early_warning_hi = #{sensor.earlyWarningHi},\n" +
            "early_warning_lo = #{sensor.earlyWarningLo},\n" +
            "thr_hi = #{sensor.thrHi},\n" +
            "thr_lo = #{sensor.thrLo},\n" +
            "thr_hihi = #{sensor.thrHihi},\n" +
            "thr_lolo = #{sensor.thrLolo},\n" +
            "importance = #{sensor.importance},\n" +
            "last_update_by = #{sensor.lastUpdateBy},\n" +
            "last_update_on = #{currentDate} \n" +
            "WHERE id = #{sensor.id} and (last_update_on<#{currentDate} or last_update_on is null)")
    int updateWarningThreadOptimisticLock(@Param("sensor") MdSensor sensor, @Param("currentDate") Date currentDate);

    List<MeasurePointTimesScaleVO> selectTbWarmingPointsByDeviceId(@Param("deviceId") Long deviceId, @Param("startTime") Long startTime, @Param("endTime") Long endTime);
}