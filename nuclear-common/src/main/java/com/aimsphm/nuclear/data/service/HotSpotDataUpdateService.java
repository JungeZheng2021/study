package com.aimsphm.nuclear.data.service;

import org.springframework.scheduling.annotation.Async;

/**
 * @Package: com.aimsphm.nuclear.data.service
 * @Description: <用于热点数据更新>
 * @Author: MILLA
 * @CreateDate: 2020/4/2 11:41
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/2 11:41
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface HotSpotDataUpdateService {
    /**
     * 更新指定的点的数据值
     *
     * @param itemId
     * @param value
     */
    void updatePIMeasurePoints(String itemId, Double value);

    void updateNonePIMeasurePoints(String itemId, Double value, Double temp1, Double temp2);
}
