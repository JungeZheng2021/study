package com.aimsphm.nuclear.data.service.impl;

import com.aimsphm.nuclear.data.service.HBaseService;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @Package: com.aimsphm.nuclear.data.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/10/23 11:27
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/10/23 11:27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class HBaseServiceImpl implements HBaseService {
    @Autowired
    private Connection connection;

    @Override
    public void batchSave2HBase(String tableName, List<Put> putList) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            table.put(putList);
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public void save2HBase(String tableName, Put put) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            table.put(put);
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public boolean familyExists(String tableName, String family, boolean isCreate, Compression.Algorithm type) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            TableDescriptor descriptor = table.getDescriptor();
            ColumnFamilyDescriptor descriptorCf = descriptor.getColumnFamily(Bytes.toBytes(family));
            if (Objects.nonNull(descriptorCf)) {
                return true;
            }
            if (isCreate) {
                ColumnFamilyDescriptorBuilder builder = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(family));
                if (type != null) {
                    builder.setCompressionType(type);
                }
                connection.getAdmin().addColumnFamily(name, builder.build());
                return true;
            }
        } catch (IOException e) {
            throw e;
        }
        return false;
    }
}
