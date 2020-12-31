package com.aimsphm.nuclear.algorithm.service;

import com.aimsphm.nuclear.algorithm.entity.bo.PointDataBO;

import java.util.concurrent.CountDownLatch;

/**
 * @Package: com.aimsphm.nuclear.algorithm.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/23 16:11
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/23 16:11
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmAsyncService {


    /**
     * 异步查询HBase数据
     *
     * @param family         列族
     * @param pre            前缀
     * @param data           测点信息
     * @param countDownLatch 同步计数器
     */
    void listPointDataFromHBase(String family, String pre, PointDataBO data, CountDownLatch countDownLatch);
}
