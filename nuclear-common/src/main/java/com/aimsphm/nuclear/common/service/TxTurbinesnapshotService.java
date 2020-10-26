package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.TxTurbinesnapshot;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-06-09
 */
public interface TxTurbinesnapshotService extends IService<TxTurbinesnapshot> {

    TxTurbinesnapshot getTxTurbinesnapshotByDeviceId(Long deviceId,Integer additionalType);

    List<TxTurbinesnapshot> getSnapshotByDeviceIdAndDuration(Long deviceId, Long start, Long end);
}