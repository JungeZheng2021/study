package com.aimsphm.nuclear.common.util;

import com.aimsphm.nuclear.common.constant.HBaseConstant;
import com.aimsphm.nuclear.common.pojo.EstimateResult;
import com.aimsphm.nuclear.common.pojo.EstimateTotal;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * @Package: com.aimsphm.nuclear.hbase.utill
 * @Description: <Hbase操作工具类>
 * @Author: MILLA
 * @CreateDate: 2020/3/5 13:40
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/5 13:40
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
@Component
public class HBaseUtil {
    @Autowired
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
                int blockSize = cf.getBlocksize(); // 存储块大小
                int timeToLive = cf.getTimeToLive(); // 存活时间
                int maxVersions = cf.getMaxVersions(); // 最大版本
                String family = Bytes.toString(cf.getName()); // 列族
                boolean blockCacheEnabled = cf.isBlockCacheEnabled(); // 启缓存
                Map<String, String> configuration = cf.getConfiguration(); // 配置信息
                Compression.Algorithm compressionType = cf.getCompressionType(); // 压缩算法
                DataBlockEncoding dataBlockEncoding = cf.getDataBlockEncoding(); // 编码
                sb.append(" '").append(family).append("'").append(", VERSIONS => ").append(maxVersions)
                        .append(", BLOCKSIZE => '").append(blockSize).append("' ").append(", TTL => '")
                        .append(timeToLive).append("' ").append(", BLOCKCACHE => '").append(blockCacheEnabled)
                        .append("' ").append(", COMPRESSION => '").append(compressionType.getName()).append("' ")
                        .append(", DATA_BLOCK_ENCODING => '").append(Bytes.toString(dataBlockEncoding.getNameInBytes()))
                        .append("' ");

                if (!configuration.isEmpty()) {// 配置信息
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
    public List<List<Object>> selectDataListByHours(String tableName, String tag, Long startTime, Long endTime, String family) throws IOException {
        TableName name = TableName.valueOf(tableName);
        try (Table table = connection.getTable(name)) {
            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes(family));
            Long startRow = startTime / (1000 * 3600) * (1000 * 3600);
            Long endRow = endTime / (1000 * 3600) * (1000 * 3600) + 1;
            scan.withStartRow(Bytes.toBytes(tag + HBaseConstant.ROW_KEY_SEPARATOR + startRow));
            scan.withStopRow(Bytes.toBytes(tag + HBaseConstant.ROW_KEY_SEPARATOR + endRow));
            ResultScanner scanner = table.getScanner(scan);

            List<List<Object>> data = Lists.newArrayList();
            for (Result rs : scanner) {
                List<List<Object>> items = new ArrayList();
                for (Cell cell : rs.listCells()) {
                    List<Object> item = new ArrayList<>();
                    double value = Bytes.toDouble(CellUtil.cloneValue(cell));
                    Long timestamp = cell.getTimestamp();
                    if (timestamp > endTime) {//如果列的时间戳大于终点查询时间跳出
                        break;
                    }
                    if (timestamp < startTime) {//如果列的时间戳小于开始时间直接丢弃
                        continue;
                    }
                    item.add(timestamp);
                    item.add(value);
                    items.add(item);
                }
                data.addAll(items);
            }
            return data;
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
     * @param step      取数的间隔
     * @return
     * @throws IOException
     */
    public List<com.aimsphm.nuclear.common.entity.dto.Cell> selectDataListByHoursStep(String tableName, String tag, Long startTime, Long endTime, String family, Integer step) throws IOException {
        TableName name = TableName.valueOf(tableName);
        if (step == null) {
            long duration = endTime - startTime;
            if (duration <= 3 * 3600 * 1000l) {
                step = 1;
            } else if (duration > 3 * 3600 * 1000l && duration <= 7 * 24 * 3600 * 1000l) {
                step = 60;
            } else if (duration > 7 * 24 * 3600 * 1000l && duration <= 30 * 24 * 3600 * 1000l) {
                step = 600;
            } else {
                step = 3600;
            }
        }
        try (Table table = connection.getTable(name)) {
            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes(family));
            Long startRow = startTime / (1000 * 3600) * (1000 * 3600);
            Long endRow = endTime / (1000 * 3600) * (1000 * 3600) + 1;
            scan.withStartRow(Bytes.toBytes(tag + HBaseConstant.ROW_KEY_SEPARATOR + startRow));
            scan.withStopRow(Bytes.toBytes(tag + HBaseConstant.ROW_KEY_SEPARATOR + endRow));
            scan.setCaching(10000);
            scan.setBatch(1000);
            int loop = 3600 / step;
            List<Filter> filters = new ArrayList<Filter>();
            for (int i = 0; i < loop; i++) {

                Filter filter = new QualifierFilter(CompareOp.EQUAL,
                        new BinaryComparator(Bytes.toBytes(0 + step * i)));
                filters.add(filter);

            }
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE, filters);
            scan.setFilter(filterList);
            ResultScanner scanner = table.getScanner(scan);

            int count = 0;
            List<com.aimsphm.nuclear.common.entity.dto.Cell> data = Lists.newArrayList();
            for (Result rs : scanner) {
                List<com.aimsphm.nuclear.common.entity.dto.Cell> items = new ArrayList();
                for (Cell cell : rs.listCells()) {

                    List<Object> item = new ArrayList<>();
                    double value = Bytes.toDouble(CellUtil.cloneValue(cell));
                    Long timestamp = cell.getTimestamp() / 1000l * 1000l;
                    if (timestamp > endTime) {//如果列的时间戳大于终点查询时间跳出
                        break;
                    }
                    if (timestamp < startTime) {//如果列的时间戳小于开始时间直接丢弃
                        continue;
                    }
                    count++;
//                    if (count % step != 0) {
//						continue;
//					}
                    com.aimsphm.nuclear.common.entity.dto.Cell cellone = new com.aimsphm.nuclear.common.entity.dto.Cell();
                    cellone.setTimestamp(timestamp);
                    cellone.setValue(value);
                    items.add(cellone);
                }
                data.addAll(items);
            }
            return data;
        } catch (IOException e) {
            throw e;
        }
    }


    public Map<String, List<com.aimsphm.nuclear.common.entity.dto.Cell>> selectMonitorDataList(String tableName, String tags, Long startTime, Long endTime, String family) throws IOException {
        TableName name = TableName.valueOf(tableName);

        try (Table table = connection.getTable(name)) {
            Long startRow = startTime / (1000 * 3600) * (1000 * 3600);
            Long offsetColumnl = startTime / 1000 % (3600);
            Integer offsetColumn = offsetColumnl.intValue();
            Long endRow = endTime / (1000 * 3600) * (1000 * 3600) + 1;
            long loop = startTime - startRow;
            List<Filter> filters = new ArrayList<Filter>();
//            for(int i=0;i<60;i++)
////            {
////
////                Filter filter = new QualifierFilter(CompareOp.EQUAL,
////                        new BinaryComparator(Bytes.toBytes(i+startTime)));
////                filters.add(filter);
////
////            }
            FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE, filters);

            String taga[] = tags.split(",");
            List<Get> getList = Lists.newArrayList();
            for (String tag : taga) {
                Get get = new Get(Bytes.toBytes(tag + HBaseConstant.ROW_KEY_SEPARATOR + startRow));
                get.addFamily(Bytes.toBytes(family));
                //  get.setFilter(filterList);
                for (int i = 0; i < 60; i++) {

                    get.addColumn(Bytes.toBytes(family), Bytes.toBytes(i + offsetColumn.intValue()));

                }
                getList.add(get);
            }
            Result[] results = table.get(getList);


            int count = 0;
            Map<String, List<com.aimsphm.nuclear.common.entity.dto.Cell>> map = Maps.newHashMap();

            for (Result rs : results) {
                List<com.aimsphm.nuclear.common.entity.dto.Cell> data = Lists.newArrayList();
                if (ObjectUtils.isEmpty(rs.getRow())) {
                    continue;
                }
                String rowkey = Bytes.toString(rs.getRow());
                List<com.aimsphm.nuclear.common.entity.dto.Cell> items = new ArrayList();
                for (Cell cell : rs.listCells()) {

                    List<Object> item = new ArrayList<>();
                    double value = Bytes.toDouble(CellUtil.cloneValue(cell));
                    Long timestamp = cell.getTimestamp() / 1000l * 1000l;
                    if (timestamp > endTime) {//如果列的时间戳大于终点查询时间跳出
                        break;
                    }
                    if (timestamp < startTime) {//如果列的时间戳小于开始时间直接丢弃
                        continue;
                    }
                    count++;
//                    if (count % step != 0) {
//						continue;
//					}
                    com.aimsphm.nuclear.common.entity.dto.Cell cellone = new com.aimsphm.nuclear.common.entity.dto.Cell();
                    cellone.setTimestamp(timestamp);
                    cellone.setValue(value);
                    items.add(cellone);
                }
                data.addAll(items);
                map.put(rowkey.split("_")[0], data);
            }
            return map;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * @param tableName 表格名称
     * @param start     开始rowKey
     * @param end       结束rowKey
     * @param family    列族
     * @param qualifier 列
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> selectModelTagDataList(String tableName, Long start, Long end, String family,
                                                            String qualifier, String modelId) throws IOException {
        TableName name = TableName.valueOf(tableName);
        String rowKeyStart = modelId + "m_" + start;
        String rowKeyEnd = modelId + "m_" + end;
        try (Table table = connection.getTable(name)) {
            Scan scan = new Scan();
            boolean isHasFamily = StringUtils.isEmpty(family);
            boolean isHasQualifier = StringUtils.isEmpty(qualifier);
            if (!isHasFamily) {
                scan.addFamily(Bytes.toBytes(family));
                if (!isHasQualifier) {
                    scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
                }
            }
            scan.withStartRow(Bytes.toBytes(rowKeyStart));
            scan.withStopRow(Bytes.toBytes(rowKeyEnd));
            ResultScanner scanner = table.getScanner(scan);
            Map<String, Set<String>> familyMap = Maps.newHashMap();
            Map<String, List<Object>> dataWithQualifier = Maps.newHashMap();
            for (Result rs : scanner) {
                assembleModelTagCellDataWithList(familyMap, dataWithQualifier, rs);

            }
            return assembleReturnDataWithList(dataWithQualifier, familyMap.entrySet());
        } catch (IOException e) {
            throw e;
        }
    }


    /**
     * @param tableName 表格名称
     * @param start     开始rowKey
     * @param end       结束rowKey
     * @param family    列族
     * @param qualifier 列
     * @return
     * @throws IOException
     */
    public List<EstimateResult> selectModelTagDataList2(String tableName, Long start, Long end, String family,
                                                        String qualifier, String modelId) throws IOException {
        TableName name = TableName.valueOf(tableName);
        String rowKeyStart = modelId + "m_" + start;
        String rowKeyEnd = modelId + "m_" + end;
        try (Table table = connection.getTable(name)) {
            Scan scan = new Scan();
            boolean isHasFamily = StringUtils.isEmpty(family);
            boolean isHasQualifier = StringUtils.isEmpty(qualifier);
            if (!isHasFamily) {
                scan.addFamily(Bytes.toBytes(family));
                if (!isHasQualifier) {
                    scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
                }
            }
            scan.withStartRow(Bytes.toBytes(rowKeyStart));
            scan.withStopRow(Bytes.toBytes(rowKeyEnd));
            ResultScanner scanner = table.getScanner(scan);
            List<EstimateResult> estimateResults = Lists.newArrayList();
            for (Result rs : scanner) {
                for (Cell cell : rs.listCells()) {
                    String value = Bytes.toString(CellUtil.cloneValue(cell));
                    EstimateResult es = gson.fromJson(value, EstimateResult.class);
                    estimateResults.add(es);
                }

            }
            return estimateResults;
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 插入模型记录
     *
     * @param modelId         模型名
     * @param timestamp       时间戳
     * @param estimateResults 算法預測結果
     */
    public void insertModelEstimateData(String modelId, Long timestamp, List<EstimateResult> estimateResults)
            throws IOException {
        for (EstimateResult er : estimateResults) {
            insertObject(HBaseConstant.TABLE_MODEL_ESTIMATE_RESULT, modelId + "m_" + timestamp, HBaseConstant.FAMILY_MODEL_ESTIMATE_RESULT, er.getTagId(), gson.toJson(er), timestamp);
        }
    }

    /**
     * @param tableName 表格名称
     * @param start     开始rowKey
     * @param end       结束rowKey
     * @param family    列族
     * @param qualifier 列
     * @return
     * @throws IOException
     */
    public List<EstimateTotal> selectModelDataList(String tableName, Long start, Long end, String family,
                                                   String qualifier, String modelId) throws IOException {
        TableName name = TableName.valueOf(tableName);
        String rowKeyStart = modelId + "m_" + start;
        String rowKeyEnd = modelId + "m_" + end;
        try (Table table = connection.getTable(name)) {
            Scan scan = new Scan();
            boolean isHasFamily = StringUtils.isEmpty(family);
            boolean isHasQualifier = StringUtils.isEmpty(qualifier);
            if (!isHasFamily) {
                scan.addFamily(Bytes.toBytes(family));
                if (!isHasQualifier) {
                    scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
                }
            }
            scan.withStartRow(Bytes.toBytes(rowKeyStart));
            scan.withStopRow(Bytes.toBytes(rowKeyEnd));
            ResultScanner scanner = table.getScanner(scan);
            Map<String, Set<String>> familyMap = Maps.newHashMap();
            Map<String, List<Object>> dataWithQualifier = Maps.newHashMap();

            List<EstimateTotal> ets = new ArrayList<>();
            for (Result rs : scanner) {
                EstimateTotal et = new EstimateTotal();
                assembleModelCellDataWithList(familyMap, dataWithQualifier, rs, et);
                ets.add(et);
            }
            return ets;
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
    private void assembleModelCellDataWithList(Map<String, Set<String>> familyMap, Map<String, List<Object>> withQualifier,
                                               Result rs, EstimateTotal et) {
        if (rs.size() == 0) {
            return;
        }
        for (Cell cell : rs.listCells()) {
            String family = Bytes.toString(CellUtil.cloneFamily(cell));
            String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
            String value = Bytes.toString(CellUtil.cloneValue(cell));
            EstimateResult es = gson.fromJson(value, EstimateResult.class);
            et.getEstimateResults().add(es);
//            long timestamp = cell.getTimestamp();
//
//            if (!withQualifier.containsKey(qualifier)) {
//                withQualifier.put(qualifier, Lists.newArrayList());
//            }
//            if (!familyMap.containsKey(family)) {
//                familyMap.put(family, Sets.newHashSet());
//            }
//            familyMap.get(family).add(qualifier);
//            withQualifier.get(qualifier).add(es);

//            withQualifier.get(qualifier).add(Lists.newArrayList(timestamp, es));
        }
    }

    /**
     * @param familyMap     列族集合
     * @param withQualifier 列数据集合(以集合方式返回)
     * @param rs            结果集
     */
    private void assembleModelTagCellDataWithList(Map<String, Set<String>> familyMap, Map<String, List<Object>> withQualifier, Result rs) {
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
    private void assembleCellDataWithList(Map<String, Set<String>> familyMap, Map<String, List<List<Object>>> withQualifier, Result rs) {
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
    private void assembleCellData(Map<String, Set<String>> familyMap, Map<String, List<Object>> qualifierMap, Result rs) {
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
