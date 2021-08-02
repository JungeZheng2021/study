package com.aimsphm.nuclear.algorithm.service;

import java.util.Date;

/**
 * <p>
 * 功能描述:降采样服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/07/27 13:57
 */
public interface DownSampleService {

    /**
     * 降采样
     */
    void execute();

    /**
     * 手动执行1次
     *
     * @param date          指定的时间
     * @param previousHours 指定需要向前查询几个小时
     */
    void executeOnce(Date date, Long previousHours);
}
