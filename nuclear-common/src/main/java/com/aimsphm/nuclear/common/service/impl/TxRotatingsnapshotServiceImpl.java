package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.MdDevice;
import com.aimsphm.nuclear.common.entity.TxRotatingsnapshot;
import com.aimsphm.nuclear.common.mapper.TxRotatingsnapshotMapper;
import com.aimsphm.nuclear.common.service.AlgorithmCacheService;
import com.aimsphm.nuclear.common.service.MdDeviceService;
import com.aimsphm.nuclear.common.service.TxRotatingsnapshotService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author lu.yi
 * @since 2020-08-18
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true", matchIfMissing = false)
public class TxRotatingsnapshotServiceImpl extends ServiceImpl<TxRotatingsnapshotMapper, TxRotatingsnapshot> implements TxRotatingsnapshotService {

    @Autowired
    private TxRotatingsnapshotMapper txRotatingsnapshotMapper;
   @Autowired
    AlgorithmCacheService algorithmCacheService;
   @Autowired
    MdDeviceService mdDeviceService;
    @Override
    public TxRotatingsnapshot getTxRotatingsnapshotByDeviceId(Long deviceId, Integer additionalType) {
        QueryWrapper<TxRotatingsnapshot> qw = new QueryWrapper<>();
        qw.lambda().eq(TxRotatingsnapshot::getDeviceId, deviceId).orderByDesc(TxRotatingsnapshot::getSnapshotTime);
        TxRotatingsnapshot tt =algorithmCacheService.getRotatingsnapshot(deviceId);
        if (tt != null) {
            return tt;
        }
        tt = this.list(qw).stream().findFirst().orElse(null);
        if (tt == null) {
            MdDevice mdDevice = mdDeviceService.getById(deviceId);
            tt = new TxRotatingsnapshot();
            tt.setDeviceId(deviceId);
            tt.setDeviceName(mdDevice!=null?mdDevice.getDeviceName():"");
            tt.setHealthStatus(0);
            tt.setHeathRunningTime(0l);
            tt.setSnapshotTime(new Date());
            tt.setStopTimes(0);
            tt.setTotalRunningDuration(0l);
            tt.setAdditionalType(additionalType);
            tt.setConnectFaultSensor(0);
            tt.setFaultSensor(0);
            tt.setLowVoltageSensor(0);
            this.save(tt);
        }
        return tt;
    }

    @Override
    public List<TxRotatingsnapshot> getSnapshotByDeviceIdAndDuration(Long deviceId, Long start, Long end) {
        QueryWrapper<TxRotatingsnapshot> qw = new QueryWrapper<>();
        qw.lambda().eq(TxRotatingsnapshot::getDeviceId, deviceId).ge(TxRotatingsnapshot::getSnapshotTime,new Date(start)).le(TxRotatingsnapshot::getSnapshotTime,new Date(end)).orderByDesc(TxRotatingsnapshot::getSnapshotTime);
        return this.list(qw);
    }

}