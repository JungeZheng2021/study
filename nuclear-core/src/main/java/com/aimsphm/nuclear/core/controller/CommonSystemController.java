package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonSystemDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.ext.service.CommonSystemServiceExt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <系统信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "system-系统信息-相关接口")
@RequestMapping(value = "/common/system", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonSystemController {
    @Autowired
    private CommonSystemServiceExt iCommonSystemServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "系统信息分页查询")
    public Page<CommonSystemDO> listCommonSystemServiceByPage(QueryBO<CommonSystemDO> query) {
        return iCommonSystemServiceExt.page(query.getPage() == null ? new Page() : query.getPage(), query.initQueryWrapper());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "系统信息获取某一实体")
    public CommonSystemDO getCommonSystemServiceDetails(@PathVariable Long id) {
        return iCommonSystemServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "系统信息新增数据")
    public boolean saveCommonSystemService(@RequestBody CommonSystemDO dto) {
        return iCommonSystemServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "系统信息修改数据")
    public boolean modifyCommonSystemService(@RequestBody CommonSystemDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iCommonSystemServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "系统信息批量删除数据")
    public boolean batchRemoveCommonSystemService(@RequestParam(value = "ids") List<Long> ids) {
        return iCommonSystemServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "系统信息删除数据")
    public boolean removeCommonSystemService(@PathVariable Long id) {
        return iCommonSystemServiceExt.removeById(id);
    }

    @GetMapping("/tree/{id}")
    @ApiOperation(value = "获取某系统信息结构树")
    public TreeVO getCommonSystemTree(@PathVariable Long id) {
        return iCommonSystemServiceExt.listCommonSystemTree(id);
    }
}