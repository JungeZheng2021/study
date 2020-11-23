package com.aimsphm.nuclear.history.service.impl;

import com.aimsphm.nuclear.common.entity.bo.HColumnQueryBO;
import com.aimsphm.nuclear.common.entity.dto.*;
import com.aimsphm.nuclear.common.exception.CustomMessageException;
import com.aimsphm.nuclear.common.util.HBaseUtil;
import com.aimsphm.nuclear.history.service.HBaseService;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.aimsphm.nuclear.common.constant.HBaseConstant.ROW_KEY_SEPARATOR;

/**
 * @Package: com.aimsphm.nuclear.hbase.service.impl
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/3/5 17:48
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/5 17:48
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Service
public class HBaseServiceImpl implements HBaseService {

    @Autowired
    private HBaseUtil hbase;

    @Override
    public void saveTable(HBaseTableDTO table) {
        Assert.hasText(table.getTableName(), "tableName can not be null");
        Assert.isTrue(table.getFamilies() != null && table.getFamilies().size() > 0, "family list can not be null");
        try {
            hbase.createTable(table.getTableName(), table.getFamilies(), Compression.Algorithm.SNAPPY);
        } catch (Exception e) {
            throw new CustomMessageException(e.getCause());
        }
    }

    @Override
    public void saveFamily2Table(HBaseTableDTO table) {
        Assert.hasText(table.getTableName(), "tableName can not be null");
        Assert.isTrue(table.getFamilies() != null && table.getFamilies().size() > 0, "family list can not be null");
        try {
            hbase.addFamily2Table(table.getTableName(), table.getFamilies(), Compression.Algorithm.SNAPPY);
        } catch (Exception e) {
            throw new CustomMessageException(e.getCause());
        }
    }

    @Override
    public void removeTable(String tableName) {
        Assert.hasText(tableName, "tableName can not be null");
        try {
            hbase.deleteTable(tableName);
        } catch (Exception e) {
            throw new CustomMessageException(e.getCause());
        }
    }

    @Override
    public void removeFamilyFromTable(String tableName, String family) {
        try {
            hbase.deleteFamily(tableName, family);
        } catch (Exception e) {
            throw new CustomMessageException(e.getCause());
        }

    }

    @Override
    public void saveItemData2Table(HBaseColumnItemDTO itemDTO) {
        checkHBaseParams(itemDTO);
        Assert.notNull(itemDTO.getValue(), "value can not be null");
        Assert.notNull(itemDTO.getQualifier(), "qualifier can not be null");
        try {
            String rowKey = itemDTO.getTag() + ROW_KEY_SEPARATOR + itemDTO.getTimestamp();
            hbase.insertDouble(itemDTO.getTableName(), rowKey, itemDTO.getFamily(), itemDTO.getQualifier(), itemDTO.getValue(), itemDTO.getTimestamp());
        } catch (Exception e) {
            throw new CustomMessageException(e.getCause());
        }
    }

    /**
     * 校验HBase参数是否合法
     *
     * @param itemDTO
     */
    private void checkHBaseParams(HBaseParamDTO itemDTO) {
        Assert.hasText(itemDTO.getTag(), "tag can not be null");
        Assert.hasText(itemDTO.getFamily(), "family can not be null");
        Assert.hasText(itemDTO.getTableName(), "tableName can not be null");
        Assert.notNull(itemDTO.getTimestamp(), "timestamp can not be null");
        Assert.isTrue(!itemDTO.getTag().contains(ROW_KEY_SEPARATOR), "tag can not contains '" + ROW_KEY_SEPARATOR + "'");
    }

    private void checkHBaseQuery(HColumnQueryBO itemDTO) {
        Assert.hasText(itemDTO.getTag(), "tag can not be null");
        Assert.hasText(itemDTO.getFamily(), "family can not be null");
        Assert.hasText(itemDTO.getTableName(), "tableName can not be null");
        Assert.notNull(itemDTO.getEndTime(), "end time can not be null");
        Assert.notNull(itemDTO.getStartTime(), "start time can not be null");
        Assert.isTrue(!itemDTO.getTag().contains(ROW_KEY_SEPARATOR), "tag can not contains '" + ROW_KEY_SEPARATOR + "'");
        Assert.isTrue(itemDTO.getStartTime() <= itemDTO.getEndTime(), "end time must be greater than the start time");

    }

    @Override
    public void saveItemData2TableByHour(HBaseColumnItemDTO itemDTO) {
        checkHBaseParams(itemDTO);
        Assert.isTrue(itemDTO.getValue() != null, "value can not be null");
        Long timestamp = itemDTO.getTimestamp();
        //3600列的索引
        Integer index = Math.toIntExact(timestamp / 1000 % 3600);
        itemDTO.setQualifier(index);
        try {
            String rowKey = itemDTO.getTag() + ROW_KEY_SEPARATOR + itemDTO.getTimestamp() / (1000 * 3600) * (1000 * 3600);
            hbase.insertDouble(itemDTO.getTableName(), rowKey, itemDTO.getFamily(), itemDTO.getQualifier(), itemDTO.getValue(), itemDTO.getTimestamp());
        } catch (Exception e) {
            throw new CustomMessageException(e);
        }

    }

    @Override
    public void saveItemsData2Table(HBaseColumnItemsDTO itemsDTO) {
        checkHBaseParams(itemsDTO);
        Assert.isTrue(!itemsDTO.getQualifiers().isEmpty(), "qualifiers can not be empty");
        List<HBaseColumnDoubleDTO> columnDTOList = itemsDTO.getQualifiers();
        List<Object> columns = columnDTOList.stream().map(HBaseColumnDoubleDTO::getQualifier).collect(Collectors.toList());
        List<Double> values = columnDTOList.stream().map(HBaseColumnDoubleDTO::getValue).collect(Collectors.toList());
        try {
            String rowKey = itemsDTO.getTag() + ROW_KEY_SEPARATOR + itemsDTO.getTimestamp();
            hbase.insertDoubles(itemsDTO.getTableName(), rowKey, itemsDTO.getFamily(), columns, values);
        } catch (Exception e) {
            throw new CustomMessageException(e.getCause());
        }
    }

    @Override
    public List<Map<String, Object>> listData(HColumnQueryBO queryBo) {
        checkHBaseQuery(queryBo);
        try {
            String start = queryBo.getTag() + ROW_KEY_SEPARATOR + queryBo.getStartTime();
            String end = queryBo.getTag() + ROW_KEY_SEPARATOR + queryBo.getEndTime();
            return hbase.selectDataList(queryBo.getTableName(), start, end, queryBo.getFamily(), queryBo.getQualifier());
        } catch (Exception e) {
            throw new CustomMessageException(e.getCause());
        }
    }


    @Override
    public Object getNewestData(HColumnQueryBO queryBo) {
        checkHBaseQuery(queryBo);
        try {
            String start = queryBo.getTag() + ROW_KEY_SEPARATOR + queryBo.getStartTime();
            String end = queryBo.getTag() + ROW_KEY_SEPARATOR + queryBo.getEndTime();
            return hbase.selectNewestData(queryBo.getTableName(), start, end, queryBo.getFamily());
        } catch (Exception e) {
            throw new CustomMessageException(e.getCause());
        }
    }

    @Override
    public void removeDataFromTable(String tableName, String rowKey, HBaseFamilyDTO familyDTO) {
        try {
            //删除指定的列数据
            if (StringUtils.hasText(familyDTO.getQualifier())) {
                Assert.hasText(familyDTO.getFamily(), "family can not be null");
                hbase.deleteColumnData(tableName, rowKey, familyDTO.getFamily(), familyDTO.getQualifier());
                return;
            }
            //删除指定的列族数据
            if (StringUtils.hasText(familyDTO.getFamily())) {
                hbase.deleteFamilyData(tableName, rowKey, familyDTO.getFamily());
                return;
            }
            //删除指定的行数据
            hbase.deleteRowData(tableName, rowKey);
        } catch (Exception e) {
            throw new CustomMessageException(e.getCause());
        }
    }

    @Override
    public Long getTableDataCount(String tableName) {
        try {
            return hbase.countRowsWithCoprocessor(tableName);
        } catch (Throwable e) {
            throw new CustomMessageException(e.getCause());
        }
    }

    @Override
    public List<Map<String, Object>> getDataByRowKey(String tableName, String rowKey, HBaseFamilyDTO familyDTO) {
        try {
            if (StringUtils.hasText(familyDTO.getQualifier())) {
                Assert.hasText(familyDTO.getFamily(), "family can not be null");
                return hbase.selectData(tableName, rowKey, familyDTO.getFamily(), familyDTO.getQualifier());
            }
            if (StringUtils.hasText(familyDTO.getFamily())) {
                return hbase.selectData(tableName, rowKey, familyDTO.getFamily());
            }
            return hbase.selectData(tableName, rowKey);
        } catch (Exception e) {
            throw new CustomMessageException(e.getCause());
        }
    }

    @Override
    public String getTableDescription(String tableName) {
        try {
            return hbase.tableDescription(tableName);
        } catch (Exception e) {
            throw new CustomMessageException(e.getCause());
        }
    }

}
