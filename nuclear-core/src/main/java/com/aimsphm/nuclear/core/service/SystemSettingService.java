package com.aimsphm.nuclear.core.service;

import com.aimsphm.nuclear.common.entity.SysLog;
import com.aimsphm.nuclear.common.entity.bo.SysLogQueryBo;
import com.aimsphm.nuclear.core.entity.bo.MdSensorDaqConfigBO;
import com.aimsphm.nuclear.core.entity.bo.MdSensorExtrainfoBO;
import com.aimsphm.nuclear.core.entity.bo.MdSensorConfigBO;
import com.aimsphm.nuclear.core.vo.DeviceAlarmSettingVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 报告表
 *
 * @author lu.yi
 * @since 2020-04-30
 */

public interface SystemSettingService {


    List<DeviceAlarmSettingVO> getDeviceAlarmSetting(Long subSystemId);

    @Transactional
    void resetAlarm(Long deviceId);

    @Transactional
    void disableAlarm(Long deviceId);

    @Transactional
    void enableAlarm(Long deviceId);

    void purgeRedis();

    Integer modifySensorDetail(MdSensorExtrainfoBO bo);

    Map<String, List<MdSensorConfigBO>> sensorConfigList(Long deviceId);

    List<MdSensorDaqConfigBO> daqConfigDetail(Long sensorId);


    Boolean daqConfig(MdSensorDaqConfigBO bo);

    Page<SysLog> getDailyUsersLog(SysLogQueryBo sysLogQueryBo);
}