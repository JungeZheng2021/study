package com.study.mq.service;

import com.study.mq.util.entity.DataQuery;

/**
 * @Package: com.example.mq.service
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/08/05 09:51
 * @UpdateUser: milla
 * @UpdateDate: 2020/08/05 09:51
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface IOrderService {
    int saveOrder();

    void disabled(String url);

    void enable(DataQuery query);
}