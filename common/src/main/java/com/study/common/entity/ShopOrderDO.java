package com.study.common.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.study.common.entity
 * @Description: <订单信息实体>
 * @Author: milla
 * @CreateDate: 2020-11-14
 * @UpdateUser: milla
 * @UpdateDate: 2020-11-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("shop_order")
@ApiModel(value = "订单信息实体")
public class ShopOrderDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = 8985653170140721455L;

    @ApiModelProperty(value = "金额", notes = "")
    private BigDecimal mount;

    @ApiModelProperty(value = "状态", notes = "0：未支付 1：已经支付")
    private Boolean status;

}