package com.aimsphm.nuclear.data;

import com.aimsphm.nuclear.common.constant.HBaseConstant;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.aimsphm.nuclear.data.feign.entity.dto.DataItemDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.ROW_KEY_SEPARATOR;


/**
 * @Package: com.aimsphm.nuclear.data
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/10/23 18:03
 * @UpdateUser: milla
 * @UpdateDate: 2020/10/23 18:03
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class HBaseTest {

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
            log.error("error:{}", e);
        }
    }

    public static final String HBASE_TABLE_NPC_REAL_TIME = "npc_real_time";

    public static void main(String[] args) throws IOException, InterruptedException {
        test();
        Long timestamp = 1605757290472L;
        long s = timestamp / (1000 * 3600) * (1000 * 3600);
        Integer index = Math.toIntExact(timestamp / 1000 % 3600);
        Long timestamp1 = 1605757291472L;
        long s1 = timestamp1 / (1000 * 3600) * (1000 * 3600);
        Integer index1 = Math.toIntExact(timestamp1 / 1000 % 3600);
        log.debug("", "", index1);
        Integer index2 = Math.toIntExact(timestamp1 / (1000 % 3600));
        log.debug("", index2);
//
        testBatch();
        HBaseUtil hbaseUtil = new HBaseUtil(connection);
////        时间//2020//年//1//月//1//日//1//时//1//分//1//秒//1577811661000
        long key = timestamp / (1000 * 3600) * (1000 * 3600);
        hbaseUtil.insertObject(HBASE_TABLE_NPC_REAL_TIME, "test-" + key, "pRaw", index, 10000.235, timestamp);
        TableName name = TableName.valueOf(HBASE_TABLE_NPC_REAL_TIME);
        List<Map<String, Object>> pRaw = hbaseUtil.selectData(HBASE_TABLE_NPC_REAL_TIME, "test_" + key, "pRaw");
//        log.debug("","生成数据的时间戳： " + timestamp);
        pRaw.stream().forEach(x -> {
            x.forEach((k, v) -> {
                List data = (List) v;
                data.stream().forEach(d -> log.debug("", d));
            });
        });


        Get get = new Get(("6M2DVC301MV-N_" + s).getBytes());
        get.addColumn("vRaw".getBytes(), Bytes.toBytes(index));
        Get get1 = new Get(("6M2DVC301MV-N_" + s1).getBytes());
        get1.addColumn("vRaw".getBytes(), Bytes.toBytes(index1));
        get1.addColumn("vRaw".getBytes(), Bytes.toBytes(2));
        Map<String, Map<Long, Object>> data = hbaseUtil.selectObjectDataWithGets("npc_phm_data", Lists.newArrayList(get, get1));
        data.forEach((k, v) -> {
            log.debug("", "key:" + k + " value:" + v);
        });
        Map<String, List<HBaseTimeSeriesDataDTO>> stringListMap = hbaseUtil.selectDataList("npc_phm_data", Lists.newArrayList(get));
        log.debug("", stringListMap);

        hbaseUtil.createTable("t5", Lists.newArrayList("acc-Rems"), Compression.Algorithm.SNAPPY);
        String rowKey = UUID.randomUUID().toString().toUpperCase().substring(0, 1) + HBaseConstant.ROW_KEY_SEPARATOR + System.currentTimeMillis();
        hbaseUtil.deleteTable("t5");

        hbaseUtil.insertDouble("t5", rowKey, "t", "auto", 133.5);
        hbaseUtil.insertDouble("t5", "1_1603676917064 ", "acc-Rems", "demo", 33.5);
        select(hbaseUtil, 5000000);
        long l = System.currentTimeMillis();
        long l1 = System.currentTimeMillis();
        log.debug("", "共计耗时： " + (l1 - l));


    }

    private static void test() {
        DataItemDTO dto = new DataItemDTO();
        dto.setTimestamp(123456789L);
        Long timestamp = dto.getTimestamp();
        long key = timestamp / (1000 * 3600) * (1000 * 3600);
        log.debug("", key);
        log.debug("", dto.getTimestamp());
    }

    private static void select(HBaseUtil hbaseUtil, int total) throws IOException, InterruptedException {
        TableName name = TableName.valueOf("t5");
        Table table = connection.getTable(name);
        Scan scan = new Scan();
        String family = "acc-Rems";
        boolean isHasFamily = StringUtils.isEmpty(family);
        if (!isHasFamily) {
            scan.addFamily(Bytes.toBytes(family));
        }
//        scan.withStartRow(Bytes.toBytes("5B6_" + (System.currentTimeMillis() - 1000 * 3600 * 500L)));
//        scan.withStopRow(Bytes.toBytes("5B6_" + System.currentTimeMillis()));
        RowFilter rowFilter1 = new RowFilter(CompareOperator.EQUAL, new BinaryPrefixComparator(Bytes.toBytes("HHH")));
        RowFilter rowFilter2 = new RowFilter(CompareOperator.EQUAL, new BinaryPrefixComparator(Bytes.toBytes("DDD")));
        List<Filter> rowFilters = Lists.newArrayList(rowFilter1, rowFilter2);

        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE, rowFilters);
        scan.setFilter(filterList);
        scan.setReversed(true);
//        scan.setLimit(1000000);
//        scan.setMaxResultsPerColumnFamily(100);
        ResultScanner scanner = table.getScanner(scan);

        int count = 0;
        LinkedList<DataItemDTO> data = Lists.newLinkedList();
        Test:
        for (Result rs : scanner) {
            log.debug("", "----------------------====>......");
            LinkedList<DataItemDTO> cellData = Lists.newLinkedList();
            List<Cell> cells = rs.listCells();
            for (int i = cells.size() - 1; i >= 0; i--) {
                if (count++ > total) {
                    data.addAll(0, cellData);
                    break Test;
                }
                Cell cell = cells.get(i);
                String rowKey = Bytes.toString(CellUtil.cloneRow(cell));
                String familyC = Bytes.toString(CellUtil.cloneFamily(cell));
                Integer qualifier = Bytes.toInt(CellUtil.cloneQualifier(cell));
                double value = Bytes.toDouble(CellUtil.cloneValue(cell));
                long timestamp = cell.getTimestamp();
                DataItemDTO itemDTO = new DataItemDTO();
                itemDTO.setValue(value);
                itemDTO.setTimestamp(timestamp);
                cellData.addFirst(itemDTO);
                log.info("  {}, 数量：{}, ,{\"rowKey\":{},\"family\":{},\"index\":{},\"value\":{},\"timestamp\":{}}", timestamp, count, rowKey, familyC, qualifier, value, timestamp);
            }
            data.addAll(0, cellData);
        }
        log.debug("", "------------------------------------------");
        data.stream().forEach((o) -> log.debug("", o.getTimestamp()));
        log.debug("", "------------------------------------------");
        connection.close();
    }

    private static void testBatch() throws IOException {
        List<Put> putList = Lists.newArrayList();
        Random random = new Random();
        for (int i = 0; i <= 200; i++) {
            String sensorCode = "test";
//            String family = "acc-Rems";
            String family = "pRaw";
//            if (i % 600 == 0) {
//                family = UUID.randomUUID().toString().toUpperCase().substring(0, 5);
//                hbaseUtil.addFamily2Table("t5", Lists.newArrayList(family), Compression.Algorithm.SNAPPY);
//            }
            long timestamp = 1577911661000L + 1000 * i;
            String rowKey = sensorCode + ROW_KEY_SEPARATOR + timestamp / (1000 * 3600) * (1000 * 3600);
            System.out.print("rowKey: " + rowKey);
            Integer index = Math.toIntExact(timestamp / 1000 % 3600);
            Put put = new Put(Bytes.toBytes(rowKey));
            put.setTimestamp(timestamp);
            log.debug("", ",插入时时间戳：" + timestamp);
            put.addColumn(Bytes.toBytes(family), Bytes.toBytes(index), Bytes.toBytes(random.nextDouble()));
            putList.add(put);
        }
        log.debug("", "共计元素：" + putList.size());
        batchSave2HBase(HBASE_TABLE_NPC_REAL_TIME, putList);
        log.debug("", "批量插入完毕：" + putList.size());
//        connection.close();

    }


    private static void batchSave2HBase(String tableName, List<Put> putList) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            table.put(putList);
        } catch (IOException e) {
            throw e;
        }
    }

    @Test
    public void contextLoads() {
        log.debug("tests");
    }

}

