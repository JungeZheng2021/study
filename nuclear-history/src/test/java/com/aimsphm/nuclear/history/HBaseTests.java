package com.aimsphm.nuclear.history;

import com.aimsphm.nuclear.common.util.HBaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.H_BASE_TABLE_NPC_PHM_DATA;

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
}