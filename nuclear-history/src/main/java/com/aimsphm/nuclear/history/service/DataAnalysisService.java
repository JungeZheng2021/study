package com.aimsphm.nuclear.history.service;

import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;

import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.history.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2021/01/08 16:11
 * @UpdateUser: MILLA
 * @UpdateDate: 2021/01/08 16:11
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface DataAnalysisService {

    /**
     * 振动分析数据接口
     *
     * @param queryMultiBO
     * @return
     */
    Map<String, HistoryDataVO> listAnalysisDataWithPointList(HistoryQueryMultiBO queryMultiBO);
}
