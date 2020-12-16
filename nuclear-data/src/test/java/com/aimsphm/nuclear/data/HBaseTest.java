package com.aimsphm.nuclear.data;

import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.aimsphm.nuclear.data.entity.DataItemDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
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
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HBaseUtil hbaseUtil = new HBaseUtil(connection);
        Get get = new Get("6M2DVC003MI_1605805200000".getBytes());
        get.addColumn("pRaw".getBytes(), Bytes.toBytes(1));
        Get get1 = new Get("6M2RCV011MN_1605805200000".getBytes());
        get1.addColumn("pRaw".getBytes(), Bytes.toBytes(2));
        Map<String, List<HBaseTimeSeriesDataDTO>> stringListMap = hbaseUtil.selectDataList("npc_phm_data", Lists.newArrayList(get, get1));
        System.out.println(stringListMap);

        //        hbaseUtil.createTable("t5", Lists.newArrayList("acc-Rems"), Compression.Algorithm.SNAPPY);
//        String rowKey = UUID.randomUUID().toString().toUpperCase().substring(0, 1) + HBaseConstant.ROW_KEY_SEPARATOR + System.currentTimeMillis();
//        hbaseUtil.deleteTable("t5");
//
//        hbaseUtil.insertDouble("t5", rowKey, "t", "auto", 133.5);
//        hbaseUtil.insertDouble("t5", "1_1603676917064 ", "acc-Rems", "demo", 33.5);
//        testBatch(hbaseUtil);
//        select(hbaseUtil, 5000000);
//        long l = System.currentTimeMillis();
//        List<HBaseTimeSeriesDataDTO> data = hbaseUtil.listDataWithLimit("t5", "acc-Rems", "HHH_", 10, 10, 2);
//        data.stream().forEach((o) -> System.out.println(o));
//        long l1 = System.currentTimeMillis();
//        System.out.println("共计耗时： " + (l1 - l));

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
            System.out.println("----------------------====>......");
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
        Thread.sleep(1000L);
        System.out.println("------------------------------------------");
        data.stream().forEach((o) -> System.out.println(o.getTimestamp()));
        System.out.println("------------------------------------------");
        connection.close();
    }

    private static void testBatch(HBaseUtil hbaseUtil) throws IOException {
        List<Put> putList = Lists.newArrayList();
        Random random = new Random();
//        for (int i = 200; i > 0; i--) {
        String sensorCode = "HHH";
        String family = "acc-Rems";
//            if (i % 600 == 0) {
//                family = UUID.randomUUID().toString().toUpperCase().substring(0, 5);
//                hbaseUtil.addFamily2Table("t5", Lists.newArrayList(family), Compression.Algorithm.SNAPPY);
//            }
        long timestamp = 1603796601001L;//System.currentTimeMillis() - 10 * 60 * 1000 * i;
        String rowKey = sensorCode + ROW_KEY_SEPARATOR + 1603796600001L;//timestamp / (1000 * 3600) * (1000 * 3600);

        Integer index = Math.toIntExact(timestamp / 1000 % 3600);
        Put put = new Put(Bytes.toBytes(rowKey));
        put.setTimestamp(timestamp);
        put.addColumn(Bytes.toBytes(family), Bytes.toBytes(index), Bytes.toBytes(random.nextDouble()));
        putList.add(put);
//        }
        System.out.println("共计元素：" + putList.size());
        batchSave2HBase("", putList);
        System.out.println("批量插入完毕：" + putList.size());
        connection.close();

    }


    private static void batchSave2HBase(String tableName, List<Put> putList) throws IOException {
        TableName name = TableName.valueOf("t5");
        try (Table table = connection.getTable(name)) {
            table.put(putList);
        } catch (IOException e) {
            throw e;
        }
    }

}
