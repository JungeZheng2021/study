package com.aimsphm.nuclear.history;

import com.aimsphm.nuclear.common.constant.HBaseConstant;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_FAMILY_NPC_PI_REAL_TIME;
import static com.aimsphm.nuclear.common.constant.HBaseConstant.ROW_KEY_SEPARATOR;

/**
 * @Package: com.aimsphm.nuclear.history
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/11/23 13:24
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/23 13:24
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class HBaseTests {

    static Connection connection = null;

    static {
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        //消除sasl认证
        System.setProperty("zookeeper.sasl.client", "false");
        conf.set("hbase.client.ipc.pool.size", "10");
        conf.set("hbase.hconnection.threads.max", "30");
        conf.set("hbase.zookeeper.quorum", "hadoop-master:2181,hadoop-slave01:2181,hadoop-slave02:2181");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 30, 2000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100));
        try {
            connection = ConnectionFactory.createConnection(conf, executor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HBaseUtil hbaseUtil = new HBaseUtil(connection);
        Long start = 1600006002123L;
        Long end = System.currentTimeMillis();
        String tag = "6M2DVC003MI";
        long l = System.currentTimeMillis();
        List<HBaseTimeSeriesDataDTO> dataDTOS = listByGetList(hbaseUtil, start, end, tag);
        long l1 = System.currentTimeMillis();
        log.info("listByGetList: 共计耗时：{} 毫秒，数据量为：{}", (l1 - l), dataDTOS.size());
        List<HBaseTimeSeriesDataDTO> dtos = listByScan(hbaseUtil, start, end, tag);
        long l2 = System.currentTimeMillis();
        log.info("listByScan...: 共计耗时：{} 毫秒，数据量为：{}", (l2 - l1), dtos.size());
    }

    private static List<HBaseTimeSeriesDataDTO> listByScan(HBaseUtil hbaseUtil, Long start, Long end, String tag) throws IOException {
        String tableName = "npc_phm_data";
        return hbaseUtil.listObjectDataWith3600Columns(tableName, tag, start, end, H_BASE_FAMILY_NPC_PI_REAL_TIME);

    }

    private static List<HBaseTimeSeriesDataDTO> listByGetList(HBaseUtil hbaseUtil, Long start, Long end, String tag) throws IOException {
        List<Get> getList = getList(tag, start, end);
        List<HBaseTimeSeriesDataDTO> data = Lists.newArrayList();
        String tableName = "npc_phm_data";
        TableName name = TableName.valueOf(tableName);
        Table table = connection.getTable(name);
        Result[] results = table.get(getList);
        for (Result rs : results) {
            List<HBaseTimeSeriesDataDTO> items = new ArrayList();
            List<Cell> cells = rs.listCells();
            if (CollectionUtils.isEmpty(cells)) {
                continue;
            }
            for (Cell cell : cells) {
                if (Objects.isNull(cell)) {
                    continue;
                }
                double value = Bytes.toDouble(CellUtil.cloneValue(cell));
                Long timestamp = cell.getTimestamp();
                HBaseTimeSeriesDataDTO dto = new HBaseTimeSeriesDataDTO();
                dto.setTimestamp(timestamp);
                dto.setValue(value);
                items.add(dto);
            }
            data.addAll(items);
        }
        return data;
    }

    private static List<Get> getList(String tag, Long start, Long end) {
        Long startRow = start / (1000 * 3600) * (1000 * 3600);
        Long endRow = end / (1000 * 3600) * (1000 * 3600);
        List<Get> getList = Lists.newArrayList();
        for (Long i = start; i < startRow + 3600 * 1000; i = i + 1000) {
            Get get = new Get(Bytes.toBytes(tag + ROW_KEY_SEPARATOR + startRow));
            Integer index = Math.toIntExact(i / 1000 % 3600);
            get.addColumn(Bytes.toBytes(H_BASE_FAMILY_NPC_PI_REAL_TIME), Bytes.toBytes(index));
            getList.add(get);
        }
        for (Long i = startRow + 3600 * 1000; i < endRow; i = i + 1000 * 3600) {
            Get get = new Get(Bytes.toBytes(tag + ROW_KEY_SEPARATOR + i));
            get.addFamily(Bytes.toBytes(H_BASE_FAMILY_NPC_PI_REAL_TIME));
            getList.add(get);
        }
        for (Long i = endRow; i < end; i = i + 1000) {
            Get get = new Get(Bytes.toBytes(tag + ROW_KEY_SEPARATOR + startRow));
            Integer index = Math.toIntExact(i / 1000 % 3600);
            get.addColumn(Bytes.toBytes(H_BASE_FAMILY_NPC_PI_REAL_TIME), Bytes.toBytes(index));
            getList.add(get);
        }
        return getList;
    }
}
