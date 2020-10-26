package com.aimsphm.nuclear.core.mapper;

import com.aimsphm.nuclear.core.entity.MdRuntimeBaseCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Mao
 * @since 2020-05-11
 */
@Repository
public interface MdRuntimeBaseCfgMapper extends BaseMapper<MdRuntimeBaseCfg> {

    @Update("update md_runtime_base_cfg set initial_parameter_value=#{config.initialParameterValue},start_date_time=#{config.startDateTime},last_update_by=#{config.lastUpdateBy},last_update_on=#{currentDate} where id=#{config.id} and (last_update_on<#{currentDate} or last_update_on is null)")
    public int updateOptimisticLock(@Param("config") MdRuntimeBaseCfg config, @Param("currentDate") Date currentDate);


    /**
     * 根据设备编号获取旋机的运行时间配置
     *
     * @param deviceId
     * @return
     */
    List<MdRuntimeBaseCfg> getRotaryRuntimeConfig(@Param("deviceId") Long deviceId);
}