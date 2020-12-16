package com.aimsphm.nuclear.history.service;

import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleBO;
import com.aimsphm.nuclear.common.entity.bo.HistoryQuerySingleWithFeatureBO;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;

import java.util.List;
import java.util.Map;

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
    List<List<Object>> listHistoryDataWithPointByScan(HistoryQuerySingleWithFeatureBO singleBO);


    /**
     * 根据查询条件获取历史数据
     *
     * @param singleBO
     * @return
     */
    HistoryDataVO listHistoryDataWithPointByScan(HistoryQuerySingleBO singleBO);

    /**
     * 查询多个测点的历史数据
     *
     * @param queryMultiBO
     * @return
     */
    Map<String, HistoryDataVO> listHistoryDataWithPointIdsByScan(HistoryQueryMultiBO queryMultiBO);

    /**
     * 查询多个测点的历史数据
     *
     * @param queryMultiBO 查询条件
     * @return
     */
    Map<String, HistoryDataVO> listHistoryDataWithPointIdsByGetList(HistoryQueryMultiBO queryMultiBO);
}
