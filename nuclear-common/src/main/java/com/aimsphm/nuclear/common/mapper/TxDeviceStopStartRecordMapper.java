package com.aimsphm.nuclear.common.mapper;

import com.aimsphm.nuclear.common.entity.TxDeviceStopStartRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author lu.yi
 * @since 2020-06-12
 */
@Repository
@Mapper
public interface TxDeviceStopStartRecordMapper extends BaseMapper<TxDeviceStopStartRecord> {

    @Select({"<script> select * from tx_device_stop_start_record where ${colName} &lt;= #{baseDate} and device_id=#{deviceId} and device_type=#{deviceType} and delimit = 0  <when test='exceptionalId!=null '> and id!=#{exceptionalId} </when>  order by ${colName} desc limit 1 for update </script>"})
     TxDeviceStopStartRecord getPreviousRecordByColumn(@Param("colName") String colName, @Param("baseDate") Date baseDate,@Param("deviceId") Long deviceId,@Param("deviceType") Integer deviceType,@Param("exceptionalId") Long excepionalId);

    @Select({"<script> select * from tx_device_stop_start_record where ${colName} &gt;= #{baseDate} and device_id=#{deviceId}  and device_type=#{deviceType}  and delimit = 0 <when test='exceptionalId!=null '> and id!=#{exceptionalId} </when>  order by ${colName} limit 1 for update </script>"})
     TxDeviceStopStartRecord getNextRecordByColumn(@Param("colName") String colName, @Param("baseDate") Date baseDate,@Param("deviceId") Long deviceId,@Param("deviceType") Integer deviceType,@Param("exceptionalId") Long excepionalId);

    @Select({"<script> select * from tx_device_stop_start_record where (start_begin_time &gt; #{baseDate} or start_end_time &gt; #{baseDate} or stop_begin_time &gt;  #{baseDate} or stop_end_time &gt; #{baseDate}) and device_id=#{deviceId}  and device_type=#{deviceType}  and delimit = 0  <when test='exceptionalId!=null '> and id!=#{exceptionalId} </when> for update </script>"})
     List<TxDeviceStopStartRecord> getSucceedRecords(@Param("baseDate") Date baseDate,@Param("deviceId") Long deviceId,@Param("deviceType") Integer deviceType,@Param("exceptionalId") Long excepionalId);

    @Select("select max(action_times) from tx_device_stop_start_record where device_id=#{deviceId} and  device_type=#{deviceType} and delimit = 0")
     Integer getMaxActiontims(@Param("deviceId") Long deviceId,@Param("deviceType") Integer deviceType);

    @Select("select * from tx_device_stop_start_record where id=#{id} for update")
    TxDeviceStopStartRecord selectForUpdateById(@Param("id") Long id);


}