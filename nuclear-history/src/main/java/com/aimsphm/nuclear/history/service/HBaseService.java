package com.aimsphm.nuclear.history.service;


import com.aimsphm.nuclear.common.entity.bo.HColumnQueryBO;
import com.aimsphm.nuclear.common.entity.bo.HColumnQueryExtendBO;
import com.aimsphm.nuclear.common.entity.bo.HModelEstimateQueryBO;
import com.aimsphm.nuclear.common.entity.dto.*;
import com.aimsphm.nuclear.common.pojo.EstimateResult;

import java.io.IOException;
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

    List<Map<String, Object>> listData(HColumnQueryBO queryBo);

    void removeDataFromTable(String tableName, String rowKey, HBaseFamilyDTO removeDTO);

    Long getTableDataCount(String tableName);

    List<Map<String, Object>> getDataByRowKey(String tableName, String rowKey, HBaseFamilyDTO familyDTO);

    String getTableDescription(String tableName);

    void removeFamilyFromTable(String tableName, String family);


    Object getNewestData(HColumnQueryBO queryBo);

    void saveItemData2TableByHour(HBaseColumnItemDTO itemDTO);

}
