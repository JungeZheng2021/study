package com.aimsphm.nuclear.common.service.impl;

import com.aimsphm.nuclear.common.entity.MdDevice;
import com.aimsphm.nuclear.common.entity.TxTurbinesnapshot;
import com.aimsphm.nuclear.common.service.MdDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
/**
 * 
 *
 * @author lu.yi
 * @since 2020-03-18
 */

import com.aimsphm.nuclear.common.entity.TxPumpsnapshot;
import com.aimsphm.nuclear.common.mapper.TxPumpsnapshotMapper;
import com.aimsphm.nuclear.common.service.AlgorithmCacheService;
import com.aimsphm.nuclear.common.service.TxPumpsnapshotService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.mybatisPlusConfig", name = "enable", havingValue = "true",matchIfMissing= false)
public class TxPumpsnapshotServiceImpl extends ServiceImpl<TxPumpsnapshotMapper, TxPumpsnapshot> implements TxPumpsnapshotService {

    @Autowired
    private TxPumpsnapshotMapper txPumpsnapshotMapper;
    @Autowired
    private AlgorithmCacheService algorithmCacheService;

    @Autowired
	MdDeviceService mdDeviceService;
    @Override
    public TxPumpsnapshot getTxPumpsnapshot(Long id) {
    	return this.getById(id);
    }


	@Override
	public TxPumpsnapshot getTxPumpsnapshotByDeviceId(Long deviceId,Integer additionalType) {
		QueryWrapper<TxPumpsnapshot> qw = new QueryWrapper<>();
		qw.lambda().eq(TxPumpsnapshot::getDeviceId, deviceId).orderByDesc(TxPumpsnapshot::getSnapshotTime);
		TxPumpsnapshot tp = algorithmCacheService.getPumpSnapshot(deviceId);
		if (tp != null) {
			tp.setAdditionalType(additionalType);
			return tp;
		}
		TxPumpsnapshot tt = new TxPumpsnapshot();
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
		return tt;
	}
	@Override
	public List<TxPumpsnapshot> getSnapshotByDeviceIdAndDuration(Long deviceId, Long start, Long end) {
		QueryWrapper<TxPumpsnapshot> qw = new QueryWrapper<>();
		qw.lambda().eq(TxPumpsnapshot::getDeviceId, deviceId).ge(TxPumpsnapshot::getSnapshotTime,new Date(start)).le(TxPumpsnapshot::getSnapshotTime,new Date(end)).orderByDesc(TxPumpsnapshot::getSnapshotTime);
		return this.list(qw);
	}
}