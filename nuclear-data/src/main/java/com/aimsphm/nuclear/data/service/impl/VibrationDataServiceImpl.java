package com.aimsphm.nuclear.data.service.impl;

import com.aimsphm.nuclear.common.constant.HBaseConstant;
import com.aimsphm.nuclear.common.util.ByteUtil;
import com.aimsphm.nuclear.data.entity.dto.PacketDTO;
import com.aimsphm.nuclear.data.entity.dto.SensorDataDTO;
import com.aimsphm.nuclear.data.enums.SensorDataCategoryEnum;
import com.aimsphm.nuclear.data.service.CommonDataService;
import com.aimsphm.nuclear.data.service.HBaseService;
import com.aimsphm.nuclear.data.service.HotSpotDataUpdateService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.*;
import static com.aimsphm.nuclear.data.service.impl.PIDataServiceImpl.ROW_KEY_SEPARATOR;

/**
 * @Package: com.aimsphm.nuclear.data.service.impl
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
    @Autowired
    private HBaseService hBaseService;
    @Autowired
    private HotSpotDataUpdateService serviceUpdate;

    @Override
    public void operateData(String topic, String message) {
        log.info("topic:{} ,message:{}", topic, message);
        if (StringUtils.isBlank(message)) {
            return;
        }
        SensorDataDTO sensorDataBO = JSON.parseObject(message, SensorDataDTO.class);
        if (Objects.isNull(sensorDataBO)) {
            return;
        }
        Integer type = sensorDataBO.getType();
        PacketDTO packet = sensorDataBO.getPacket();
        //如果是油质数据，需要按照3600列数据处理
        boolean secondData = SensorDataCategoryEnum.OIL_NORMAL_DATA.getType().equals(type);
        batchUpdateAndSave(packet, secondData);
    }

    private void batchUpdateAndSave(PacketDTO packet, boolean secondData) {
        if (Objects.isNull(packet)) {
            return;
        }
        //tagId
        String sensorCode = packet.getSensorCode();
        Long timestamp = packet.getTimestamp();
        //如果没有时间戳或者是tag不存在不处理
        if (StringUtils.isBlank(sensorCode) || Objects.isNull(timestamp)) {
            return;
        }
        String rowKey = sensorCode + ROW_KEY_SEPARATOR + timestamp / (1000 * 3600) * (1000 * 3600);

        Integer index = Math.toIntExact(timestamp / 1000 % 3600);
        boolean isDriveData = operationDeriveData3600Columns(packet, rowKey, index);
        //保存到redis中
        operationRmsData(packet, rowKey, index);
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
     * @param packet 数据包
     * @param rowKey 行键
     * @param index  索引值
     */
    private void operationRmsData(PacketDTO packet, String rowKey, Integer index) {
        serviceUpdate.updateNonePIMeasurePoints(packet.getSensorCode(), packet.getRms(), null, null);
        insert2HBase(rowKey, index, packet.getTimestamp(), packet.getRms(), H_BASE_FAMILY_NPC_SENSOR_RMS);
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
        insert2HBase(rowKey, index, packet.getTimestamp(), vecData, H_BASE_FAMILY_NPC_VIBRATION_RAW);
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
        insert2HBase(rowKey, index, packet.getTimestamp(), data, H_BASE_FAMILY_NPC_VIBRATION_CALCULATE);
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
            hBaseService.save2HBase(HBaseConstant.H_BASE_TABLE_NPC_REAL_TIME, put);
        } catch (IOException e) {
            log.error("batch save  hbase failed:{}", e);
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

        for (Iterator<Map.Entry<String, Double>> it = features.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Double> next = it.next();
            String feature = next.getKey();
            List<String> featureList = listFeatures();
            //需要保证要添加的特征family是存在的
            if (CollectionUtils.isEmpty(featureList) || !featureList.contains(feature)) {
                continue;
            }
            Double value = next.getValue();
            Put put = new Put(Bytes.toBytes(rowKey));
            put.setTimestamp(packet.getTimestamp());
            put.addColumn(Bytes.toBytes(feature), Bytes.toBytes(index), Bytes.toBytes(value));
            putList.add(put);
        }
        try {
            hBaseService.batchSave2HBase(HBaseConstant.H_BASE_TABLE_NPC_REAL_TIME, putList);
        } catch (IOException e) {
            log.error("batch save 2 hbase failed:{}", e);
        } finally {
            return true;
        }
    }

    /**
     * 获取全量的特征，避免边缘端随便传一个特征也能存储
     *
     * @return
     */
    private List<String> listFeatures() {
        return null;
    }
}
