package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.bo.*;
import com.aimsphm.nuclear.algorithm.service.AlgorithmAsyncService;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.*;
import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_QUEUE_REAL_TIME_PRE;

/**
 * @Package: com.aimsphm.nuclear.algorithm.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/23 16:16
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/23 16:16
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service
public class AlgorithmAsyncServiceImpl implements AlgorithmAsyncService {

    @Resource
    private HBaseUtil hBase;
    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redis;

    @Async
    @Override
    public void listPointDataFromHBase(String family, Long id, String sensorCode, PointDataBO data, CountDownLatch countDownLatch) {
        log.info("请求数据.family-{}  .sensorCode-{}", family, sensorCode);
        long start = System.currentTimeMillis();
        try {
            String key = REDIS_QUEUE_REAL_TIME_PRE + id;
            List range = redis.opsForList().range(key, 0, -1);
            if (!CollectionUtils.isEmpty(range)) {
                data.setCells(range);
                return;
            }
            List<HBaseTimeSeriesDataDTO> cells = hBase.listDataWithLimit(H_BASE_TABLE_NPC_PHM_DATA, family, sensorCode, 1, 60, 60);
            log.info("请求数据返回值-.family-{}  .sensorCode-{} 返回值数据量 {}  耗时-- {} 毫秒", family, sensorCode, cells.size(), (System.currentTimeMillis() - start));
            data.setCells(cells);
            cells.stream().forEach(item -> {
                redis.opsForList().rightPush(key, item);
            });
        } catch (Exception e) {
            data.setCells(Lists.newArrayList());
            log.error("get point data failed ...family：{},pre:{}, --{}", family, sensorCode, e);
        } finally {
            countDownLatch.countDown();
        }
    }
}
