package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.ext.service.CommonComponentServiceExt;
import com.aimsphm.nuclear.common.service.impl.CommonComponentServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.aimsphm.nuclear.ext.service.impl
 * @Description: <组件信息扩展服务实现类>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableServiceExtImpl", havingValue = "true")
public class CommonComponentServiceExtImpl extends CommonComponentServiceImpl implements CommonComponentServiceExt {

}
