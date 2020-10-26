package com.aimsphm.nuclear.common.mapper;

import com.aimsphm.nuclear.common.entity.MdSensorExtrainfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lu.yi
 * @since 2020-08-11
 */
@Repository
public interface MdSensorExtrainfoMapper extends BaseMapper<MdSensorExtrainfo> {
    List<MdSensorExtrainfo> selectFailedSensor(@Param("deviceId") long deviceId, @Param("type") int type);
}