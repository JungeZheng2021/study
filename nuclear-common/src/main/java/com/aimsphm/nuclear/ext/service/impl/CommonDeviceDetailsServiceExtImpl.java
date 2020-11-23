package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.ext.service.CommonDeviceDetailsServiceExt;
import com.aimsphm.nuclear.common.service.impl.CommonDeviceDetailsServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.aimsphm.nuclear.ext.service.impl
 * @Description: <设备详细信息扩展服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonDeviceDetailsServiceExtImpl extends CommonDeviceDetailsServiceImpl implements CommonDeviceDetailsServiceExt {

}
