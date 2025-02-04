package com.study.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.study.common.entity
 * @Description: <实体>
 * @Author: milla
 * @CreateDate: 2021-12-06
 * @UpdateUser: milla
 * @UpdateDate: 2021-12-06
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("shop_order")
@ApiModel(value = "实体")
public class ShopOrderDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = 8985653170140721455L;

    @ApiModelProperty(value = "", notes = "订单状态：1:待支付,2:支付成功，3:订单超时 4:订单支付失败")
    private Integer status;

    @Override
    public String toString() {
        return "ShopOrderDO{" +
                " id=" + id +
                ", status=" + status +
                '}' + "过了：" + (System.currentTimeMillis() - this.getGmtCreate().getTime()) / 1000 + "s";
    }
}