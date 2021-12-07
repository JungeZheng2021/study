package com.example.order.core.service;


import com.example.order.core.entity.ShopOrderDO;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/12/06 16:33
 */
public interface ShopOrderOperateService {
    /**
     * 保存一个数据
     *
     * @param dto 订单信息
     * @return
     */
    boolean save(ShopOrderDO dto);

    /**
     * 取消订单
     *
     * @param orderId 订单超时
     * @return
     */
    boolean cancelOrder(Long orderId);
}
