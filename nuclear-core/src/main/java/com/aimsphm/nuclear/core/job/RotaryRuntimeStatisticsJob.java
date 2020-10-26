package com.aimsphm.nuclear.core.job;

import com.aimsphm.nuclear.common.annotation.DistributedLock;
import com.aimsphm.nuclear.common.entity.MdDevice;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointTimesScaleVO;
import com.aimsphm.nuclear.common.mapper.MdDeviceMapper;
import com.aimsphm.nuclear.pump.service.PumpEquipmentMonitoringService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 旋机运行时常统计job
 *
 * @author milla
 */
@Component
@Slf4j
public class RotaryRuntimeStatisticsJob {

    @Autowired
    private MdDeviceMapper deviceMapper;
    @Autowired
    private PumpEquipmentMonitoringService monitoringService;


    //定时器 每天凌晨1点执行一次
    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(cron = "*/10 * * * * ?")
    //分布式执行锁
    @DistributedLock("RotaryRuntimeStatisticsJob")
    public void monitor() {
        try {
            //获取所有的旋机
            LambdaQueryWrapper<MdDevice> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MdDevice::getDeviceType, 2);
            List<MdDevice> devices = deviceMapper.selectList(wrapper);
            if (CollectionUtils.isEmpty(devices)) {
                return;
            }
            for (int i = 0; i < devices.size(); i++) {
                MdDevice device = devices.get(i);
                MeasurePointTimesScaleVO statistics = monitoringService.getMonitorStatisticsOfRotary(device.getId());
                log.info(device.getDeviceName() + " 执行配置更新:{}", statistics);
            }
        } catch (Exception e) {
            log.error("配置更新失败：{}", e);
        } finally {
        }
    }

}
