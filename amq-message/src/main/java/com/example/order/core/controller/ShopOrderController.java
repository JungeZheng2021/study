package com.example.order.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.order.core.entity.ShopOrderDO;
import com.example.order.core.service.ShopOrderOperateService;
import com.example.order.core.service.ShopOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/12/06 16:33
 */
@RestController
@Api(tags = "-相关接口")
@RequestMapping(value = "/shop/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopOrderController {

    @Autowired
    private ShopOrderOperateService operateService;


    @Autowired
    private ShopOrderService ext;

    @GetMapping("list")
    @ApiOperation(value = "查询集合")
    public List<ShopOrderDO> listShopOrderServiceByPage(ShopOrderDO query) {
        LambdaQueryWrapper<ShopOrderDO> wrapper = Wrappers.lambdaQuery(query);
        return ext.list(wrapper);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "获取某一实体")
    public ShopOrderDO getShopOrderServiceDetails(@PathVariable Long id) {
        return ext.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public boolean saveShopOrderService(@RequestBody ShopOrderDO dto) {
        dto.setGmtCreate(new Date());
        dto.setGmtModified(new Date());
        dto.setCreator("初始化");
        dto.setRemark("新建");
        return operateService.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "修改数据")
    public boolean modifyShopOrderService(@RequestBody ShopOrderDO dto, @PathVariable Long id) {
        dto.setId(id);
        dto.setGmtModified(new Date());
        dto.setModifier("更新");
        dto.setRemark("手动更新");
        return ext.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "批量删除数据")
    public boolean batchRemoveShopOrderService(@RequestParam(value = "ids") List<Long> ids) {
        return ext.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "删除数据")
    public boolean removeShopOrderService(@PathVariable Long id) {
        return ext.removeById(id);
    }
}