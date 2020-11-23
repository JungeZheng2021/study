package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.ext.service.MonitorDeviceStateServiceExt;
import com.aimsphm.nuclear.common.service.impl.MonitorDeviceStateServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.aimsphm.nuclear.ext.service.impl
 * @Description: <设备状态扩展服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-11-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class MonitorDeviceStateServiceExtImpl extends MonitorDeviceStateServiceImpl implements MonitorDeviceStateServiceExt {

}
