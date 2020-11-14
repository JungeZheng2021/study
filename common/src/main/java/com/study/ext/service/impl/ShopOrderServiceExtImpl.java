package com.study.ext.service.impl;

import com.study.ext.service.ShopOrderServiceExt;
import com.study.common.service.impl.ShopOrderServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.study.ext.service.impl
 * @Description: <订单信息扩展服务实现类>
 * @Author: milla
 * @CreateDate: 2020-11-14
 * @UpdateUser: milla
 * @UpdateDate: 2020-11-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config-service", name = "enable", havingValue = "true")
public class ShopOrderServiceExtImpl extends ShopOrderServiceImpl implements ShopOrderServiceExt {

}
