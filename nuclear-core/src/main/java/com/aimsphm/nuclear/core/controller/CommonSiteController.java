package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.CommonSiteDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.TreeVO;
import com.aimsphm.nuclear.ext.service.CommonSiteServiceExt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <电厂信息-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2020-11-17
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-17
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "site-电厂信息-相关接口")
@RequestMapping(value = "/common/site", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonSiteController {
    @Autowired
    private CommonSiteServiceExt iCommonSiteServiceExt;

    @GetMapping("list")
    @ApiOperation(value = "电厂信息分页查询")
    public Page<CommonSiteDO> listCommonSiteServiceByPage(QueryBO<CommonSiteDO> query) {
        return iCommonSiteServiceExt.page(query.getPage() == null ? new Page() : query.getPage(), query.initQueryWrapper());
    }

    @GetMapping("{id}")
    @ApiOperation(value = "电厂信息获取某一实体")
    public CommonSiteDO getCommonSiteServiceDetails(@PathVariable Long id) {
        return iCommonSiteServiceExt.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "电厂信息新增数据")
    public boolean saveCommonSiteService(@RequestBody CommonSiteDO dto) {
        return iCommonSiteServiceExt.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "电厂信息修改数据")
    public boolean modifyCommonSiteService(@RequestBody CommonSiteDO dto, @PathVariable Long id) {
        dto.setId(id);
        return iCommonSiteServiceExt.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "电厂信息批量删除数据")
    public boolean batchRemoveCommonSiteService(@RequestParam(value = "ids") List<Long> ids) {
        return iCommonSiteServiceExt.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "电厂信息删除数据")
    public boolean removeCommonSiteService(@PathVariable Long id) {
        return iCommonSiteServiceExt.removeById(id);
    }

    @GetMapping("/tree/{id}")
    @ApiOperation(value = "获取某电厂信息结构树")
    public TreeVO getCommonSiteTree(@PathVariable Long id) {
        return iCommonSiteServiceExt.listCommonSetTree(id);
    }


}

