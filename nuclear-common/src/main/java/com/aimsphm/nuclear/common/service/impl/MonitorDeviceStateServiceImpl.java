package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.MonitorDeviceStateDO;
import com.aimsphm.nuclear.common.mapper.MonitorDeviceStateMapper;
import com.aimsphm.nuclear.common.service.MonitorDeviceStateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.aimsphm.nuclear.common.service.impl
 * @Description: <设备状态服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-11-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class MonitorDeviceStateServiceImpl extends ServiceImpl<MonitorDeviceStateMapper, MonitorDeviceStateDO> implements MonitorDeviceStateService {

}
