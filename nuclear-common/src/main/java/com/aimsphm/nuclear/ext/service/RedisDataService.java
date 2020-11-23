package com.aimsphm.nuclear.ext.service;

import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;

import java.util.List;
import java.util.Set;

/**
 * @Package: com.aimsphm.nuclear.ext.service
 * @Description: <redis数据库操作类>
 * @Author: MILLA
 * @CreateDate: 2020/11/18 17:22
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/18 17:22
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface RedisDataService {

    /**
     * 根据key获取测点的缓存数据
     *
     * @param tagList 测点列表
     * @return
     */
    List<MeasurePointVO> listPointByRedisKey(Set<String> tagList);
}
