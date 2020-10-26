package com.aimsphm.nuclear.core.controller;


import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavorite;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.service.MdVibrationAnalysisFavoriteService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 振动分析收藏夹
 *
 * @author lu.yi
 * @since 2020-08-14
 */
@Slf4j
@RestController
@RequestMapping("favorite")
@Api(tags = "旋机-振动分析用户收藏夹")
public class MdVibrationAnalysisFavoriteController {

    @Autowired
    private MdVibrationAnalysisFavoriteService favoriteService;

    @GetMapping("list")
    @ApiOperation(value = "收藏夹-获取所有的收藏夹信息")
    public Object listAnalysisFavorite(Page<MdVibrationAnalysisFavorite> page, MdVibrationAnalysisFavorite query, TimeRangeQueryBO timeBo) {
        return ResponseUtils.success(favoriteService.listAnalysisFavorite(page, query, timeBo));
    }

    @ApiOperation(value = "收藏夹-添加到收藏夹")
    @PostMapping
    public Object saveMdVibrationAnalysisFavorite(@RequestBody MdVibrationAnalysisFavorite favorite) {
        return ResponseUtils.success(favoriteService.saveMdVibrationAnalysisFavorite(favorite));
    }

    @PutMapping
    public Object modifyMdVibrationAnalysisFavorite(@RequestBody MdVibrationAnalysisFavorite favorite) {
        favorite.setHasRemark(null);
        return ResponseUtils.success(favoriteService.updateById(favorite));
    }

    @DeleteMapping("list/{id}")
    @ApiOperation(value = "收藏夹-删除收藏夹")
    public Object removeVibrationAnalysisFavorite(@PathVariable Long id) {
        return ResponseUtils.success(favoriteService.removeFavorite(id));
    }

    @DeleteMapping
    public Object delMdVibrationAnalysisFavorite(@RequestParam String ids) {
        return ResponseUtils.success(favoriteService.removeByIds(Arrays.asList(ids.split(","))));
    }
}
