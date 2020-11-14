package com.study.core.controller;

import com.study.common.entity.ShopOrderDO;
import com.study.common.entity.bo.QueryBO;
import com.study.ext.service.ShopOrderServiceExt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.study.core.controller
 * @Description: <订单信息前端控制器>
 * @Author: milla
 * @CreateDate: 2020-11-14
 * @UpdateUser: milla
 * @UpdateDate: 2020-11-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "订单信息-相关接口")
@RequestMapping(value = "/shop/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopOrderController {
    @Autowired
    private ShopOrderServiceExt iShopOrderServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "订单信息分页查询")
    public Page<ShopOrderDO> listShopOrderServiceByPage(QueryBO<ShopOrderDO> query) {
        return iShopOrderServiceExt.page(query.getPage() == null ? new Page() : query.getPage(), query.initQueryWrapper());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "订单信息获取某一实体")
    public ShopOrderDO getShopOrderServiceDetails(@PathVariable Long id) {
        return iShopOrderServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "订单信息新增数据")
    public boolean saveShopOrderService(@RequestBody ShopOrderDO dto) {
        return iShopOrderServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "订单信息修改数据")
    public boolean modifyShopOrderService(@RequestBody ShopOrderDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iShopOrderServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "订单信息批量删除数据")
    public boolean batchRemoveShopOrderService(@RequestParam(value = "ids") List<Long> ids) {
        return iShopOrderServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "订单信息删除数据")
    public boolean removeShopOrderService(@PathVariable Long id) {
        return iShopOrderServiceExt.removeById(id);
    }
}