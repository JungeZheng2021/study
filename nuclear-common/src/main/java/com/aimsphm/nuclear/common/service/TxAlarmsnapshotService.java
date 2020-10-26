package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.TxAlarmsnapshot;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-06-09
 */
public interface TxAlarmsnapshotService extends IService<TxAlarmsnapshot> {

    @Transactional
    TxAlarmsnapshot getAndCreateAlarmSnapshot(Long deviceId);
}