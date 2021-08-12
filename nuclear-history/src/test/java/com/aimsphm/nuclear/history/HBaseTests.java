package com.aimsphm.nuclear.history;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.AlgorithmPrognosticFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.enums.TimeUnitEnum;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.*;
import static com.aimsphm.nuclear.common.constant.ReportConstant.BLANK;

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
    static HBaseUtil hbaseUtil = null;
    static Table table = null;

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
            hbaseUtil = new HBaseUtil(connection);
            table = connection.getTable(TableName.valueOf(H_BASE_TABLE_NPC_PHM_DATA));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delete(String tableName, String tag, Long startTime, Long endTime, String family) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes(family));
            Long startRow = startTime / (1000 * 3600) * (1000 * 3600);
            Long endRow = endTime / (1000 * 3600) * (1000 * 3600) + 1;
            scan.withStartRow(Bytes.toBytes(tag + ROW_KEY_SEPARATOR + startRow));
            scan.withStopRow(Bytes.toBytes(tag + ROW_KEY_SEPARATOR + endRow));
            ResultScanner scanner = table.getScanner(scan);
            for (Result rs : scanner) {
                Delete delete = new Delete(rs.getRow());
                table.delete(delete);
                System.out.println("成功");
            }
        } catch (IOException e) {
            throw e;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {


//        Long start = 1600006002123L;
//        Long end = System.currentTimeMillis();
//        String tag = "4";
////        long l = System.currentTimeMillis();
////        List<HBaseTimeSeriesDataDTO> dataDTOS = listByGetList(hbaseUtil, start, end, tag);
////        long l1 = System.currentTimeMillis();
////        log.info("listByGetList: 共计耗时：{} 毫秒，数据量为：{}", (l1 - l), dataDTOS.size());
//        List<HBaseTimeSeriesDataDTO> dtos = listByScan(hbaseUtil, start, end, tag);
//        System.out.println(dtos.size());
//        long l2 = System.currentTimeMillis();
////        log.info("listByScan...: 共计耗时：{} 毫秒，数据量为：{}", (l2 - l1), dtos.size());
//        delete(H_BASE_TABLE_NPC_PHM_DATA, tag, start, end, H_BASE_FAMILY_NPC_ESTIMATE);
        long l = System.currentTimeMillis();
        test();
        System.out.println(System.currentTimeMillis() - l);
        table.close();
        connection.close();

    }

    private static void test() throws IOException {
        String s = "[{\"componentId\":5,\"deleted\":false,\"deviceType\":0,\"featureHi\":10.0,\"featureName\":\"主电机驱动端轴承振动-N_峰值\",\"featureType\":5,\"gmtModified\":1627006157000,\"id\":1,\"sensorCode\":\"5M2RCV246MV-N\",\"sensorDesc\":\"5M2RCV246MV-N-acc-ZeroPeak\",\"timeGap\":\"30m\",\"timeRange\":\"7d\"}" +
                "   ,{\"componentId\":5,\"deleted\":false,\"deviceType\":0,\"featureHi\":10.0,\"featureName\":\"主电机驱动端轴承振动-N_通频值\",\"featureType\":5,\"gmtModified\":1627006439000,\"id\":2,\"sensorCode\":\"5M2RCV246MV-N\",\"sensorDesc\":\"5M2RCV246MV-N-vec-Rms\",\"timeGap\":\"1h\",\"timeRange\":\"7d\"}" +
                "   ,{\"componentId\":5,\"deleted\":false,\"deviceType\":0,\"featureHi\":0.5,\"featureName\":\"主电机驱动端轴承振动-N_转频1X\",\"featureType\":1,\"id\":3,\"sensorCode\":\"5M2RCV246MV-N\",\"sensorDesc\":\"5M2RCV246MV-N-vec-1Xrf\",\"timeGap\":\"1h\",\"timeRange\":\"7d\"}" +
                "   ,{\"componentId\":5,\"deleted\":false,\"deviceType\":0,\"featureHi\":0.5,\"featureName\":\"主电机驱动端轴承振动-N_转频2X\",\"featureType\":1,\"id\":4,\"sensorCode\":\"5M2RCV246MV-N\",\"sensorDesc\":\"5M2RCV246MV-N-vec-2Xrf\",\"timeGap\":\"1h\",\"timeRange\":\"7d\"}" +
                "   ,{\"componentId\":5,\"deleted\":false,\"deviceType\":0,\"featureHi\":0.5,\"featureName\":\"主电机驱动端轴承振动-N_转频3X\",\"featureType\":1,\"id\":5,\"sensorCode\":\"5M2RCV246MV-N\",\"sensorDesc\":\"5M2RCV246MV-N-vec-3Xrf\",\"timeGap\":\"1h\",\"timeRange\":\"7d\"}" +
                "   ,{\"componentId\":5,\"deleted\":false,\"deviceType\":0,\"featureHi\":70.0,\"featureName\":\"主电机驱动端轴承温度_主电机驱动端轴承温度\",\"featureType\":5,\"id\":7,\"sensorCode\":\"TW1RCV200MT\",\"sensorDesc\":\"TW1RCV200MT\",\"timeGap\":\"30m\",\"timeRange\":\"7d\"}" +
                "   ,{\"componentId\":5,\"deleted\":false,\"deviceType\":0,\"featureHi\":100.0,\"featureName\":\"主电机驱动端轴承声强-N_声波强度\",\"featureType\":5,\"id\":10,\"sensorCode\":\"5M2RCV246MS-N\",\"sensorDesc\":\"5M2RCV246MS-N-raw-stressWaveStrength\",\"timeGap\":\"1h\",\"timeRange\":\"7d\"}" +
                "   ,{\"componentId\":5,\"deleted\":false,\"deviceType\":0,\"featureHi\":70.0,\"featureName\":\"主电机驱动端轴承温度_主电机驱动端轴承温度\",\"featureType\":5,\"id\":17,\"sensorCode\":\"TW1RCV200MT\",\"sensorDesc\":\"TW1RCV200MT\",\"timeGap\":\"1h\",\"timeRange\":\"7d\"}" +
                "   ,{\"componentId\":5,\"deleted\":false,\"deviceType\":0,\"featureHi\":10.0,\"featureName\":\"主电机驱动端轴承振动-N_峰值\",\"featureType\":5,\"id\":18,\"sensorCode\":\"5M2RCV246MV-N\",\"sensorDesc\":\"5M2RCV246MV-N-acc-ZeroPeak\",\"timeGap\":\"1h\",\"timeRange\":\"7d\"}" +
                "   ,{\"componentId\":5,\"deleted\":false,\"deviceType\":0,\"featureHi\":10.0,\"featureName\":\"主电机驱动端轴承振动-N_通频值\",\"featureType\":5,\"id\":19,\"sensorCode\":\"5M2RCV246MV-N\",\"sensorDesc\":\"5M2RCV246MV-N-vec-Rms\",\"timeGap\":\"1h\",\"timeRange\":\"7d\"}]";
        List<AlgorithmPrognosticFaultFeatureDO> value = JSON.parseArray(s, AlgorithmPrognosticFaultFeatureDO.class);
        long endTime = System.currentTimeMillis();
        Set<String> distinctPointId = new HashSet<>();
        List<Get> gets = new ArrayList<>();
        List<AlgorithmNormalFaultFeatureDO> collect = value.stream().map(x -> {
            //去除重复的测点
            if (distinctPointId.contains(x.getSensorDesc())) {
                return null;
            }
            distinctPointId.add(x.getSensorDesc());
            AlgorithmNormalFaultFeatureDO featureDO = new AlgorithmNormalFaultFeatureDO();
            BeanUtils.copyProperties(x, featureDO);
            try {
                calculateGets(x, gets, endTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return featureDO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        boolean[] exists = table.exists(gets);
        System.out.println(Arrays.toString(exists));
        Map<String, List<List<Object>>> map = hbaseUtil.selectByGets(H_BASE_TABLE_NPC_PHM_DATA, gets);
        System.out.println(map);
    }

    private static void calculateGets(AlgorithmPrognosticFaultFeatureDO x, List<Get> gets, Long endTime) throws IOException, InterruptedException {
        if (Objects.isNull(x) || Objects.isNull(x.getTimeRange()) || Objects.isNull(x.getTimeGap())) {
            return;
        }
        String timeRange = x.getTimeRange();
        Long gapValue = TimeUnitEnum.getGapValue(timeRange);
        if (Objects.isNull(gapValue)) {
            return;
        }
        String timeGap = x.getTimeGap();
        Long timeGapValue = TimeUnitEnum.getGapValue(timeGap);
        if (Objects.isNull(timeGapValue)) {
            return;
        }
        String pointId = x.getSensorDesc();
        String sensorCode = x.getSensorCode();
        if (!"5M2RCV246MV-N-vec-1Xrf".equals(pointId)) {
            System.out.println();
            return;
        }
        Long startTime = endTime - gapValue;
        StringBuilder sb = null;
        if (log.isDebugEnabled()) {
            sb = new StringBuilder("[");
        }
        String family = H_BASE_FAMILY_NPC_PI_REAL_TIME;
        //不是PI点
        if (!StringUtils.equals(pointId, sensorCode)) {
            family = pointId.replace(sensorCode, BLANK).substring(1);
            loop4Get(gets, x.getSensorCode() + ROW_KEY_SEPARATOR, family, startTime, endTime);
            return;
        }
        while (startTime <= endTime) {
            Integer index = hbaseUtil.indexOf3600(startTime);
            Long key = hbaseUtil.rowKeyOf3600(startTime);
            Get get = new Get((x.getSensorCode() + ROW_KEY_SEPARATOR + key).getBytes(StandardCharsets.UTF_8));
            if (StringUtils.isBlank(sensorCode)) {
                continue;
            }
            get.addColumn(family.getBytes(StandardCharsets.UTF_8), Bytes.toBytes(index));
            gets.add(get);
            startTime = startTime + timeGapValue;
            if (log.isDebugEnabled() && Objects.nonNull(sb)) {
                sb.append("\"" + x.getSensorCode() + ROW_KEY_SEPARATOR + key + ":" + family + ":" + index + "\",");
            }
        }
        System.out.println(Objects.isNull(sb) ? null : sb.substring(0, sb.length() - 1) + "]");
    }

    private static void loop4Get(List<Get> gets, String rowKey, String family, Long startTime, Long endTime) throws IOException {
        long l = (endTime - startTime) / 3600000;
        long i = 0;
        while (i <= l) {
            Long finalStartTime = startTime;
            long finalI = i;
            List<Get> collect = IntStream.range(0, 3599).mapToObj(x -> {
//                System.out.println((rowKey + hbaseUtil.rowKeyOf3600(finalStartTime + finalI * 3600 * 1000)));
                Get get = new Get((rowKey + hbaseUtil.rowKeyOf3600(finalStartTime + finalI * 3600 * 1000)).getBytes(StandardCharsets.UTF_8));
//                try {
//                    get.setTimeRange(finalStartTime, finalStartTime + l * 3600 * 1000);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                String username = null;
                get.addColumn(family.getBytes(StandardCharsets.UTF_8), Bytes.toBytes(x));
                return get;
            }).collect(Collectors.toList());
            boolean[] exists = table.exists(collect);
            for (int j = 0; j < exists.length; j++) {
                if (exists[j]) {
                    Get get = collect.get(j);
                    gets.add(get);
                }
            }
            i++;
            startTime = startTime + i * 3600 * 1000;
        }

    }

    private static List<HBaseTimeSeriesDataDTO> listByScan(HBaseUtil hbaseUtil, Long start, Long end, String tag) throws IOException {
        String tableName = "npc_phm_data";
        return hbaseUtil.listObjectDataWith3600Columns(tableName, tag, start, end, H_BASE_FAMILY_NPC_ESTIMATE);

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