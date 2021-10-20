package com.aimsphm.nuclear.algorithm.service.impl;

import com.aimsphm.nuclear.algorithm.entity.bo.PointDataBO;
import com.aimsphm.nuclear.algorithm.service.AlgorithmAsyncService;
import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.CommonSensorSettingsDO;
import com.aimsphm.nuclear.common.entity.bo.TimeRangeQueryBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import com.aimsphm.nuclear.common.enums.PointTypeEnum;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.CommonSensorService;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.*;
import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_QUEUE_REAL_TIME_PRE;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.DASH;

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
    private CommonSensorService sensorService;
    @Resource
    private CommonMeasurePointService pointService;

    @Value("#{${customer.config.sleepTime:2}*60*1000}")
    private Long sleepTime;
    @Value("#{${customer.config.rangeTime:4}*60*1000}")
    private Long rangeTime;


    @Resource
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redis;
    /**
     * 点的数量
     */
    private static final Integer POINT_NUMBER = 60;

    @Async
    @Override
    public void listPointDataFromHBase(String family, Long id, String sensorCode, PointDataBO data, CountDownLatch countDownLatch) {
        log.debug("请求数据.family-{}  .sensorCode-{}", family, sensorCode);
        long start = System.currentTimeMillis();
        try {
            String key = REDIS_QUEUE_REAL_TIME_PRE + id;
            List range = redis.opsForList().range(key, 0, -1);
            if (!CollectionUtils.isEmpty(range)) {
                data.setCells(range);
                return;
            }
            List<HBaseTimeSeriesDataDTO> cells = hBase.listDataWithLimit(H_BASE_TABLE_NPC_PHM_DATA, family, sensorCode, 1, 60, 60);
            log.debug("请求数据返回值-.family-{}  .sensorCode-{} 返回值数据量 {}  耗时-- {} 毫秒", family, sensorCode, cells.size(), (System.currentTimeMillis() - start));
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

    @Async
    @Override
    public void faultDiagnosis(List<String> pointIdList) {
    }

    @Async
    @Override
    public void listPointDataFromHBase(CommonMeasurePointDO item, PointDataBO data, CountDownLatch countDownLatch) {
        String family = PointTypeEnum.PI.getValue().equals(item.getPointType()) ? H_BASE_FAMILY_NPC_PI_REAL_TIME : item.getFeatureType() + DASH + item.getFeature();
        Long id = item.getId();
        String sensorCode = item.getSensorCode();
        TimeRangeQueryBO rangeQuery = getRangeDate(item, sensorCode);
        log.debug("请求数据.family-{}  .sensorCode-{}", family, sensorCode);
        long start = System.currentTimeMillis();
        try {
            String key = REDIS_QUEUE_REAL_TIME_PRE + id;
            List range = redis.opsForList().range(key, 0, -1);
            if (!CollectionUtils.isEmpty(range)) {
                data.setCells(range);
                return;
            }
            log.debug("请求数据返回值-.请求数据：{}", rangeQuery);
            List<HBaseTimeSeriesDataDTO> cells = hBase.listObjectDataWith3600Columns(H_BASE_TABLE_NPC_PHM_DATA, sensorCode, rangeQuery.getStart(), rangeQuery.getEnd(), family);
            log.debug("请求数据返回值-.family-{}  .sensorCode-{} 返回值数据量 {}  耗时-- {} 毫秒", family, sensorCode, cells.size(), (System.currentTimeMillis() - start));
            data.setCells(cells);
            cells.stream().forEach(x -> {
                redis.opsForList().rightPush(key, x);
            });
        } catch (Exception e) {
            data.setCells(Lists.newArrayList());
            log.error("get point data failed ...family：{},pre:{}, --{}", family, sensorCode, e);
        } finally {
            countDownLatch.countDown();
        }
    }

    @Async
    @Override
    public void deleteData(Long deviceId, Long end) {
        try {
            if (Objects.isNull(end)) {
                Thread.sleep(sleepTime);
                end = System.currentTimeMillis();
            }
            LambdaQueryWrapper<CommonMeasurePointDO> wrapper = Wrappers.lambdaQuery(CommonMeasurePointDO.class);
            wrapper.eq(CommonMeasurePointDO::getDeviceId, deviceId);
            wrapper.eq(CommonMeasurePointDO::getFeatureType, "vec");
            wrapper.eq(CommonMeasurePointDO::getFeature, "Rms");
            List<CommonMeasurePointDO> list = pointService.list(wrapper);
            Long start = end - rangeTime;
            if (CollectionUtils.isEmpty(list)) {
                return;
            }
            Long finalEnd = end;
            list.stream().forEach(item -> {
                String family = PointTypeEnum.PI.getValue().equals(item.getPointType()) ? H_BASE_FAMILY_NPC_PI_REAL_TIME : item.getFeatureType() + DASH + item.getFeature();
                try {
                    List<HBaseTimeSeriesDataDTO> dtoList = hBase.listObjectDataWith3600Columns(H_BASE_TABLE_NPC_PHM_DATA, item.getSensorCode(), start, finalEnd, family);
                    if (CollectionUtils.isEmpty(dtoList)) {
                        return;
                    }
                    double average = dtoList.stream().mapToDouble(x -> x.getValue()).summaryStatistics().getAverage();
                    List<Delete> collect = dtoList.stream().filter(x -> (x.getValue() > average)).map(x -> {
                        Long timestamp = x.getTimestamp();
                        Long rowKey = hBase.rowKeyOf3600(timestamp);
                        Integer index = hBase.indexOf3600(timestamp);
                        Delete delete = new Delete(Bytes.toBytes(item.getSensorCode() + ROW_KEY_SEPARATOR + rowKey));
                        delete.addColumn(Bytes.toBytes(family), Bytes.toBytes(index));
                        return delete;
                    }).collect(Collectors.toList());
                    hBase.deleteBatch(H_BASE_TABLE_NPC_PHM_DATA, collect);
                } catch (IOException e) {
                    log.error("查询错误：", e);
                }
            });

        } catch (InterruptedException e) {
            log.error("打补丁异常：删除记录异常：{}", deviceId);
        }
    }

    private TimeRangeQueryBO getRangeDate(CommonMeasurePointDO item, String sensorCode) {
        CommonSensorSettingsDO config = sensorService.getSensorConfigBySensorCode(sensorCode, PointCategoryEnum.VIBRATION.getValue());
        //秒级
        Long period = Objects.isNull(config) || Objects.isNull(config.getEigenvalueSamplePeriod()) ? 10 * 60 * 1000 : config.getEigenvalueSamplePeriod() * 1000;
        TimeRangeQueryBO bo = new TimeRangeQueryBO();
        long end = System.currentTimeMillis();
        bo.setEnd(end);
        //PI点
        if (PointTypeEnum.PI.getValue().equals(item.getPointType())) {
            bo.setStart(end - POINT_NUMBER * 1000);
        } else if (H_BASE_FAMILY_NPC_SENSOR_RMS.equals(item.getFeatureType().concat(DASH).concat(item.getFeature()))) {
            //vec-Rms值
            bo.setStart(end - POINT_NUMBER * 6 * 1000);
        } else if (H_BASE_FAMILY_NPC_SENSOR_ANA.equals(item.getFeatureType()) || H_BASE_FAMILY_NPC_SENSOR_ABR.equals(item.getFeatureType())) {
            //油液
            bo.setStart(end - POINT_NUMBER * 6 * 1000);
        } else {
            bo.setStart(end - POINT_NUMBER * period);
        }
        return bo;
    }
}
