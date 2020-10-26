package com.aimsphm.nuclear.data.service;

import org.apache.hadoop.hbase.client.Put;

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
public interface HBaseService {
    /**
     * 批量保存到hbase中
     *
     * @param tableName 要添加的表格名称
     * @param putList   要增加的列集合
     * @throws IOException
     */
    void batchSave2HBase(String tableName, List<Put> putList) throws IOException;

    /**
     * 保存到hbase数据
     *
     * @param tableName 表名称
     * @param put       增加的列对象
     */
    void save2HBase(String tableName, Put put) throws IOException;
}
