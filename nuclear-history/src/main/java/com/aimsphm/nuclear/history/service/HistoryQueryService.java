package com.aimsphm.nuclear.history.service;

import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleBO;
import com.aimsphm.nuclear.common.entity.dto.HBaseTimeSeriesDataDTO;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.history.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/11/21 17:42
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/21 17:42
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface HistoryQueryService {

    /**
     * 根据查询条件获取历史数据
     *
     * @param singleBO 测点信息
     * @return
     */
    List<HBaseTimeSeriesDataDTO> listHistoryWithSingleTagByScan(HistoryQuerySingleBO singleBO);

    List<HBaseTimeSeriesDataDTO> listHistoryWithSingleTagByRowKeyList(HistoryQuerySingleBO singleBO);
}
