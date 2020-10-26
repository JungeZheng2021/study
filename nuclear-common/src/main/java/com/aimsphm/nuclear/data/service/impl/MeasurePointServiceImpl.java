package com.aimsphm.nuclear.data.service.impl;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.data.mapper.MeasurePointVOMapper;
import com.aimsphm.nuclear.data.service.MeasurePointService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Package: com.aimsphm.nuclear.data.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/16 15:25
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/16 15:25
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class MeasurePointServiceImpl implements MeasurePointService {
    @Autowired
    private MeasurePointVOMapper mapper;

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_POINT_INFO_LIST)
    public Map<String, Object> getMeasurePoints() {
        //1：PI测点
        List<MeasurePointVO> points = mapper.selectMeasurePoints(1);
        if (CollectionUtils.isEmpty(points)) {
            return null;
        }
        return points.stream().collect(Collectors.toMap(MeasurePointVO::getTag, point -> point));
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_POINT_INFO_LIST, key = "#tagId")
    public List<MeasurePointVO> getMeasurePointsByTagId(String tagId) {
        //1：PI测点
        return mapper.selectMeasurePointsByTagId(1, tagId);
    }

    @Override
    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_NONE_PI_POINT_INFO_LIST, key = "#tagId")
    public List<MeasurePointVO> getNonPiMeasurePointsByTagId(String tagId) {
        //1：PI测点
        return mapper.selectMeasurePointsByTagId(0, tagId);
    }
}
