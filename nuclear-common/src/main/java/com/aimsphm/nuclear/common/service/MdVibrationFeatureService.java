package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.MdVibrationFeature;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-08-14
 */
public interface MdVibrationFeatureService extends IService<MdVibrationFeature> {

    List<MdVibrationFeature> getVibrationFeature(String featureType, String desCode);
}