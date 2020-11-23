package com.aimsphm.nuclear.data.service;

/**
 * @Package: com.aimsphm.nuclear.common.service.ext.service
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/3/31 11:40
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/31 11:40
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface CommonDataService {
    /**
     * 解析数据
     *
     * @param topic   topic
     * @param message 消息
     */
    void operateData(String topic, String message);
}
