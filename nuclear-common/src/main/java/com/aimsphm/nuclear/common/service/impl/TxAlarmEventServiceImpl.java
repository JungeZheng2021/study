package com.aimsphm.nuclear.common.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.aimsphm.nuclear.common.entity.TxAlarmEvent;
import com.aimsphm.nuclear.common.mapper.TxAlarmEventMapper;
import com.aimsphm.nuclear.common.service.TxAlarmEventService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-04-07
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class TxAlarmEventServiceImpl extends ServiceImpl<TxAlarmEventMapper, TxAlarmEvent>
		implements TxAlarmEventService {

	@Autowired
	private TxAlarmEventMapper txAlarmEventMapper;

	@Override
	public List<TxAlarmEvent> getDeviceWarnings(Long deviceId,Long invokingTime) {

		QueryWrapper<TxAlarmEvent> queryWrapper = new QueryWrapper<>();
		Date now = new Date();
		Date cutdowntime = new Date(now.getTime() - 180 * 3600 * 24 * 1000l);
		Date invokingtime = new Date(invokingTime);
		queryWrapper.lambda().gt(TxAlarmEvent::getLastAlarm, cutdowntime);
		queryWrapper.lambda().lt(TxAlarmEvent::getLastAlarm, invokingtime);
		TxAlarmEvent ta = new TxAlarmEvent();
		ta.setDeviceId(deviceId);
		ta.setStopFlag(false);
		queryWrapper.setEntity(ta);
		queryWrapper.orderBy(true, false, "last_alarm");
		return this.list(queryWrapper);
	}
}