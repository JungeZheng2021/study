package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.MdDevice;
import com.aimsphm.nuclear.common.entity.TxPumpsnapshot;
import com.aimsphm.nuclear.common.entity.TxRotatingsnapshot;
import com.aimsphm.nuclear.common.entity.TxTurbinesnapshot;
import com.aimsphm.nuclear.common.mapper.TxTurbinesnapshotMapper;
import com.aimsphm.nuclear.common.service.AlgorithmCacheService;
import com.aimsphm.nuclear.common.service.MdDeviceService;
import com.aimsphm.nuclear.common.service.TxTurbinesnapshotService;
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
 * @since 2020-06-09
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class TxTurbinesnapshotServiceImpl extends ServiceImpl<TxTurbinesnapshotMapper, TxTurbinesnapshot> implements TxTurbinesnapshotService {

    @Autowired
    private TxTurbinesnapshotMapper TxTurbinesnapshotMapper;

    @Autowired
    AlgorithmCacheService algorithmCacheService;
    @Autowired
    MdDeviceService mdDeviceService;
    @Override
    public TxTurbinesnapshot getTxTurbinesnapshotByDeviceId(Long deviceId,Integer additionalType) {
        QueryWrapper<TxTurbinesnapshot> qw = new QueryWrapper<>();
        qw.lambda().eq(TxTurbinesnapshot::getDeviceId, deviceId).orderByDesc(TxTurbinesnapshot::getSnapshotTime);
        TxTurbinesnapshot tt = algorithmCacheService.getTurbineSnapshot(deviceId);
        if (tt != null) {
            return tt;
        }
        tt = this.list(qw).stream().findFirst().orElse(null);
        if (tt == null) {
            tt = new TxTurbinesnapshot();
            MdDevice mdDevice = mdDeviceService.getById(deviceId);
            tt.setDeviceId(deviceId);
            tt.setDeviceName(mdDevice!=null?mdDevice.getDeviceName():"");
            tt.setHealthStatus(0);
            tt.setHeathRunningTime(0l);
            tt.setSnapshotTime(new Date());
            tt.setStopTimes(0);
            tt.setTotalRunningDuration(0l);
            tt.setAdditionalType(additionalType);
            this.save(tt);
        }
        return tt;
    }

    @Override
    public List<TxTurbinesnapshot> getSnapshotByDeviceIdAndDuration(Long deviceId, Long start, Long end) {
        QueryWrapper<TxTurbinesnapshot> qw = new QueryWrapper<>();
        qw.lambda().eq(TxTurbinesnapshot::getDeviceId, deviceId).ge(TxTurbinesnapshot::getSnapshotTime,new Date(start)).le(TxTurbinesnapshot::getSnapshotTime,new Date(end)).orderByDesc(TxTurbinesnapshot::getSnapshotTime);
        return this.list(qw);
    }
}