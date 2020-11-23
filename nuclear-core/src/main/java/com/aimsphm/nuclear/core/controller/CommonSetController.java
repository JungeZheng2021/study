package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonSetDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.ext.service.CommonSetServiceExt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <机组信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "机组信息-相关接口")
@RequestMapping(value = "/common/set", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonSetController {
    @Autowired
    private CommonSetServiceExt iCommonSetServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "机组信息分页查询")
    public Page<CommonSetDO> listCommonSetServiceByPage(QueryBO<CommonSetDO> query) {
        return iCommonSetServiceExt.page(query.getPage() == null ? new Page() : query.getPage(), query.initQueryWrapper());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "机组信息获取某一实体")
    public CommonSetDO getCommonSetServiceDetails(@PathVariable Long id) {
        return iCommonSetServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "机组信息新增数据")
    public boolean saveCommonSetService(@RequestBody CommonSetDO dto) {
        return iCommonSetServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "机组信息修改数据")
    public boolean modifyCommonSetService(@RequestBody CommonSetDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iCommonSetServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "机组信息批量删除数据")
    public boolean batchRemoveCommonSetService(@RequestParam(value = "ids") List<Long> ids) {
        return iCommonSetServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "机组信息删除数据")
    public boolean removeCommonSetService(@PathVariable Long id) {
        return iCommonSetServiceExt.removeById(id);
    }

    @GetMapping("/tree/{id}")
    @ApiOperation(value = "获取某机组信息结构树")
    public TreeVO getCommonSetTree(@PathVariable Long id) {
        return iCommonSetServiceExt.listCommonSetTree(id);
    }
}