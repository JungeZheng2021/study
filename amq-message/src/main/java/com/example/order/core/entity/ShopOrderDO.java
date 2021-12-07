package com.example.order.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 功能描述:实体
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-12-07
 */
@Data
@TableName("shop_order")
@ApiModel(value = "实体")
public class ShopOrderDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -8985653170140721455L;

    @ApiModelProperty(value = "订单状态", notes = "1:待支付,2:支付成功，3:订单超时 4:订单支付失败")
    private Integer status;

    @ApiModelProperty(value = "重要成都", notes = "")
    private Integer importance;

    @ApiModelProperty(value = "备注", notes = "")
    private String remark;

    @Override
    public String toString() {
        return "ShopOrderDO{" +
                " id=" + id +
                ", status=" + status +
                '}' + "过了：" + (System.currentTimeMillis() - this.getGmtCreate().getTime()) / 1000 + "s";
    }
}