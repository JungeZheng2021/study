package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavorite;
import com.aimsphm.nuclear.common.entity.MdVibrationAnalysisFavoriteRemark;
import com.aimsphm.nuclear.common.entity.ModelBase;
import com.aimsphm.nuclear.common.entity.bo.RemarkBO;
import com.aimsphm.nuclear.common.entity.vo.FavoriteDetailsVO;
import com.aimsphm.nuclear.common.mapper.MdVibrationAnalysisFavoriteRemarkMapper;
import com.aimsphm.nuclear.common.service.MdSensorService;
import com.aimsphm.nuclear.common.service.MdVibrationAnalysisFavoriteRemarkService;
import com.aimsphm.nuclear.common.service.MdVibrationAnalysisFavoriteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 振动分析用户备注
 *
 * @author lu.yi
 * @since 2020-08-14
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class MdVibrationAnalysisFavoriteRemarkServiceImpl extends ServiceImpl<MdVibrationAnalysisFavoriteRemarkMapper, MdVibrationAnalysisFavoriteRemark> implements MdVibrationAnalysisFavoriteRemarkService {

    @Autowired
    private MdSensorService sensorService;

    @Autowired
    private MdVibrationAnalysisFavoriteRemarkMapper remarkMapper;

    @Autowired
    private MdVibrationAnalysisFavoriteService favoriteService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object saveMdVibrationAnalysisFavoriteRemark(RemarkBO bo) {
        MdVibrationAnalysisFavoriteRemark remark = new MdVibrationAnalysisFavoriteRemark();
        BeanUtils.copyProperties(bo, remark);
        MdVibrationAnalysisFavorite favorite = new MdVibrationAnalysisFavorite();
        BeanUtils.copyProperties(bo, favorite);
        MdVibrationAnalysisFavorite findFavorite = getVibrationAnalysisFavorite(favorite);
        if (Objects.nonNull(findFavorite)) {
            remark.setFavoriteId(findFavorite.getId());
            return this.save(remark);
        }
        favoriteService.saveMdVibrationAnalysisFavorite(favorite);
        remark.setFavoriteId(favorite.getId());
        return this.save(remark);
    }

    private MdVibrationAnalysisFavorite getVibrationAnalysisFavorite(MdVibrationAnalysisFavorite favorite) {
        LambdaQueryWrapper<MdVibrationAnalysisFavorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MdVibrationAnalysisFavorite::getTagId, favorite.getTagId()).eq(MdVibrationAnalysisFavorite::getAcquisitionTime, favorite.getAcquisitionTime())
                .eq(MdVibrationAnalysisFavorite::getDataType, favorite.getDataType());
        setSelectFields(wrapper);
        return favoriteService.getOne(wrapper);
    }

    private MdVibrationAnalysisFavorite getVibrationAnalysisFavoriteById(Long favoriteId) {
        LambdaQueryWrapper<MdVibrationAnalysisFavorite> favoriteQuery = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<MdVibrationAnalysisFavorite> wrapper = favoriteQuery.eq(MdVibrationAnalysisFavorite::getId, favoriteId);
        setSelectFields(wrapper);
        return favoriteService.getOne(favoriteQuery);
    }

    private void setSelectFields(LambdaQueryWrapper<MdVibrationAnalysisFavorite> wrapper) {
        wrapper.select(ModelBase::getId, MdVibrationAnalysisFavorite::getAcquisitionTime, MdVibrationAnalysisFavorite::getTagId,
                MdVibrationAnalysisFavorite::getDataType, MdVibrationAnalysisFavorite::getDeviceId);
    }

    @Override
    public FavoriteDetailsVO listFavoriteRemark(Long favoriteId) {
        FavoriteDetailsVO vo = new FavoriteDetailsVO();
        MdVibrationAnalysisFavorite favorite = getVibrationAnalysisFavoriteById(favoriteId);
        setFavoriteAlias(favorite);

        List<MdVibrationAnalysisFavoriteRemark> remarks = listRemarks(favoriteId);
        vo.setFavorite(favorite);
        vo.setRemarkList(remarks);
        return vo;

    }

    private List<MdVibrationAnalysisFavoriteRemark> listRemarks(Long favoriteId) {
        LambdaQueryWrapper<MdVibrationAnalysisFavoriteRemark> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MdVibrationAnalysisFavoriteRemark::getFavoriteId, favoriteId);
        return this.list(wrapper);
    }

    /**
     * 设置测点别名
     *
     * @param favorite 收藏夹对象
     */
    private void setFavoriteAlias(MdVibrationAnalysisFavorite favorite) {
        if (Objects.isNull(favorite)) {
            return;
        }
        String tagId = favorite.getTagId();
        LambdaQueryWrapper<MdSensor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MdSensor::getTagId, tagId).select(MdSensor::getAlias, MdSensor::getIswifi);
        MdSensor one = sensorService.getOne(wrapper);
        if (Objects.nonNull(one)) {
            favorite.setAlias(one.getAlias());
            favorite.setWifiFlag(one.getIswifi());
        }
    }

    @Override
    public FavoriteDetailsVO listRemarkByFavorite(MdVibrationAnalysisFavorite favorite) {
        if (Objects.isNull(favorite)) {
            return null;
        }
        if (Objects.nonNull(favorite.getId())) {
            return this.listFavoriteRemark(favorite.getId());
        }

        FavoriteDetailsVO vo = new FavoriteDetailsVO();
        MdVibrationAnalysisFavorite find = getVibrationAnalysisFavorite(favorite);
        if (Objects.isNull(find)) {
            return vo;
        }
        setFavoriteAlias(find);
        List<MdVibrationAnalysisFavoriteRemark> remarks = listRemarks(find.getId());
        vo.setFavorite(find);
        vo.setRemarkList(remarks);
        return vo;
    }
}