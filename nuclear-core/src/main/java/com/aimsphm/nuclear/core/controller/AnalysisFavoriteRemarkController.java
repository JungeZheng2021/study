package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.AnalysisFavoriteRemarkDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.service.AnalysisFavoriteRemarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <振动分析用户备注-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2021-01-14
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "AnalysisFavoriteRemark-振动分析用户备注-相关接口")
@RequestMapping(value = "analysis/favoriteRemark", produces = MediaType.APPLICATION_JSON_VALUE)
public class AnalysisFavoriteRemarkController {

    @Resource
    private AnalysisFavoriteRemarkService service;

    @GetMapping("list")
    @ApiOperation(value = "振动分析用户备注分页查询", notes = "多条件组合查询")
    public Page<AnalysisFavoriteRemarkDO> listAnalysisFavoriteRemarkByPageWithParams(Page<AnalysisFavoriteRemarkDO> page, AnalysisFavoriteRemarkDO entity, ConditionsQueryBO query) {
        return service.listAnalysisFavoriteRemarkByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{favoriteId}")
    @ApiOperation(value = "根据收藏夹id获取所有的备注信息")
    public List<AnalysisFavoriteRemarkDO> getAnalysisFavoriteRemarkDetails(@PathVariable Long favoriteId) {
        return service.listRemarkByFavoriteId(favoriteId);
    }

    @PostMapping
    @ApiOperation(value = "振动分析用户备注新增数据")
    public boolean saveAnalysisFavoriteRemark(@RequestBody AnalysisFavoriteRemarkDO dto) {
        return service.save(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "振动分析用户备注修改数据")
    public boolean modifyAnalysisFavoriteRemark(@RequestBody AnalysisFavoriteRemarkDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "振动分析用户备注批量删除数据")
    public boolean batchRemoveAnalysisFavoriteRemark(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "振动分析用户备注删除数据")
    public boolean removeAnalysisFavoriteRemark(@PathVariable Long id) {
        return service.removeById(id);
    }
}