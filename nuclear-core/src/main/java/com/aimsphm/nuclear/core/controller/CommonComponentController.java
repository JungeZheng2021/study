package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonComponentDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.ext.service.CommonComponentServiceExt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <组件信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "component-组件信息-相关接口")
@RequestMapping(value = "/common/component", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonComponentController {
    @Autowired
    private CommonComponentServiceExt iCommonComponentServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "组件信息分页查询")
    public Page<CommonComponentDO> listCommonComponentServiceByPage(QueryBO<CommonComponentDO> query) {
        return iCommonComponentServiceExt.page(query.getPage() == null ? new Page() : query.getPage(), query.initQueryWrapper());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "组件信息获取某一实体")
    public CommonComponentDO getCommonComponentServiceDetails(@PathVariable Long id) {
        return iCommonComponentServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "组件信息新增数据")
    public boolean saveCommonComponentService(@RequestBody CommonComponentDO dto) {
        return iCommonComponentServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "组件信息修改数据")
    public boolean modifyCommonComponentService(@RequestBody CommonComponentDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iCommonComponentServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "组件信息批量删除数据")
    public boolean batchRemoveCommonComponentService(@RequestParam(value = "ids") List<Long> ids) {
        return iCommonComponentServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "组件信息删除数据")
    public boolean removeCommonComponentService(@PathVariable Long id) {
        return iCommonComponentServiceExt.removeById(id);
    }
}