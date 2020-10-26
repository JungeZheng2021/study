package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.TxAlarmRealtime;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-04-07
 */
public interface TxAlarmRealtimeService extends IService<TxAlarmRealtime> {

    List<TxAlarmRealtime> listAll(String tagId, Long start, Long end,Long modelId);

    List<TxAlarmRealtime> listAll(String tagId, Long start, Long end, Long modelId, Boolean isAlgo);
}