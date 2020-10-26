package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavorite;
import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavoriteRemark;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.mapper.MdVibrationAnalysisFavoriteMapper;
import com.aimsphm.nuclear.common.mapper.MdVibrationAnalysisFavoriteRemarkMapper;
import com.aimsphm.nuclear.common.service.MdVibrationAnalysisFavoriteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 振动分析收藏夹
 *
 * @author lu.yi
 * @since 2020-08-14
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class MdVibrationAnalysisFavoriteServiceImpl extends ServiceImpl<MdVibrationAnalysisFavoriteMapper, MdVibrationAnalysisFavorite> implements MdVibrationAnalysisFavoriteService {

    @Autowired
    private MdVibrationAnalysisFavoriteMapper favoriteMapper;
    @Autowired
    private MdVibrationAnalysisFavoriteRemarkMapper remarkMapper;

    @Override
    public Page<MdVibrationAnalysisFavorite> listAnalysisFavorite(Page<MdVibrationAnalysisFavorite> page, MdVibrationAnalysisFavorite query, TimeRangeQueryBO timeBo) {
        List<MdVibrationAnalysisFavorite> list = favoriteMapper.listAnalysisFavorite(page, query, timeBo);
        return page.setRecords(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer removeFavorite(Long id) {
        LambdaQueryWrapper<MdVibrationAnalysisFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MdVibrationAnalysisFavorite::getId, id).select(MdVibrationAnalysisFavorite::getId);
        MdVibrationAnalysisFavorite favorite = this.getOne(queryWrapper);
        if (Objects.isNull(favorite)) {
            throw new CustomMessageException("this element is not exist");
        }
        //排除sql中自动生成的字段
        favorite.setHasRemark(null);
        this.remove(queryWrapper);
        QueryWrapper<MdVibrationAnalysisFavoriteRemark> wrapper = new QueryWrapper();
        wrapper.lambda().eq(MdVibrationAnalysisFavoriteRemark::getFavoriteId, id);
        int delete = remarkMapper.delete(wrapper);
        return delete;
    }

    @Override
    public Boolean saveMdVibrationAnalysisFavorite(MdVibrationAnalysisFavorite favorite) {
        if (!StringUtils.hasText(favorite.getTagId()) || Objects.isNull(favorite.getAcquisitionTime()) || !StringUtils.hasText(favorite.getDataType())) {
            throw new CustomMessageException("入参不完整");
        }
        LambdaQueryWrapper<MdVibrationAnalysisFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MdVibrationAnalysisFavorite::getTagId, favorite.getTagId()).eq(MdVibrationAnalysisFavorite::getAcquisitionTime, favorite.getAcquisitionTime())
                .eq(MdVibrationAnalysisFavorite::getDataType, favorite.getDataType()).select(MdVibrationAnalysisFavorite::getId);
        List<MdVibrationAnalysisFavorite> list = this.list(wrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new CustomMessageException("收藏夹中已存在该记录");
        }
        //指定的字段不需要插入
        favorite.setHasRemark(null);
        return this.save(favorite);
    }
}