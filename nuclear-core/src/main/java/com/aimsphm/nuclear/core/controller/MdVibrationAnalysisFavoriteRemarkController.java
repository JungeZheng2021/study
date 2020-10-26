package com.aimsphm.nuclear.core.controller;


import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavorite;
import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavoriteRemark;
import com.aimsphm.nuclear.common.entity.bo.RemarkBO;
import com.aimsphm.nuclear.common.entity.vo.FavoriteDetailsVO;
import com.aimsphm.nuclear.common.response.ResponseUtils;
import com.aimsphm.nuclear.common.service.MdVibrationAnalysisFavoriteRemarkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 振动分析用户备注
 *
 * @author lu.yi
 * @since 2020-08-14
 */
@Slf4j
@RestController
@RequestMapping("favoriteRemark")
@Api(tags = "旋机-用户收藏备注")
public class MdVibrationAnalysisFavoriteRemarkController {

    @Autowired
    private MdVibrationAnalysisFavoriteRemarkService remarkService;

    @GetMapping("list/{favoriteId}")
    @ApiOperation(value = "收藏夹备注-获取所有的收藏夹备注")
    public Object listFavoriteRemark(@PathVariable Long favoriteId) {
        FavoriteDetailsVO remarks = remarkService.listFavoriteRemark(favoriteId);
        return ResponseUtils.success(remarks);
    }

    @GetMapping("list")
    @ApiOperation(value = "收藏夹备注-根据tagId采集时间等参数获取备注信息")
    public Object listRemarkByFavorite(MdVibrationAnalysisFavorite favorite) {
        FavoriteDetailsVO remarks = remarkService.listRemarkByFavorite(favorite);
        return ResponseUtils.success(remarks);
    }

    @PostMapping
    @ApiOperation(value = "收藏夹备注-添加被收藏夹的备注信息")
    public Object saveMdVibrationAnalysisFavoriteRemark(@RequestBody RemarkBO remark) {
        return ResponseUtils.success(remarkService.saveMdVibrationAnalysisFavoriteRemark(remark));
    }

    @PutMapping
    public Object modifyMdVibrationAnalysisFavoriteRemark(@RequestBody MdVibrationAnalysisFavoriteRemark remark) {
        return ResponseUtils.success(remarkService.updateById(remark));
    }

    @DeleteMapping
    public Object delMdVibrationAnalysisFavoriteRemark(@RequestParam String ids) {
        return ResponseUtils.success(remarkService.removeByIds(Arrays.asList(ids.split(","))));
    }
}
