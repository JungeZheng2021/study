package com.aimsphm.nuclear.history.service;

import com.aimsphm.nuclear.common.entity.bo.HistoryQueryMultiBO;
import com.aimsphm.nuclear.history.entity.vo.HistoryDataVO;

import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.history.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/22 13:35
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/22 13:35
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmQueryService {

    /**
     * 获取滑动平均值
     *
     * @param multiBo 一个或者是多个测点
     * @return
     */
    Map<String, HistoryDataVO> listMovingAverageInfo(HistoryQueryMultiBO multiBo);

    /**
     * 获取预测值
     *
     * @param multiBo 一个或者是多个测点
     * @return
     */
    Map<String, HistoryDataVO> listPredictionInfo(HistoryQueryMultiBO multiBo);
}
