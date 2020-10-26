package com.aimsphm.nuclear.core.mapper;

import com.aimsphm.nuclear.core.entity.MdPumpinfo;
import com.aimsphm.nuclear.core.entity.MdRuntimeBaseCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author lu.yi
 * @since 2020-03-18
 */
@Repository
public interface MdPumpinfoMapper extends BaseMapper<MdPumpinfo> {

    @Update("UPDATE md_pumpinfo\n" +
            "SET\n" +
            "pump_type = #{config.pumpType},\n" +
            "last_repair_time = #{config.lastRepairTime},\n" +
            "pump_material = #{config.pumpMaterial},\n" +
            "rate_flow = #{config.rateFlow},\n" +
            "rate_head = #{config.rateHead},\n" +
            "design_pressure = #{config.designPressure},\n" +
            "design_temperature = #{config.designTemperature},\n" +
            "cooling_water_inlet_temperature = #{config.coolingWaterInletTemperature},\n" +
            "outlet_nozzle_inner_diameter = #{config.outletNozzleInnerDiameter},\n" +
            "inlet_nozzle_inner_diameter = #{config.inletNozzleInnerDiameter},\n" +
            "design_life = #{config.designLife},\n" +
            "system_power = #{config.systemPower},\n" +
            "operation_pressure = #{config.operationPressure},\n" +
            "primary_water_capacity = #{config.primaryWaterCapacity},\n" +
            "last_update_by=#{config.lastUpdateBy},\n" +
            "last_update_on = #{currentDate} \n" +
            "where id=#{config.id} and (last_update_on<#{currentDate} or last_update_on is null)")

    public int updateOptimisticLock(@Param("config") MdPumpinfo config, @Param("currentDate") Date currentDate);
}