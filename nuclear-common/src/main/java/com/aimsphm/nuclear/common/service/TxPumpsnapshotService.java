package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.TxPumpsnapshot;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-03-18
 */
public interface TxPumpsnapshotService extends IService<TxPumpsnapshot> {

	TxPumpsnapshot getTxPumpsnapshot(Long id);

	TxPumpsnapshot getTxPumpsnapshotByDeviceId(Long deviceId,Integer additionalType);

	List<TxPumpsnapshot> getSnapshotByDeviceIdAndDuration(Long deviceId, Long start, Long end);
}