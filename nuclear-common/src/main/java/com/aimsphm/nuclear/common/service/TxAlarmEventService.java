package com.aimsphm.nuclear.common.service;

import java.util.List;

import com.aimsphm.nuclear.common.entity.TxAlarmEvent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-04-07
 */
public interface TxAlarmEventService extends IService<TxAlarmEvent> {

	List<TxAlarmEvent> getDeviceWarnings(Long deviceId,Long invokingTime);

}