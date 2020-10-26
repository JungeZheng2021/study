package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.TxAlarmsnapshot;
import com.aimsphm.nuclear.common.mapper.TxAlarmsnapshotMapper;
import com.aimsphm.nuclear.common.service.TxAlarmsnapshotService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-06-09
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class TxAlarmsnapshotServiceImpl extends ServiceImpl<TxAlarmsnapshotMapper, TxAlarmsnapshot> implements TxAlarmsnapshotService {

    @Autowired
    private TxAlarmsnapshotMapper TxAlarmsnapshotMapper;

    @Transactional
    @Override
    public TxAlarmsnapshot getAndCreateAlarmSnapshot(Long deviceId){
        QueryWrapper<TxAlarmsnapshot> qw = new QueryWrapper<>();
        TxAlarmsnapshot ta = new TxAlarmsnapshot();
        ta.setDeviceId(deviceId);
        qw.setEntity(ta);
        TxAlarmsnapshot tae =  this.list(qw).stream().findAny().orElse(null);
        if(!ObjectUtils.isEmpty(tae))
        {
            return tae;
        }else{
            ta.setAlarmingStateRuntime(0l);
            ta.setNormalStateRuntime(0l);
            ta.setOverallAlarmingStateRuntime(0l);
            ta.setOverallNormalStateRuntime(0l);
            ta.setOverallWarningStateRuntime(0l);
            ta.setOverallWatchingStateRuntime(0l);
            ta.setWarningStateRuntime(0l);
            ta.setWatchingStateRuntime(0l);
            this.save(ta);
            return ta;
        }

    }
}