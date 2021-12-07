package com.study.ext.service.impl;

import com.study.common.service.impl.ShopOrderServiceImpl;
import com.study.ext.service.ShopOrderServiceExt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @Package: com.study.ext.service.impl
 * @Description: <扩展服务实现类>
 * @Author: milla
 * @CreateDate: 2021-12-06
 * @UpdateUser: milla
 * @UpdateDate: 2021-12-06
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config-service", name = "enable", havingValue = "true")
public class ShopOrderServiceExtImpl extends ShopOrderServiceImpl implements ShopOrderServiceExt {

}
