package com.aimsphm.nuclear.data.service.impl;

import com.aimsphm.nuclear.common.constant.HBaseConstant;
import com.aimsphm.nuclear.common.entity.BizOriginalDataDO;
import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import com.aimsphm.nuclear.common.service.BizOriginalDataService;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.common.service.CommonSensorService;
import com.aimsphm.nuclear.common.util.ByteUtil;
import com.aimsphm.nuclear.data.entity.dto.PacketDTO;
import com.aimsphm.nuclear.data.entity.dto.SensorDataDTO;
import com.aimsphm.nuclear.data.enums.CalculateFeatureEnum;
import com.aimsphm.nuclear.data.service.CommonDataService;
import com.aimsphm.nuclear.data.service.HBaseService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.*;
import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_WAVE_DATA_ACC;
import static com.aimsphm.nuclear.common.constant.RedisKeyConstant.REDIS_WAVE_DATA_VEC;
import static com.aimsphm.nuclear.common.constant.SymbolConstant.DASH;
import static com.aimsphm.nuclear.data.enums.SensorDataCategoryEnum.SETTINGS_STATUS;
import static com.aimsphm.nuclear.data.enums.SensorDataCategoryEnum.WAVEFORM_DATA;

/**
 * @Package: com.aimsphm.nuclear.common.service.ext.service.impl
 * @Description: <振动数据处理服务>
 * @Author: MILLA
 * @CreateDate: 2020/3/31 11:41
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/31 11:41
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Service("vibration")
public class VibrationDataServiceImpl implements CommonDataService {

    @Resource
    private StringRedisTemplate redis;

    @Resource
    private HBaseService hBaseService;

    @Resource
    private BizOriginalDataService service;

    @Resource
    private CommonMeasurePointService pointServiceExt;

    @Resource
    private CommonSensorService sensorService;
    /**
     * 需要存储到redis中的特征列表
     */
    private static final List<String> store2RedisFeatureList = Lists.newArrayList("vec-Rms", "ana-temperature", "ana-humidity", "ana-PPM", "ana-viscosity", "ana-density", "abr-realTime", "raw-stressWaveStrength");

    static {
        //将所有需要保存的特征值缓存起来
        CalculateFeatureEnum[] values = CalculateFeatureEnum.values();
        List<CalculateFeatureEnum> calculateFeatureEnums = Arrays.asList(values);
        List<String> collect = calculateFeatureEnums.stream().map(m -> m.getValue()).collect(Collectors.toList());
        store2RedisFeatureList.addAll(collect);
    }

    @Override
    public void operateData(String topic, String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        SensorDataDTO sensorDataBO = JSON.parseObject(message, SensorDataDTO.class);
        if (Objects.isNull(sensorDataBO) || Objects.isNull(sensorDataBO.getType()) || Objects.isNull(sensorDataBO.getPacket())) {
            return;
        }
        Integer type = sensorDataBO.getType();
        PacketDTO packet = sensorDataBO.getPacket();
        //传感器设置结果
        if (SETTINGS_STATUS.getType().equals(type)) {
            settingSensorConfigStatus(packet);
            return;
        }
        //波形数据
        if (WAVEFORM_DATA.getType().equals(type)) {
            try {
                updateWaveDate2Redis(packet);
            } catch (Exception e) {
                log.info("wave data save failed...{}", e);
            }
        }
        log.info("topic:{} ,message:{},type:{}", topic, packet.getSensorCode(), sensorDataBO.getType());
        batchUpdateAndSave(packet);

    }

    private void updateWaveDate2Redis(PacketDTO packet) {
        BizOriginalDataDO dataDO = new BizOriginalDataDO();
        BeanUtils.copyProperties(packet, dataDO);
        dataDO.setDataType(PointCategoryEnum.VIBRATION.getValue());
        service.save(dataDO);

        log.info("wave data coming.....................{}", packet.getSensorCode());
        String sensorCode = packet.getSensorCode();
        //加速度
        Double[] accData = packet.getData();
        //速度
        Double[] vecData = packet.getVecData();
        Map<String, Object> accValue = new HashMap<>(16);
        accValue.put("signal", accData);
        accValue.put("fs", packet.getAcqFrequency());

        Map<String, Object> vecValue = new HashMap<>(16);
        vecValue.put("signal", vecData);
        vecValue.put("fs", packet.getAcqFrequency());
        redis.opsForValue().setIfAbsent(String.format(REDIS_WAVE_DATA_VEC, sensorCode), JSON.toJSONString(vecValue));
        redis.opsForValue().setIfAbsent(String.format(REDIS_WAVE_DATA_ACC, sensorCode), JSON.toJSONString(accValue));
    }

    private void settingSensorConfigStatus(PacketDTO packet) {
        if (Objects.isNull(packet) || Objects.isNull(packet.getConfigResult()) || StringUtils.isBlank(packet.getEdgeCode())) {
            return;
        }
        sensorService.updateConfigStatus(packet.getEdgeCode(), packet.getConfigResult());
    }

    private void batchUpdateAndSave(PacketDTO packet) {
        if (Objects.isNull(packet)) {
            return;
        }
        String sensorCode = packet.getSensorCode();
        Long timestamp = packet.getTimestamp();
        //如果没有时间戳或者是tag不存在不处理
        if (StringUtils.isBlank(sensorCode) || Objects.isNull(timestamp)) {
            return;
        }
        String rowKey = sensorCode + ROW_KEY_SEPARATOR + timestamp / (1000 * 3600) * (1000 * 3600);
        Integer index = Math.toIntExact(timestamp / 1000 % 3600);
        boolean isDriveData = operationDeriveData3600Columns(packet, rowKey, index);
//        保存到redis中
        operationRmsData(packet, rowKey, index, timestamp);
        //特征数据和其他数据不同时存在
        if (isDriveData) {
            return;
        }
        operationVibrationVecData(packet, rowKey, index);
        operationVibrationRawData(packet, rowKey, index);

    }

    /**
     * 操作Rms值
     *
     * @param packet    数据包
     * @param rowKey    行键
     * @param index     索引值
     * @param timestamp
     */
    private void operationRmsData(PacketDTO packet, String rowKey, Integer index, Long timestamp) {
        if (Objects.isNull(packet.getVecRms())) {
            return;
        }
        pointServiceExt.updateMeasurePointsInRedis(packet.getSensorCode() + DASH + H_BASE_FAMILY_NPC_SENSOR_RMS, packet.getVecRms(), timestamp);
        insert2HBase(rowKey, index, packet.getTimestamp(), packet.getVecRms(), H_BASE_FAMILY_NPC_SENSOR_RMS);
    }

    /**
     * 操作振动计算数据
     *
     * @param packet 数据包
     * @param rowKey 行键
     * @param index  索引值
     */
    private void operationVibrationVecData(PacketDTO packet, String rowKey, Integer index) {
        //振动计算得到数据
        Double[] vecData = packet.getVecData();
        if (Objects.isNull(vecData) || vecData.length == 0) {
            return;
        }
        insert2HBase(rowKey, index, packet.getTimestamp(), vecData, H_BASE_FAMILY_NPC_VIBRATION_CALCULATE);
    }

    /**
     * 操作振动原始数据
     *
     * @param packet 数据包
     * @param rowKey 行键
     * @param index  索引值
     */
    private void operationVibrationRawData(PacketDTO packet, String rowKey, Integer index) {
        //振动原始数据
        Double[] data = packet.getData();
        if (Objects.isNull(data) || data.length == 0) {
            return;
        }
        insert2HBase(rowKey, index, packet.getTimestamp(), data, H_BASE_FAMILY_NPC_VIBRATION_RAW);
    }

    /**
     * 入库
     *
     * @param rowKey    行键
     * @param index     索引值
     * @param timestamp 时间戳
     * @param data      真实数据
     * @param family    列族
     */
    private void insert2HBase(String rowKey, Integer index, Long timestamp, Object data, String family) {
        Put put = new Put(Bytes.toBytes(rowKey));
        put.setTimestamp(timestamp);
        put.addColumn(Bytes.toBytes(family), Bytes.toBytes(index), ByteUtil.toBytes(data));
        try {
            hBaseService.save2HBase(HBaseConstant.H_BASE_TABLE_NPC_PHM_DATA, put);
        } catch (IOException e) {
            log.error("batch save  hBase failed:{}", e);
        }
    }

    /**
     * 操作特征数据[3600列存储]
     *
     * @param packet 数据包
     * @param rowKey 生成的rowKey
     * @param index  生成的索引(列族)
     */
    private boolean operationDeriveData3600Columns(PacketDTO packet, String rowKey, Integer index) {
        //派生测点数值
        Map<String, Double> features = packet.getFeaturesResult();
        if (MapUtils.isEmpty(features)) {
            return false;
        }
        List<Put> putList = Lists.newArrayList();
        Set<String> featureList = pointServiceExt.listFeatures();
        for (Iterator<Map.Entry<String, Double>> it = features.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Double> next = it.next();
            String feature = next.getKey();
            //需要保证要添加的特征family是存在的
            if (CollectionUtils.isEmpty(featureList) || !featureList.contains(feature)) {
                continue;
            }
            Double value = next.getValue();

            //是否需要存储到redis
            if (store2RedisFeatureList.contains(feature)) {
                String itemId = packet.getSensorCode() + DASH + feature;
                CalculateFeatureEnum calculateFeatureEnum = CalculateFeatureEnum.value(feature);
                //磨砺分析需要额外计算入库
                if (Objects.nonNull(calculateFeatureEnum)) {
                    //判断列族是否存在，如不存在创建该列族--上线后需要拿掉
                    try {
                        hBaseService.familyExists(H_BASE_TABLE_NPC_PHM_DATA, feature + DASH + OIL_FEATURE_CALCULATE_FIX, true, Compression.Algorithm.SNAPPY);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //计算后的数据
                    Double aDouble = pointServiceExt.calculatePointValueFromRedis(itemId, value);
                    Put put = new Put(Bytes.toBytes(rowKey));
                    put.setTimestamp(packet.getTimestamp());
                    put.addColumn(Bytes.toBytes(feature + DASH + OIL_FEATURE_CALCULATE_FIX), Bytes.toBytes(index), Bytes.toBytes(aDouble));
                    putList.add(put);
                }
                //
                pointServiceExt.updateMeasurePointsInRedis(itemId, value, packet.getTimestamp());
            }
            //判断列族是否存在，如不存在创建该列族--上线后需要拿掉
            try {
                hBaseService.familyExists(H_BASE_TABLE_NPC_PHM_DATA, feature, true, Compression.Algorithm.SNAPPY);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Put put = new Put(Bytes.toBytes(rowKey));
            put.setTimestamp(packet.getTimestamp());
            put.addColumn(Bytes.toBytes(feature), Bytes.toBytes(index), Bytes.toBytes(value));
            putList.add(put);
        }
        try {
            hBaseService.batchSave2HBase(H_BASE_TABLE_NPC_PHM_DATA, putList);
        } catch (IOException e) {
            log.error("batch save 2 hBase failed:{}", e);
        } finally {
            return true;
        }
    }
}
