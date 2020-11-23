package com.aimsphm.nuclear.ext.service.impl;

import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.aimsphm.nuclear.ext.service.RedisDataService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Package: com.aimsphm.nuclear.ext.service.impl
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/11/18 17:22
 * @UpdateUser: milla
 * @UpdateDate: 2020/11/18 17:22
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
@ConditionalOnProperty(prefix = "spring.config", name = "enableRedis", havingValue = "true")
public class RedisDataServiceImpl implements RedisDataService {
    @Autowired
    @Qualifier(value = "redisTemplate")
    private RedisTemplate<String, Object> redis;

    @Override
    public List<MeasurePointVO> listPointByRedisKey(Set<String> tagList) {
        return (List<MeasurePointVO>) multiGetByKeyList(tagList);
    }

    private Object multiGetByKeyList(Collection<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return null;
        }
        return redis.opsForValue().multiGet(tags);
    }
}
