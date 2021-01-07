package com.aimsphm.nuclear.common.util;

import com.aimsphm.nuclear.algorithm.entity.bo.PointEstimateDataBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;
import com.aimsphm.nuclear.common.pojo.EstimateResult;
import com.aimsphm.nuclear.common.pojo.EstimateTotal;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.*;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.ROW_KEY_SEPARATOR;

/**
 * @Package: com.aimsphm.nuclear.common.util
 * @Description: <HBase操作工具类>
 * @Author: MILLA
 * @CreateDate: 2020/3/5 13:40
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/5 13:40
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@ConditionalOnProperty(prefix = "spring.config", name = "enableHBase", havingValue = "true")
@Component
public class HBaseUtil {
    private Connection connection;
    @Autowired
    Gson gson;

    public HBaseUtil(Connection connection) {
        this.connection = connection;
    }

    /**
     * 创建表
     *
     * @param tableName 表名
     * @param families  列族
     */
    public void createTable(String tableName, List<String> families, Compression.Algorithm type) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Admin admin = connection.getAdmin()) {
            Assert.isTrue(!admin.tableExists(name), "This table already exists");
            TableDescriptorBuilder desc = TableDescriptorBuilder.newBuilder(name);
            for (String cf : families) {
                ColumnFamilyDescriptorBuilder builder = ColumnFamilyDescriptorBuilder.newBuilder(cf.getBytes());
                if (type != null) {
                    builder.setCompressionType(type);
                }
                // 指定最大版本1，值会被覆盖
                builder.setMaxVersions(1);
                desc.setColumnFamily(builder.build());
            }
            admin.createTable(desc.build());
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 获取表格的建表语句
     *
     * @param tableName 表名称
     * @return
     * @throws IOException
     */
    public String tableDescription(String tableName) throws IOException {
        TableName table = TableName.valueOf(tableName);
        try (Admin admin = connection.getAdmin()) {
            TableDescriptor descriptor = admin.getDescriptor(table);
            ColumnFamilyDescriptor[] columnFamilies = descriptor.getColumnFamilies();
            StringBuilder sb = new StringBuilder("create '" + table.getNameWithNamespaceInclAsString() + "',");
            for (ColumnFamilyDescriptor cf : columnFamilies) {
                sb.append("{NAME=>");
                // 存储块大小
                int blockSize = cf.getBlocksize();
                // 存活时间
                int timeToLive = cf.getTimeToLive();
                // 最大版本
                int maxVersions = cf.getMaxVersions();
                // 列族
                String family = Bytes.toString(cf.getName());
                // 启缓存
                boolean blockCacheEnabled = cf.isBlockCacheEnabled();
                // 配置信息
                Map<String, String> configuration = cf.getConfiguration();
                // 压缩算法
                Compression.Algorithm compressionType = cf.getCompressionType();
                // 编码
                DataBlockEncoding dataBlockEncoding = cf.getDataBlockEncoding();
                sb.append(" '").append(family).append("'").append(", VERSIONS => ").append(maxVersions)
                        .append(", BLOCKSIZE => '").append(blockSize).append("' ").append(", TTL => '")
                        .append(timeToLive).append("' ").append(", BLOCKCACHE => '").append(blockCacheEnabled)
                        .append("' ").append(", COMPRESSION => '").append(compressionType.getName()).append("' ")
                        .append(", DATA_BLOCK_ENCODING => '").append(Bytes.toString(dataBlockEncoding.getNameInBytes()))
                        .append("' ");

                // 配置信息
                if (!configuration.isEmpty()) {
                    sb.append(", CONFIGURATION => {");
                    Set<Map.Entry<String, String>> entries = configuration.entrySet();
                    for (Iterator<Map.Entry<String, String>> it = entries.iterator(); it.hasNext(); ) {
                        Map.Entry<String, String> next = it.next();
                        sb.append("'").append(next.getKey()).append("' => '").append(next.getValue()).append("',");
                    }
                    sb.delete(sb.length() - 1, sb.length()).append("}");
                }
                sb.append("},");
            }

            sb.delete(sb.length() - 1, sb.length());
            return sb.toString();
        } catch (IOException e) {
            throw e;
        }
    }

    public void addFamily2Table(String tableName, List<String> families, Compression.Algorithm type)
            throws IOException {
        TableName table = TableName.valueOf(tableName);
        try (Admin admin = connection.getAdmin()) {
            Assert.isTrue(admin.tableExists(table), "This table not exists");
            for (String cf : families) {
                ColumnFamilyDescriptorBuilder builder = ColumnFamilyDescriptorBuilder.newBuilder(cf.getBytes());
                if (type != null) {
                    builder.setCompressionType(type);
                }
                // 指定最大版本1，值会被覆盖
                builder.setMaxVersions(1);
                admin.addColumnFamily(table, builder.build());
            }
        } catch (IOException e) {
            throw e;
        }
    }

    public void modifyColumnFamilyName(String tableName, String family, Compression.Algorithm type) throws IOException {
        TableName table = TableName.valueOf(tableName);
        try (Admin admin = connection.getAdmin()) {
            Assert.isTrue(admin.tableExists(table), "This table not exists");
            ColumnFamilyDescriptorBuilder builder = ColumnFamilyDescriptorBuilder.newBuilder(family.getBytes());
            if (type != null) {
                builder.setCompressionType(type);
            }
            // 指定最大版本1，值会被覆盖
            builder.setMaxVersions(1);
            admin.modifyColumnFamily(table, builder.build());
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 删除表操作
     *
     * @param tableName
     */
    public void deleteTable(String tableName) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Admin admin = connection.getAdmin()) {
            Assert.isTrue(admin.tableExists(name), "This table not exists");
            admin.disableTable(name);
            admin.deleteTable(name);
        } catch (IOException e) {
            throw e;
        }

    }

    /**
     * 删除列族操作
     *
     * @param tableName 表格名称
     * @param family    列族
     */
    public void deleteFamily(String tableName, String family) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Admin admin = connection.getAdmin()) {
            Assert.isTrue(admin.tableExists(name), "This table not exists");
            admin.deleteColumnFamily(name, Bytes.toBytes(family));
        } catch (IOException e) {
            throw e;
        }

    }

    /**
     * 插入记录（单行单列族-单列单值-指定时间戳）
     *
     * @param tableName 表名
     * @param rowKey    行名
     * @param family    列族名
     * @param column    列名
     * @param value     值
     * @param timestamp 值对应的时间戳
     */
    public void insertDouble(String tableName, String rowKey, String family, Object column, Double value,
                             Long timestamp) throws IOException {
        insertObject(tableName, rowKey, family, column, value, timestamp);
    }

    /**
     * 插入记录（单行单列族-单列单值-不指定时间戳）
     *
     * @param tableName 表名
     * @param rowKey    行名
     * @param family    列族名
     * @param column    列名
     * @param value     值
     */
    public void insertDouble(String tableName, String rowKey, String family, String column, Double value)
            throws IOException {
        insertObject(tableName, rowKey, family, column, value, null);
    }

    /**
     * 插入记录（单行单列族-单列单值）
     *
     * @param tableName 表名
     * @param rowKey    行名
     * @param family    列族名
     * @param column    列名
     * @param value     值
     * @param timestamp 值对应的时间戳
     */
    public void insertObject(String tableName, String rowKey, String family, Object column, Object value,
                             Long timestamp) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            Put put = new Put(Bytes.toBytes(rowKey));
            if (timestamp != null) {
                put.setTimestamp(timestamp);
            }
            put.addColumn(Bytes.toBytes(family), getBytes(column), getBytes(value));
            table.put(put);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 插入记录（单行单列族-多列多值）
     *
     * @param tableName 表名
     * @param rowKey    行名
     * @param family    列族名
     * @param columns   列名（数组）
     * @param values    值（数组）（且需要和列一一对应）
     */
    public void insertDoubles(String tableName, String rowKey, String family, List<Object> columns, List<Double> values)
            throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            Put put = new Put(Bytes.toBytes(rowKey));
            for (int i = 0, len = columns.size(); i < len; i++) {
                put.addColumn(Bytes.toBytes(family), getBytes(columns.get(i)), Bytes.toBytes(values.get(i)));
                table.put(put);
            }
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 查找一行记录
     *
     * @param tableName 表名
     * @param rowKey    行名
     */
    public List<Map<String, Object>> selectData(String tableName, String rowKey) throws IOException {
        return this.selectData(tableName, rowKey, null);
    }

    /**
     * 查找一行记录
     *
     * @param tableName 表名
     * @param rowKey    行名
     * @param family    列族
     */
    public List<Map<String, Object>> selectData(String tableName, String rowKey, String family) throws IOException {
        return this.selectData(tableName, rowKey, family, null);
    }

    /**
     * 查找一行记录
     *
     * @param tableName 表名
     * @param rowKey    行名
     * @param family    列族
     * @param qualifier 列名
     */
    public List<Map<String, Object>> selectData(String tableName, String rowKey, String family, String qualifier)
            throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            Get g = new Get(rowKey.getBytes());
            if (!StringUtils.isEmpty(family)) {
                g.addFamily(Bytes.toBytes(family));
                if (!StringUtils.isEmpty(qualifier)) {
                    g.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
                }
            }
            Result rs = table.get(g);
            Map<String, Set<String>> familyMap = Maps.newHashMap();
            Map<String, List<List<Object>>> dataWithQualifier = Maps.newHashMap();
            assembleCellDataWithList(familyMap, dataWithQualifier, rs);
            return assembleReturnDataWithList(dataWithQualifier, familyMap.entrySet());
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * @param tableName   表格名称
     * @param rowKeyStart 开始rowKey
     * @param rowKeyEnd   结束rowKey
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> selectDataList(String tableName, String rowKeyStart, String rowKeyEnd)
            throws IOException {
        return this.selectDataList(tableName, rowKeyStart, rowKeyEnd, null);
    }

    /**
     * @param tableName   表格名称
     * @param rowKeyStart 开始rowKey
     * @param rowKeyEnd   结束rowKey
     * @param family      列族
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> selectDataList(String tableName, String rowKeyStart, String rowKeyEnd,
                                                    String family) throws IOException {
        return this.selectDataList(tableName, rowKeyStart, rowKeyEnd, family, null);
    }

    /**
     * @param tableName 表格名称
     * @param gets      批量get
     * @return
     * @throws IOException
     */
    public Map<String, List<HBaseTimeSeriesDataDTO>> selectDataList(String tableName, List<Get> gets) throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            Result[] results = table.get(gets);
            Map<String, List<HBaseTimeSeriesDataDTO>> data = new HashMap<>(16);
            String sensorCode = null;
            for (Result rs : results) {
                String rowKey = Bytes.toString(rs.getRow());
                if (rowKey.contains(ROW_KEY_SEPARATOR)) {
                    sensorCode = rowKey.split(ROW_KEY_SEPARATOR)[0];
                    data.putIfAbsent(sensorCode, Lists.newArrayList());
                }
                for (Cell cell : rs.listCells()) {
                    double value = Bytes.toDouble(CellUtil.cloneValue(cell));
                    Long timestamp = cell.getTimestamp();
                    HBaseTimeSeriesDataDTO dto = new HBaseTimeSeriesDataDTO();
                    dto.setTimestamp(timestamp);
                    dto.setValue(value);
                    data.get(sensorCode).add(dto);
                }
            }
            return data;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * @param tableName   表格名称
     * @param rowKeyStart 时间区间内的开始时间
     * @param rowKeyEnd   时间区间内的结束时间
     * @param family      列族
     * @return
     */

    public Object selectNewestData(String tableName, String rowKeyStart, String rowKeyEnd, String family)
            throws IOException {
        try (Table table = connection.getTable(TableName.valueOf(tableName))) {
            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes(family));
            scan.setReversed(true);
            scan.withStartRow(Bytes.toBytes(rowKeyEnd));
            scan.withStopRow(Bytes.toBytes(rowKeyStart));
            ResultScanner scanner = table.getScanner(scan);
            Result next = scanner.next();
            // 获取结果中的第一行
            if (next == null || next.size() == 0) {
                return null;
            }
            List<Cell> cells = next.listCells();
            return getObject(CellUtil.cloneValue(cells.get(cells.size() - 1)), Double.class);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 根据小时获取数据(时间戳秒级数据分3600列存储)
     *
     * @param tableName 表格名称
     * @param tag       唯一标识
     * @param startTime 开始行
     * @param endTime   结束行
     * @param family    列族
     * @return
     * @throws IOException
     */
    public List<HBaseTimeSeriesDataDTO> listObjectDataWith3600Columns(String tableName, String tag, Long startTime, Long
            endTime, String family) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes(family));
            Long startRow = startTime / (1000 * 3600) * (1000 * 3600);
            Long endRow = endTime / (1000 * 3600) * (1000 * 3600) + 1;
            scan.withStartRow(Bytes.toBytes(tag + ROW_KEY_SEPARATOR + startRow));
            scan.withStopRow(Bytes.toBytes(tag + ROW_KEY_SEPARATOR + endRow));
            ResultScanner scanner = table.getScanner(scan);

            List<HBaseTimeSeriesDataDTO> items = new ArrayList();
            for (Result rs : scanner) {
                for (Cell cell : rs.listCells()) {
                    double value = Bytes.toDouble(CellUtil.cloneValue(cell));
                    Long timestamp = cell.getTimestamp();
                    //如果列的时间戳大于终点查询时间跳出
                    if (timestamp > endTime) {
                        break;
                    }
                    //如果列的时间戳小于开始时间直接丢弃
                    if (timestamp < startTime) {
                        continue;
                    }
                    HBaseTimeSeriesDataDTO dto = new HBaseTimeSeriesDataDTO();
                    dto.setTimestamp(timestamp);
                    dto.setValue(value);
                    items.add(dto);
                }
            }
            return items;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 根据小时获取数据(时间戳秒级数据分3600列存储)
     *
     * @param tableName 表格名称
     * @param tag       唯一标识
     * @param startTime 开始行
     * @param endTime   结束行
     * @param family    列族
     * @return
     * @throws IOException
     */
    public List<List<Object>> listDataWith3600Columns(String tableName, String tag, Long startTime, Long
            endTime, String family) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes(family));
            Long startRow = startTime / (1000 * 3600) * (1000 * 3600);
            Long endRow = endTime / (1000 * 3600) * (1000 * 3600) + 1;
            scan.withStartRow(Bytes.toBytes(tag + ROW_KEY_SEPARATOR + startRow));
            scan.withStopRow(Bytes.toBytes(tag + ROW_KEY_SEPARATOR + endRow));
            ResultScanner scanner = table.getScanner(scan);

            List<List<Object>> items = new ArrayList();
            for (Result rs : scanner) {
                for (Cell cell : rs.listCells()) {
                    double value = Bytes.toDouble(CellUtil.cloneValue(cell));
                    Long timestamp = cell.getTimestamp();
                    //如果列的时间戳大于终点查询时间跳出
                    if (timestamp > endTime) {
                        break;
                    }
                    //如果列的时间戳小于开始时间直接丢弃
                    if (timestamp < startTime) {
                        continue;
                    }
                    List<Object> item = Lists.newArrayList();
                    item.add(timestamp);
                    item.add(value);
                    items.add(item);
                }
            }
            return items;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * @param tableName  表格名称
     * @param start      开始rowKey
     * @param end        结束rowKey
     * @param family     列族
     * @param qualifiers 列集合
     * @return
     * @throws IOException
     */
    public List<PointEstimateDataBO> selectModelDataList(String tableName, Long start, Long end, String family, List<String> qualifiers, Long modelId) throws IOException {
        TableName name = TableName.valueOf(tableName);
        String rowKeyStart = modelId + ROW_KEY_SEPARATOR + start;
        String rowKeyEnd = modelId + ROW_KEY_SEPARATOR + end;
        try (Table table = connection.getTable(name)) {
            Scan scan = new Scan();
            boolean isHasFamily = StringUtils.isEmpty(family);
            boolean isHasQualifier = CollectionUtils.isEmpty(qualifiers);
            if (!isHasFamily) {
                scan.addFamily(Bytes.toBytes(family));
                if (!isHasQualifier) {
                    qualifiers.stream().forEach(x -> {
                        scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(x));
                    });
                }
            }
            scan.withStartRow(Bytes.toBytes(rowKeyStart));
            scan.withStopRow(Bytes.toBytes(rowKeyEnd));
            ResultScanner scanner = table.getScanner(scan);
            List<PointEstimateDataBO> data = new ArrayList<>();
            for (Result rs : scanner) {
                for (Cell cell : rs.listCells()) {
                    PointEstimateDataBO object = (PointEstimateDataBO) getObject(CellUtil.cloneValue(cell), PointEstimateDataBO.class);
                    data.add(object);
                }
            }
            return data;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 获取数据列表
     *
     * @param tableName   表格名称
     * @param family      指定的列族
     * @param prefixKey   指定rowKey的开始前缀
     * @param rowLimit    需要查询的行数
     * @param columnLimit 每行最多取多少列
     * @param total       共计查询多个条数据
     * @return
     * @throws IOException
     */
    public List<HBaseTimeSeriesDataDTO> listDataWithLimit(String tableName, String family, String
            prefixKey, Integer rowLimit, Integer columnLimit, Integer total) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            Scan scan = new Scan();
            boolean isHasFamily = StringUtils.isEmpty(family);
            if (!isHasFamily) {
                scan.addFamily(Bytes.toBytes(family));
            }
            //前缀过滤器
            RowFilter rowFilter = new RowFilter(CompareOperator.EQUAL, new BinaryPrefixComparator(Bytes.toBytes(prefixKey)));
            scan.setFilter(rowFilter);
            //逆序返回
            scan.setReversed(true);
            if (Objects.nonNull(rowLimit)) {
                //限制行数
                scan.setLimit(rowLimit);
            }
            if (Objects.nonNull(columnLimit)) {
                //设置最大列数
                scan.setMaxResultsPerColumnFamily(columnLimit);
            }
            ResultScanner scanner = table.getScanner(scan);
            List<HBaseTimeSeriesDataDTO> data = Lists.newArrayList();
            int count = 0;
            for (Result rs : scanner) {
                LinkedList<HBaseTimeSeriesDataDTO> cellData = Lists.newLinkedList();
                List<Cell> cells = rs.listCells();
                for (int i = cells.size() - 1; i >= 0; i--) {
                    if (++count > total) {
                        data.addAll(0, cellData);
                        return data;
                    }
                    Cell cell = cells.get(i);
                    double value = Bytes.toDouble(CellUtil.cloneValue(cell));
                    long timestamp = cell.getTimestamp();
                    HBaseTimeSeriesDataDTO dto = new HBaseTimeSeriesDataDTO();
                    dto.setValue(value);
                    dto.setTimestamp(timestamp);
                    cellData.addFirst(dto);
                }
                data.addAll(0, cellData);
            }
            return data;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * @param tableName   表格名称
     * @param rowKeyStart 开始rowKey
     * @param rowKeyEnd   结束rowKey
     * @param family      列族
     * @param qualifier   列
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> selectDataList(String tableName, String rowKeyStart, String rowKeyEnd,
                                                    String family, Object qualifier) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            Scan scan = new Scan();
            boolean isHasFamily = StringUtils.isEmpty(family);
            boolean isHasQualifier = Objects.isNull(qualifier) || StringUtils.isEmpty(String.valueOf(qualifier));
            if (!isHasFamily) {
                scan.addFamily(Bytes.toBytes(family));
                if (!isHasQualifier) {
                    scan.addColumn(Bytes.toBytes(family), getBytes(qualifier));
                }
            }
            scan.withStartRow(Bytes.toBytes(rowKeyStart));
            scan.withStopRow(Bytes.toBytes(rowKeyEnd));
            ResultScanner scanner = table.getScanner(scan);
            Map<String, Set<String>> familyMap = Maps.newHashMap();
            Map<String, List<List<Object>>> dataWithQualifier = Maps.newHashMap();
            for (Result rs : scanner) {
                assembleCellDataWithList(familyMap, dataWithQualifier, rs);

            }
            return assembleReturnDataWithList(dataWithQualifier, familyMap.entrySet());
        } catch (IOException e) {
            throw e;
        }
    }


    /**
     * @param familyMap     列族集合
     * @param withQualifier 列数据集合(以集合方式返回)
     * @param rs            结果集
     */
    private void assembleModelTagCellDataWithList
    (Map<String, Set<String>> familyMap, Map<String, List<Object>> withQualifier, Result rs) {
        if (rs.size() == 0) {
            return;
        }
        for (Cell cell : rs.listCells()) {
            String family = Bytes.toString(CellUtil.cloneFamily(cell));
            String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
            String value = Bytes.toString(CellUtil.cloneValue(cell));
            EstimateResult es = gson.fromJson(value, EstimateResult.class);
            long timestamp = cell.getTimestamp();

            if (!withQualifier.containsKey(qualifier)) {
                withQualifier.put(qualifier, Lists.newArrayList());
            }
            if (!familyMap.containsKey(family)) {
                familyMap.put(family, Sets.newHashSet());
            }
            familyMap.get(family).add(qualifier);
            withQualifier.get(qualifier).add(es);

//            withQualifier.get(qualifier).add(Lists.newArrayList(timestamp, es));
        }
    }

    /**
     * @param familyMap     列族集合
     * @param withQualifier 列数据集合(以集合方式返回)
     * @param rs            结果集
     */
    private void assembleCellDataWithList
    (Map<String, Set<String>> familyMap, Map<String, List<List<Object>>> withQualifier, Result rs) {
        if (rs.size() == 0) {
            return;
        }
        for (Cell cell : rs.listCells()) {
            String family = Bytes.toString(CellUtil.cloneFamily(cell));
            String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
            double value = Bytes.toDouble(CellUtil.cloneValue(cell));
            long timestamp = cell.getTimestamp();

            if (!withQualifier.containsKey(qualifier)) {
                withQualifier.put(qualifier, Lists.newArrayList());
            }
            if (!familyMap.containsKey(family)) {
                familyMap.put(family, Sets.newHashSet());
            }
            familyMap.get(family).add(qualifier);
            withQualifier.get(qualifier).add(Lists.newArrayList(timestamp, value));
        }
    }

    /**
     * @param dataWithQualifier 列数据集合
     * @param entries           列族集合
     * @return 返回数据是集合结构
     */
    private List<Map<String, Object>> assembleReturnDataWithList(Map dataWithQualifier,
                                                                 Set<Map.Entry<String, Set<String>>> entries) {
        List<Map<String, Object>> families = Lists.newArrayList();
        for (Iterator<Map.Entry<String, Set<String>>> it = entries.iterator(); it.hasNext(); ) {
            Map.Entry<String, Set<String>> next = it.next();
            String key = next.getKey();
            Set<String> value = next.getValue();
            Map<String, Object> familyData = Maps.newHashMap();
            List<Map<String, Object>> qualifiers = Lists.newArrayList();
            familyData.put("family", key);
            familyData.put("qualifiers", qualifiers);
            families.add(familyData);
            for (String qualifier : value) {
                Map<String, Object> qualifierData = Maps.newHashMap();
                qualifierData.put("qualifier", qualifier);
                qualifierData.put("data", dataWithQualifier.get(qualifier));
                qualifiers.add(qualifierData);
            }
        }
        return families;
    }

    /**
     * @param qualifierMap 列集合
     * @param entries      列族集合
     * @return 经过转换的返回值
     */
    private List<Map<String, Object>> assembleReturnData(Map<String, List<Object>> qualifierMap,
                                                         Set<Map.Entry<String, Set<String>>> entries) {
        List<Map<String, Object>> families = Lists.newArrayList();
        for (Iterator<Map.Entry<String, Set<String>>> it = entries.iterator(); it.hasNext(); ) {
            Map.Entry<String, Set<String>> next = it.next();
            String key = next.getKey();
            Set<String> value = next.getValue();
            Map<String, Object> familyData = Maps.newHashMap();
            List<Map<String, Object>> qualifiers = Lists.newArrayList();
            familyData.put("family", key);
            familyData.put("qualifiers", qualifiers);
            families.add(familyData);
            for (String cell : value) {
                Map<String, Object> qualifierData = Maps.newHashMap();
                qualifierData.put("qualifier", cell);
                qualifierData.put("times", qualifierMap.get(cell + "Times"));
                qualifierData.put("values", qualifierMap.get(cell + "Values"));
                qualifiers.add(qualifierData);
            }
        }
        return families;
    }

    /**
     * 组装数据
     *
     * @param familyMap    列族集合
     * @param qualifierMap 列集合
     * @param rs
     */
    private void assembleCellData
    (Map<String, Set<String>> familyMap, Map<String, List<Object>> qualifierMap, Result rs) {
        if (rs.size() == 0) {
            return;
        }
        for (Cell cell : rs.listCells()) {
            String family = Bytes.toString(CellUtil.cloneFamily(cell));
            String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
            double value = Bytes.toDouble(CellUtil.cloneValue(cell));
            long timestamp = cell.getTimestamp();

            if (!qualifierMap.containsKey(qualifier + "Times")) {
                qualifierMap.put(qualifier + "Times", Lists.newArrayList());
                qualifierMap.put(qualifier + "Values", Lists.newArrayList());
            }
            if (!familyMap.containsKey(family)) {
                familyMap.put(family, Sets.newHashSet());
            }
            familyMap.get(family).add(qualifier);
            qualifierMap.get(qualifier + "Times").add(timestamp);
            qualifierMap.get(qualifier + "Values").add(value);
        }
    }

    /**
     * 删除一行记录
     *
     * @param tableName 表名
     * @param rowKey    行名
     */
    public void deleteRowData(String tableName, String rowKey) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            Delete d = new Delete(rowKey.getBytes());
            table.delete(d);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 删除单行单列族记录
     *
     * @param tableName 表名
     * @param rowKey    行名
     * @param family    列族名
     */
    public void deleteFamilyData(String tableName, String rowKey, String family) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            Delete d = new Delete(rowKey.getBytes()).addFamily(Bytes.toBytes(family));
            table.delete(d);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 删除单行单列族单列记录
     *
     * @param tableName 表名
     * @param rowKey    行名
     * @param family    列族名
     * @param qualifier 列名
     */
    public void deleteColumnData(String tableName, String rowKey, String family, String qualifier) throws
            IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            Delete d = new Delete(rowKey.getBytes()).addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
            table.delete(d);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 查找单行单列族单列记录
     *
     * @param tableName 表名
     * @param rowKey    行名
     * @param family    列族名
     * @param column    列名
     * @return
     */
    public Object selectValue(String tableName, String rowKey, String family, String column) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            Get g = new Get(rowKey.getBytes());
            g.addColumn(Bytes.toBytes(family), Bytes.toBytes(column));
            Result rs = table.get(g);
            return getObject(rs.value(), Double.class);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 利用协处理器进行全表count统计
     *
     * @param tableName
     */
    public Long countRowsWithCoprocessor(String tableName) throws Throwable {
        TableName name = TableName.valueOf(tableName);
        Admin admin = connection.getAdmin();
        HTableDescriptor descriptor = admin.getTableDescriptor(name);
        String coprocessorClass = "org.apache.hadoop.hbase.coprocessor.AggregateImplementation";
        if (!descriptor.hasCoprocessor(coprocessorClass)) {
            admin.disableTable(name);
            descriptor.addCoprocessor(coprocessorClass);
            admin.modifyTable(name, descriptor);
            admin.enableTable(name);
        }
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Scan scan = new Scan();
        // Hbase服务端需要开启该服务
        AggregationClient aggregationClient = new AggregationClient(connection.getConfiguration());
        Long count = aggregationClient.rowCount(name, new LongColumnInterpreter(), scan);
        stopWatch.stop();
        log.debug("RowCount:{} ,全表count统计耗时:{}", count, stopWatch.getTotalTimeMillis());
        return count;
    }

    /**
     * 根据不同的类型获取不同的byte数组
     *
     * @param object
     * @return
     */
    private static byte[] getBytes(Object object) {
        Class className = object.getClass();
        if (className.equals(Boolean.class)) {
            return Bytes.toBytes((Boolean) object);
        }
        if (className.equals(Byte.class)) {
            return Bytes.toBytes((Byte) object);
        }
        if (className.equals(Short.class)) {
            return Bytes.toBytes((Short) object);
        }
        if (className.equals(Character.class)) {
            return Bytes.toBytes((Character) object);
        }
        if (className.equals(Integer.class)) {
            return Bytes.toBytes((Integer) object);
        }
        if (className.equals(Long.class)) {
            return Bytes.toBytes((Long) object);
        }
        if (className.equals(Float.class)) {
            return Bytes.toBytes((Float) object);
        }
        if (className.equals(Double.class)) {
            return Bytes.toBytes((Double) object);
        }
        if (className.equals(String.class)) {
            return Bytes.toBytes((String) object);
        }
        if (className.equals(ByteBuffer.class)) {
            return Bytes.toBytes((ByteBuffer) object);
        }
        if (className.equals(BigDecimal.class)) {
            return Bytes.toBytes((BigDecimal) object);
        }
        return ByteUtil.toBytes(object);
    }

    /**
     * 根据不同的byte数组获取不同的对象
     *
     * @param bytes
     * @return
     */
    private static Object getObject(byte[] bytes, Class className) {
        if (className.equals(Boolean.class)) {
            return Bytes.toBoolean(bytes);
        }
        if (className.equals(Byte.class)) {
            return Bytes.toShort(bytes);
        }
        if (className.equals(Short.class)) {
            return Bytes.toShort(bytes);
        }
        if (className.equals(Character.class)) {
            return Bytes.toString(bytes);
        }
        if (className.equals(Integer.class)) {
            return Bytes.toInt(bytes);
        }
        if (className.equals(Long.class)) {
            return Bytes.toLong(bytes);
        }
        if (className.equals(Float.class)) {
            return Bytes.toFloat(bytes);
        }
        if (className.equals(Double.class)) {
            return Bytes.toDouble(bytes);
        }
        if (className.equals(String.class)) {
            return Bytes.toString(bytes);
        }
        if (className.equals(ByteBuffer.class)) {
            return ByteBuffer.wrap(bytes);
        }
        if (className.equals(BigDecimal.class)) {
            return Bytes.toBigDecimal(bytes);
        }
        return ByteUtil.toObject(bytes);
    }
}
