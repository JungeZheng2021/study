package com.aimsphm.nuclear.data;

import com.aimsphm.nuclear.common.constant.HBaseConstant;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    public static void main(String[] args) throws IOException {
        HBaseUtil hbaseUtil = new HBaseUtil(connection);
//        hbaseUtil.createTable("t5", Lists.newArrayList("t"), Compression.Algorithm.SNAPPY);
        String rowKey = UUID.randomUUID().toString().toUpperCase().substring(0, 1) + HBaseConstant.ROW_KEY_SEPARATOR + System.currentTimeMillis();
//        hbaseUtil.insertDouble("t5", rowKey, "t", "auto", 133.5);
        hbaseUtil.insertDouble("t5", "1_1603676917064 ", "acc-Rems", "demo", 33.5);
    }
}
