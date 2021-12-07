package com.example.order.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.order.core.entity.ShopOrderDO;
import com.example.order.core.listener.OrderMessageSender;
import com.example.order.core.service.ShopOrderOperateService;
import com.example.order.core.service.ShopOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/12/06 17:06
 */
@Slf4j
@Service
public class ShopOrderOperateServiceImpl implements ShopOrderOperateService {
    @Resource
    OrderMessageSender sender;
    @Autowired
    private ShopOrderService ext;

    @Override
    public boolean save(ShopOrderDO dto) {
        try {
            dto.setStatus(1);
            boolean save = ext.save(dto);
            if (save) {
                sender.sendMessage(dto.getId(), 1 * 60 * 1000);
            }
            return true;
        } catch (Exception e) {
            log.error(".....{}", e);
        }
        return false;
    }

    @Override
    public boolean cancelOrder(Long orderId) {
        //把待支付中的订单设置成订单失效
        LambdaUpdateWrapper<ShopOrderDO> update = Wrappers.lambdaUpdate();
        update.eq(ShopOrderDO::getId, orderId);
        update.eq(ShopOrderDO::getStatus, 1);
        update.set(ShopOrderDO::getStatus, 3);
        update.set(ShopOrderDO::getGmtModified, new Date());
        update.set(ShopOrderDO::getModifier, "auto");
        update.set(ShopOrderDO::getRemark, "过期了");
        return ext.update(update);
    }
}
