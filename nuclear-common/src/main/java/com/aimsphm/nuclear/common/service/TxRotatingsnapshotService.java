package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.TxRotatingsnapshot;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-08-18
 */
public interface TxRotatingsnapshotService extends IService<TxRotatingsnapshot> {


    TxRotatingsnapshot getTxRotatingsnapshotByDeviceId(Long deviceId, Integer additionalType);
    List<TxRotatingsnapshot> getSnapshotByDeviceIdAndDuration(Long deviceId, Long start, Long end);
}