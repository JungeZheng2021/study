package com.aimsphm.nuclear.data.service;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.compress.Compression;

import java.io.IOException;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service.ext.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/10/23 11:27
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/10/23 11:27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface HBaseService {
    /**
     * 批量保存到hBase中
     *
     * @param tableName 要添加的表格名称
     * @param putList   要增加的列集合
     * @throws IOException
     */
    void batchSave2HBase(String tableName, List<Put> putList) throws IOException;

    /**
     * 保存到hBase数据
     *
     * @param tableName 表名称
     * @param put       增加的列对象
     * @throws IOException
     */
    void save2HBase(String tableName, Put put) throws IOException;

    /**
     * 判断列族是否存在
     *
     * @param tableName 表格名称
     * @param family    列族
     * @param isCreate  是否需要创建
     * @param type      算法类型
     * @return 是否存在或者是创建是否成功
     * @throws IOException
     */
    boolean familyExists(String tableName, String family, boolean isCreate, Compression.Algorithm type) throws IOException;
}
