package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.TxAlarmRealtime;
import com.aimsphm.nuclear.common.mapper.TxAlarmRealtimeMapper;
import com.aimsphm.nuclear.common.service.TxAlarmRealtimeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author lu.yi
 * @since 2020-04-07
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class TxAlarmRealtimeServiceImpl extends ServiceImpl<TxAlarmRealtimeMapper, TxAlarmRealtime> implements TxAlarmRealtimeService {

    @Autowired
    private TxAlarmRealtimeMapper TxAlarmRealtimeMapper;

    @Override
    public List<TxAlarmRealtime> listAll(String tagId, Long start, Long end, Long modelId) {
        return listAll(tagId, start, end, modelId,true);
    }
    @Override
    public List<TxAlarmRealtime> listAll(String tagId, Long start, Long end, Long modelId,Boolean isAlgo) {
        QueryWrapper<TxAlarmRealtime> qw = new QueryWrapper<>();

        qw.lambda()
                .eq(TxAlarmRealtime::getModelId,modelId)
                .eq(TxAlarmRealtime::getSensorTagid, tagId).eq(TxAlarmRealtime::getIsAlgorithmAlarm,isAlgo).ge(TxAlarmRealtime::getAlarmTime, new Date(start)).le(TxAlarmRealtime::getAlarmTime, new Date(end));

        if (modelId.longValue() != -1l) {
            qw.lambda().eq(TxAlarmRealtime::getModelId, modelId);
        }
        return this.list(qw);
    }
}