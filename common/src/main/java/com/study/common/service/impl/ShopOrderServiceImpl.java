package com.study.common.service.impl;

import com.study.common.entity.ShopOrderDO;
import com.study.common.mapper.ShopOrderMapper;
import com.study.common.service.ShopOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @Package: com.study.common.service.impl
 * @Description: <订单信息服务实现类>
 * @Author: milla
 * @CreateDate: 2020-11-14
 * @UpdateUser: milla
 * @UpdateDate: 2020-11-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class ShopOrderServiceImpl extends ServiceImpl<ShopOrderMapper, ShopOrderDO> implements ShopOrderService {

}
