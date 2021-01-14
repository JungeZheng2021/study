package com.aimsphm.nuclear.core.controller;

import com.aimsphm.nuclear.common.entity.AnalysisFavoriteDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.AnalysisFavoriteVO;
import com.aimsphm.nuclear.common.service.AnalysisFavoriteService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.core.controller
 * @Description: <振动分析收藏夹-前端控制器>
 * @Author: MILLA
 * @CreateDate: 2021-01-14
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@RestController
@Api(tags = "AnalysisFavorite-振动分析收藏夹-相关接口")
@RequestMapping(value = "analysis/favorite", produces = MediaType.APPLICATION_JSON_VALUE)
public class AnalysisFavoriteController {

    @Resource
    private AnalysisFavoriteService service;

    @GetMapping("list")
    @ApiOperation(value = "振动分析收藏夹分页查询", notes = "多条件组合查询")
    public Page<AnalysisFavoriteDO> listAnalysisFavoriteByPageWithParams(Page<AnalysisFavoriteDO> page, AnalysisFavoriteDO entity, ConditionsQueryBO query) {
        return service.listAnalysisFavoriteByPageWithParams(new QueryBO(page, entity, query));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "振动分析收藏夹获取某一实体")
    public AnalysisFavoriteDO getAnalysisFavoriteDetails(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiOperation(value = "振动分析收藏夹新增数据")
    public boolean saveAnalysisFavorite(@RequestBody AnalysisFavoriteVO dto) {
        return service.saveFavoriteAndRemark(dto);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "振动分析收藏夹修改数据")
    public boolean modifyAnalysisFavorite(@RequestBody AnalysisFavoriteDO dto, @PathVariable Long id) {
        dto.setId(id);
        return service.updateById(dto);
    }

    @DeleteMapping("batch")
    @ApiOperation(value = "振动分析收藏夹批量删除数据")
    public boolean batchRemoveAnalysisFavorite(@RequestParam(value = "ids") List<Long> ids) {
        return service.removeByIds(ids);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "振动分析收藏夹删除数据")
    public boolean removeAnalysisFavorite(@PathVariable Long id) {
        return service.removeById(id);
    }

    @GetMapping("single")
    @ApiOperation(value = "获取单个收藏的具体信息", notes = "包含备注信息")
    public AnalysisFavoriteVO getAnalysisFavoriteWithParams(AnalysisFavoriteDO entity) {
        return service.getAnalysisFavoriteWithParams(entity);
    }
}