package com.aimsphm.nuclear.data.service.impl;

import com.aimsphm.nuclear.common.constant.HBaseConstant;
import com.aimsphm.nuclear.common.entity.dto.HBaseColumnItemDTO;
import com.aimsphm.nuclear.common.service.CommonMeasurePointService;
import com.aimsphm.nuclear.data.feign.entity.dto.DataItemDTO;
import com.aimsphm.nuclear.data.service.CommonDataService;
import com.aimsphm.nuclear.data.service.HBaseService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.ROW_KEY_SEPARATOR;

/**
 * <p>
 * 功能描述:pi数据
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/3/31 11:41
 */
@Service("pi")
@Slf4j
public class PIDataServiceImpl implements CommonDataService {
    @Resource
    private CommonMeasurePointService pointServiceExt;
    @Resource
    private HBaseService hBaseService;

    @Override
    public void operateData(String topic, String message) {
        try {
            batchUpdateAndSave(topic, message);
            Thread.sleep(10);
        } catch (InterruptedException | IOException e) {
            log.error("get failed:{}", e);
            Thread.currentThread().interrupt();
        }
    }

    private void batchUpdateAndSave(String topic, String message) throws IOException {
        log.debug("topic:{}", topic);
        List<DataItemDTO> dataItems = JSON.parseArray(message, DataItemDTO.class);
        if (CollectionUtils.isEmpty(dataItems)) {
            return;
        }
        List<Put> putList = Lists.newArrayList();
        for (DataItemDTO dataItem : dataItems) {
            HBaseColumnItemDTO item = new HBaseColumnItemDTO();
            item.setPointId(dataItem.getItemId());
            item.setTimestamp(dataItem.getTimestamp());
            item.setTableName(HBaseConstant.H_BASE_TABLE_NPC_PHM_DATA);
            item.setFamily(HBaseConstant.H_BASE_FAMILY_NPC_PI_REAL_TIME);
            item.setTimestamp(dataItem.getTimestamp());
            BigDecimal value = (BigDecimal) dataItem.getValue();
            double v = value.doubleValue();
            item.setValue(v);
            pointServiceExt.updateMeasurePointsInRedis(dataItem.getItemId(), v, dataItem.getTimestamp());

            Long timestamp = item.getTimestamp();
            //3600列的索引
            Integer index = Math.toIntExact(timestamp / 1000 % 3600);
            item.setQualifier(index);
            String rowKey = item.getPointId() + ROW_KEY_SEPARATOR + item.getTimestamp() / (1000 * 3600) * (1000 * 3600);
            Put put = new Put(Bytes.toBytes(rowKey));
            if (item.getTimestamp() != null) {
                put.setTimestamp(item.getTimestamp());
            }
            put.addColumn(Bytes.toBytes(item.getFamily()), Bytes.toBytes((Integer) item.getQualifier()), Bytes.toBytes(item.getValue()));
            putList.add(put);
        }
        hBaseService.batchSave2HBase(HBaseConstant.H_BASE_TABLE_NPC_PHM_DATA, putList);
    }


}
