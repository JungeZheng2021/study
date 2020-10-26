package com.aimsphm.nuclear.data.service.impl;

import com.aimsphm.nuclear.data.constant.Constant;
import com.aimsphm.nuclear.data.service.HBaseService;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.data.service
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/10/23 11:27
 * @UpdateUser: milla
 * @UpdateDate: 2020/10/23 11:27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class HBaseServiceImpl implements HBaseService {
    @Autowired
    private Connection connection;

    /**
     * 批量插入数据到hbase数据库中
     *
     * @param tableName
     * @param putList   要插入的数据
     * @throws IOException
     */
    @Override
    public void batchSave2HBase(String tableName, List<Put> putList) throws IOException {
        TableName name = TableName.valueOf(Constant.HBASE_TABLE_NPC_REAL_TIME);
        try (Table table = connection.getTable(name)) {
            table.put(putList);
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public void save2HBase(String tableName, Put put) throws IOException {
        TableName name = TableName.valueOf(Constant.HBASE_TABLE_NPC_REAL_TIME);
        try (Table table = connection.getTable(name)) {
            table.put(put);
        } catch (IOException e) {
            throw e;
        }
    }
}
