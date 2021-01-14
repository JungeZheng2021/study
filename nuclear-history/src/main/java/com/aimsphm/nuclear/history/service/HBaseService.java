package com.aimsphm.nuclear.history.service;


import com.aimsphm.nuclear.common.entity.bo.DataAnalysisQueryBO;
import com.aimsphm.nuclear.common.entity.bo.HBaseQueryBO;
import com.aimsphm.nuclear.common.entity.dto.*;

import java.util.List;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.history.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/3/5 17:48
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/5 17:48
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface HBaseService {

    void saveTable(HBaseTableDTO table);

    void saveFamily2Table(HBaseTableDTO table);

    void removeTable(String tableName);

    void saveItemData2Table(HBaseColumnItemDTO itemDTO);

    void saveItemsData2Table(HBaseColumnItemsDTO itemsDTO);

    List<Map<String, Object>> listData(HBaseQueryBO queryBo);

    void removeDataFromTable(String tableName, String rowKey, HBaseFamilyDTO removeDTO);

    Long getTableDataCount(String tableName);

    List<Map<String, Object>> getDataByRowKey(String tableName, String rowKey, HBaseFamilyDTO familyDTO);

    String getTableDescription(String tableName);

    void removeFamilyFromTable(String tableName, String family);


    Object getNewestData(HBaseQueryBO queryBo);

    void saveItemData2TableByHour(HBaseColumnItemDTO itemDTO);

    /**
     * 获取原始数据[只要是数据是数组的都可以获取]
     *
     * @param query 查询条件
     * @return map
     */
    Map<String, Map<Long, Object>> listArrayData(DataAnalysisQueryBO query);
}
