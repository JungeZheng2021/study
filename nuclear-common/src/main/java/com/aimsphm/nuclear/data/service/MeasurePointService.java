package com.aimsphm.nuclear.data.service;

import com.aimsphm.nuclear.common.constant.CommonConstant;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.data.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/4/16 15:25
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/16 15:25
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface MeasurePointService {
    /**
     * 获取所有的pi测点
     *
     * @return
     */
    Map<String, Object> getMeasurePoints();

    /**
     * 根据 tagId 获取测点信息
     *
     * @param tagId
     * @return (可能存在公共测点所以是list)
     */
    List<MeasurePointVO> getMeasurePointsByTagId(String tagId);

    @Cacheable(value = CommonConstant.CACHE_KEY_PREFIX + CommonConstant.REDIS_NONE_PI_POINT_INFO_LIST, key = "#tagId")
    List<MeasurePointVO> getNonPiMeasurePointsByTagId(String tagId);
}
